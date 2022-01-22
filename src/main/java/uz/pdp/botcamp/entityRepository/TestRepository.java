package uz.pdp.botcamp.entityRepository;

import uz.pdp.botcamp.config.DbConfig;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class TestRepository {
    static Connection connection = DbConfig.getConnection();

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
