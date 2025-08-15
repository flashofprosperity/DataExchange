package org.example.dataSource;

import org.example.dataSource.domain.CornorModelNo;
import org.example.dataSource.domain.XSJData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqLiteDatasource {

    String getAllInforSql = "select * from XSJData";
    public Connection sqLiteDatasourceInit() throws SQLException, ClassNotFoundException {

        Class.forName("org.sqlite.JDBC");
        Connection  connection =DriverManager.getConnection("jdbc:sqlite:E:\\dev\\XSJD.db3?");
        System.out.println(connection);
        return connection;
    }

    public List<XSJData> getAllInfor (Connection connection) throws SQLException {
        List<XSJData> resultCornorModelNo = new ArrayList<XSJData>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(getAllInforSql);
        while (resultSet.next()) {
            XSJData model = new XSJData();
            model.setSqliteid(resultSet.getString("id"));
            model.setSmcCode(resultSet.getString("smc_code"));
            model.setXlmCode(resultSet.getString("xlm_code"));
            model.setStartTime(resultSet.getString("start_time"));
            model.setEndTime(resultSet.getString("end_time"));
            model.setkZero(resultSet.getString("k_zero"));
            model.setSourceLpos(resultSet.getString("source_lpos"));
            model.setSourceApos(resultSet.getString("source_apos"));
            model.setSourceBpos(resultSet.getString("source_bpos"));
            model.setSourceCpos(resultSet.getString("source_cpos"));
            model.setSourceDpos(resultSet.getString("source_dpos"));
            model.setNowLpos(resultSet.getString("now_lpos"));
            model.setNowApos(resultSet.getString("now_apos"));
            model.setNowBpos(resultSet.getString("now_bpos"));
            model.setNowCpos(resultSet.getString("now_cpos"));
            model.setNowDpos(resultSet.getString("now_dpos"));
            model.setNowLmd(resultSet.getString("now_lmd"));
            model.setActApos(resultSet.getString("act_apos"));
            model.setActCpos(resultSet.getString("act_cpos"));
            model.setActBpos(resultSet.getString("act_bpos"));
            model.setActDpos(resultSet.getString("act_dpos"));
            model.setCurrentState(resultSet.getString("current_state"));
            model.setMysqlid(Integer.parseInt(model.getSqliteid()));
            resultCornorModelNo.add(model);
        }
        resultSet.close();
        statement.close();

        return resultCornorModelNo;
    }


    public void updateModelNo(Connection  connection, List<CornorModelNo> list) throws SQLException {

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM cornor_model_no");
        }

        String UpdateSql = "INSERT OR REPLACE INTO cornor_model_no (model_no, moldel_name, capacity, ecc_distance, up_date, remark) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(UpdateSql)) {
            for (CornorModelNo model : list) {
                statement.setString(1, model.getModelNo());
                statement.setString(2, model.getModelName());
                statement.setString(3, model.getCapactity());
                statement.setString(4, model.getEccDistance());
                statement.setDate(5, model.getUpdateTime() != null ?
                        new java.sql.Date(model.getUpdateTime().getTime()) : null);
                statement.setString(6, model.getRemark());
                statement.addBatch();
            }
            statement.executeBatch();
        }

    }

    public void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }


}
