package uz.pdp.botcamp.entityRepository;

import uz.pdp.botcamp.config.DbConfig;
import uz.pdp.botcamp.entity.Subject;
import uz.pdp.botcamp.entity.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestRepository {
    public static List<Test>tests=new ArrayList<>();
    static Connection connection = DbConfig.getConnection();
    public static void refresh() {
        tests.clear();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select *from test where active=true order by id");
            while (resultSet.next()) {
               Test test=new Test();
                test.setId(resultSet.getInt(1));
                test.setUser_id(resultSet.getInt(2));
                test.setStart_time(resultSet.getTimestamp(3));
                test.setEnd_time(resultSet.getTimestamp(4));
                test.setActive(resultSet.getBoolean(5));
                tests.add(test);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static int addTest(int id) {
        int result = -1;
        try {
            CallableStatement callableStatement = connection.prepareCall("{call addtest(?,?)}");
            callableStatement.setInt(1, id);
            callableStatement.registerOutParameter(2, Types.INTEGER);
            callableStatement.execute();
            result = callableStatement.getInt(2);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String stopProcess(int userid, int test_id) {
        String resultr = "Something wrong";
        try {
            CallableStatement callableStatement = connection.prepareCall("{call stoptest(?,?,?)}");
            callableStatement.setInt(1, userid);
            callableStatement.setInt(2, test_id);
            callableStatement.registerOutParameter(3, Types.VARCHAR);
            callableStatement.execute();
            resultr = callableStatement.getString(3);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultr;
    }
}
