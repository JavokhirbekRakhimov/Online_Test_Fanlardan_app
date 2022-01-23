package uz.pdp.botcamp.entityRepository;

import uz.pdp.botcamp.config.DbConfig;
import uz.pdp.botcamp.entity.Response;
import uz.pdp.botcamp.entity.Subject;
import uz.pdp.botcamp.entity.Test;
import uz.pdp.botcamp.input.InPutScanner;

import java.sql.*;
import java.text.SimpleDateFormat;
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

    public static void removeTestId(int id) {
        List<Integer>index=new ArrayList<>();
        index.add(0);
        System.out.println("--------------------------------------------------------");
        for (Test test : TestRepository.tests) {
            if(test.getUser_id()==id) {
                System.out.println("| * Id : " + test.getId() + " | Time: "+
                        new SimpleDateFormat("yyyy.MM.dd  hh:mm:ss").format(test.getStart_time())+"  |");
                index.add(test.getId());
            }
        }
        int test_id;
        System.out.println("--------------------------------------------------------");
        do {
            System.out.println("Enter 0 if you want back");
            System.out.print("Enter your test id: ");
            test_id = InPutScanner.SCANNERNUM.nextInt();
            System.out.println("----------------------------------------------");
        }while (!index.contains(test_id));
         if(test_id!=0) {
             Response response = deleteTestId(test_id);
             System.out.println(response.getMessage());
         }
    }

    private static Response deleteTestId(int test_id) {
        Response response=new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call delete_test(?,?,?)}");
            callableStatement.setInt(1, test_id);
            callableStatement.registerOutParameter(2,Types.BOOLEAN);
            callableStatement.registerOutParameter(3, Types.VARCHAR);
            callableStatement.execute();
            response.setSuccess(callableStatement.getBoolean(2));
            response.setMessage(callableStatement.getString(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        refresh();
        return response;
    }
}
