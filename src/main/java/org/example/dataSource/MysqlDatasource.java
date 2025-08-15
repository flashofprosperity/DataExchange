package org.example.dataSource;

import org.example.dataSource.domain.CornorModelNo;
import org.example.dataSource.domain.XSJData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MysqlDatasource {



    String getAllInforSql = "select * from cornor_model_no";
    public Connection mysqlDatasourceInit() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ms_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "123456");
        System.out.println(connection);
        return connection;
    }

    public void updateXSJData(Connection connection, List<XSJData> list) throws SQLException {
        // 先删除所有现有数据
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM xsj_data");
        }

        // 然后重新插入所有数据
        String insertSql = "INSERT INTO xsj_data (" +
                "id, smc_code, xlm_code, start_time, end_time, " +
                "k_zero, source_lpos, source_apos, source_bpos, " +
                "source_cpos, source_dpos, now_apos, now_bpos, " +
                "now_cpos, now_dpos, now_lpos, now_lmd, " +
                "current_state) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
            for (XSJData data : list) {
                statement.setInt(1, data.getMysqlid());
                statement.setString(2, data.getSmcCode());
                statement.setString(3, data.getXlmCode());
                statement.setString(4, data.getStartTime());
                statement.setString(5, data.getEndTime());
                statement.setString(6, data.getkZero());
                statement.setString(7, data.getSourceLpos());
                statement.setString(8, data.getSourceApos());
                statement.setString(9, data.getSourceBpos());
                statement.setString(10, data.getSourceCpos());
                statement.setString(11, data.getSourceDpos());
                statement.setString(12, data.getNowApos());
                statement.setString(13, data.getNowBpos());
                statement.setString(14, data.getNowCpos());
                statement.setString(15, data.getNowDpos());
                statement.setString(16, data.getNowLpos());
                statement.setString(17, data.getNowLmd());
                statement.setString(18, data.getCurrentState());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    public List<CornorModelNo> getAllInfor (Connection connection) throws SQLException {
        List<CornorModelNo> resultCornorModelNo = new ArrayList<CornorModelNo>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(getAllInforSql);
        while (resultSet.next()) {
            CornorModelNo model = new CornorModelNo();
            // 设置各个字段
            // model.setSqliteid(resultSet.getInt("id"));
            // model.setModelNo(resultSet.getString("model_no"));
            model.setId(resultSet.getInt("id"));
            model.setModelNo(resultSet.getString("model_no"));
            model.setModelName(resultSet.getString("moldel_name"));
            model.setCapactity(resultSet.getString("capacity"));
            model.setEccDistance(resultSet.getString("ecc_distance"));
            model.setUpdateTime(resultSet.getDate("up_date"));
            model.setRemark(resultSet.getString("remark"));
            resultCornorModelNo.add(model);
        }
        resultSet.close();
        statement.close();

        return resultCornorModelNo;
    }

    public void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }
}
