package uz.pdp.botcamp.entityRepository;

import uz.pdp.botcamp.config.DbConfig;
import uz.pdp.botcamp.entity.User;
import uz.pdp.botcamp.input.InPutScanner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
   static Connection connection= DbConfig.getConnection();
    public static List<User>users=new ArrayList<>();

   public static void refreshUser(){
       users.clear();
       try {
           Statement statement = connection.createStatement();
           ResultSet resultSet = statement.executeQuery("select *from users");
           while (resultSet.next()){
               User user=new User();
               user.setId(resultSet.getInt(1));
               user.setFirst_name(resultSet.getString(2));
               user.setLast_name(resultSet.getString(3));
               user.setPhone(resultSet.getString(4));
               user.setPassword(resultSet.getString(5));
               user.setActive(resultSet.getBoolean(6));
               user.setRole(resultSet.getString(8));
               users.add(user);
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }

    public static User hasUser(String phone, String password) {
       User Currentuser=null;
       String patternPassword="";
        for (User user : users) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select concat('16',md5(?),'75')");
                preparedStatement.setString(1,password);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    patternPassword=resultSet.getString(1);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            if(user.getPhone().equals(phone) && user.getPassword().equals(patternPassword)) {
                return user;
            }
        }
        return Currentuser;
    }

    public static boolean hasPhone(String phone) {
        for (User user : users) {
            if(user.getPhone().equals(phone))
                return true;
        }
        return false;
    }

    public static boolean registerUser(String i_first_name, String i_last_name, String i_phone, String i_password) {
        try {
            CallableStatement callableStatement = connection.prepareCall("{call registeruser(?,?,?,?,?)}");
            callableStatement.setString(1,i_first_name);
            callableStatement.setString(2,i_last_name);
            callableStatement.setString(3,i_phone);
            callableStatement.setString(4,i_password);
            callableStatement.registerOutParameter(5,Types.BOOLEAN);
            callableStatement.execute();
            refreshUser();
            return callableStatement.getBoolean(5);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void registerNewUser() {
        System.out.println("Enter first_name:");
        String first_name = InPutScanner.SCANNERSTR.nextLine();
        System.out.println("Enter last_name:");
        String last_name = InPutScanner.SCANNERSTR.nextLine();
        System.out.println("Enter phone number:");
        String phone = InPutScanner.SCANNERSTR.nextLine();
        boolean hasphone = UserRepository.hasPhone(phone);
        if (!hasphone) {
            String password;
            String repassword;
            do {
                System.out.println("Enter your secret password:");
                password = InPutScanner.SCANNERSTR.nextLine();
                System.out.println("Enter your secret password again:");
                repassword = InPutScanner.SCANNERSTR.nextLine();
                if (!password.equals(repassword)) {
                    System.out.println("Passwords not mach try again");
                }
            } while (!password.equals(repassword));

            boolean register =registerUser(first_name, last_name, phone, password);
            if (register)
                System.out.println("you are registered");
            else
                System.out.println("Something wrong");
        }else {
            System.out.println("This number already exist");
        }
    }
}
