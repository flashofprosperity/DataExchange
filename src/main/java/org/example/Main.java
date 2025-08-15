package org.example;

import org.example.dataSource.MysqlDatasource;
import org.example.dataSource.SqLiteDatasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Scanner;


//TIP 要<b>运行</b>代码，请按 <shortcut actionId="Run"/> 或
// 点击装订区域中的 <icon src="AllIcons.Actions.Execute"/> 图标。
public class Main {
    public static void main(String[] args)  {
        ScheduleService scheduleService = new ScheduleService();

        long startTime;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextTime;

// 如果当前时间在凌晨0点到中午12点之间，则下一个时间点是中午12点
        if (now.getHour() < 12) {
            nextTime = LocalDateTime.of(now.toLocalDate(), LocalTime.NOON); // 当天中午12点
        }
// 如果当前时间在中午12点到23:59:59之间，则下一个时间点是第二天凌晨12点
        else {
            nextTime = LocalDateTime.of(now.toLocalDate().plusDays(1), LocalTime.MIDNIGHT); // 次日凌晨12点
        }

        // 转换为毫秒时间戳
        startTime = nextTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        System.out.println("下一个执行时间: " + nextTime);




        scheduleService.schedule( ()->{
            try {
                System.out.println("开始执行任务。"+System.currentTimeMillis());
                MysqlDatasource mysqlDatasource = new MysqlDatasource();
                SqLiteDatasource sqLiteDatasource = new SqLiteDatasource();
                Connection mysqlConnection = mysqlDatasource.mysqlDatasourceInit();
                Connection sqLiteConnection = sqLiteDatasource.sqLiteDatasourceInit();
                sqLiteDatasource.updateModelNo(sqLiteConnection, mysqlDatasource.getAllInfor(mysqlConnection));
                sqLiteDatasource.getAllInfor(sqLiteConnection);
                mysqlDatasource.updateXSJData(mysqlConnection, sqLiteDatasource.getAllInfor(sqLiteConnection));
                mysqlDatasource.closeConnection(mysqlConnection);
                sqLiteDatasource.closeConnection(sqLiteConnection);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            System.out.println("执行完毕。"+System.currentTimeMillis());

        }, 600000, startTime);


        //监听命令行输入
        //输入为update则立刻执行一次runnable
        //输入为quit则退出程序

        Thread commandThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入命令 (update 执行一次更新, quit 退出程序):");

            while (true) {
                String input = scanner.nextLine().trim().toLowerCase();

                switch (input) {
                    case "update":
                        // 立即执行一次任务
                        scheduleService.schedule(() -> {
                            try {
                                MysqlDatasource mysqlDatasource = new MysqlDatasource();
                                SqLiteDatasource sqLiteDatasource = new SqLiteDatasource();
                                Connection mysqlConnection = mysqlDatasource.mysqlDatasourceInit();
                                Connection sqLiteConnection = sqLiteDatasource.sqLiteDatasourceInit();
                                sqLiteDatasource.updateModelNo(sqLiteConnection, mysqlDatasource.getAllInfor(mysqlConnection));
                                sqLiteDatasource.getAllInfor(sqLiteConnection);
                                mysqlDatasource.updateXSJData(mysqlConnection, sqLiteDatasource.getAllInfor(sqLiteConnection));
                                mysqlDatasource.closeConnection(mysqlConnection);
                                sqLiteDatasource.closeConnection(sqLiteConnection);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.println("手动执行完毕。" + System.currentTimeMillis());
                        },0,System.currentTimeMillis());
                        System.out.println("已触发手动更新");
                        break;

                    case "quit":
                        System.out.println("正在退出程序...");
                        System.exit(0);
                        break;

                    default:
                        System.out.println("未知命令，请输入 'update' 或 'quit'");
                        break;
                }

                System.out.println("请输入命令 (update 执行一次更新, quit 退出程序):");
            }
        });

        commandThread.setDaemon(true); // 设置为守护线程
        commandThread.start();


    }
}