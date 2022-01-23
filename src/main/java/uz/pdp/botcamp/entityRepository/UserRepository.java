package uz.pdp.botcamp.entityRepository;

import uz.pdp.botcamp.config.DbConfig;
import uz.pdp.botcamp.entity.Response;
import uz.pdp.botcamp.entity.User;
import uz.pdp.botcamp.input.InPutScanner;
import uz.pdp.botcamp.sms.SmsCode;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserRepository {
    static Connection connection = DbConfig.getConnection();
    public static List<User> users = new ArrayList<>();
    public static void refreshUser() {
        users.clear();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select *from users order by id");
            while (resultSet.next()) {
                User user = new User();
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
        User Currentuser = null;
        String patternPassword = "";
        for (User user : users) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select concat('16',md5(?),'75')");
                preparedStatement.setString(1, password);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    patternPassword = resultSet.getString(1);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (user.getPhone().equals(phone) && user.getPassword().equals(patternPassword)) {
                return user;
            }
        }
        return Currentuser;
    }

    public static boolean hasPhone(String phone) {
        for (User user : users) {
            if (user.getPhone().equals(phone))
                return true;
        }
        return false;
    }

    public static boolean registerUser(String i_first_name, String i_last_name, String i_phone, String i_password) {
        try {
            CallableStatement callableStatement = connection.prepareCall("{call registeruser(?,?,?,?,?)}");
            callableStatement.setString(1, i_first_name);
            callableStatement.setString(2, i_last_name);
            callableStatement.setString(3, i_phone);
            callableStatement.setString(4, i_password);
            callableStatement.registerOutParameter(5, Types.BOOLEAN);
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
        boolean rightNumber = Pattern.matches("[+]{1}[0-9]{12}", phone);
        if (!rightNumber) {
            System.out.println("Wrong number");
        } else {
            boolean hasphone = UserRepository.hasPhone(phone);
            if (!hasphone) {

                System.out.println("We send secret code please enter code");
                int code = SmsCode.getCode(phone);
                int limit = 3;
                System.out.println("Code: " + code);
                do {
                System.out.print("Enter code: ");
                int reciveCode = InPutScanner.SCANNERNUM.nextInt();

                    if (code == reciveCode) {
                        limit=-2;
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

                        boolean register = registerUser(first_name, last_name, phone, password);
                        if (register)
                            System.out.println("you are registered");
                        else
                            System.out.println("Something wrong");
                    } else {
                        limit--;
                        System.out.println("Wrong code you \n Number of remaining attempts " + limit);
                    }
                } while (limit > 0);
                if(limit!=-2) {
                    System.out.println("Try again letter");
                }
            } else {
                System.out.println("This number already exist");
            }
        }
    }

    public static void recoverCode(String phone) {
        boolean hasphone = UserRepository.hasPhone(phone);
        if (hasphone) {
            System.out.println("We send secret code please enter code");
            int code = SmsCode.getCode(phone);
            int limit = 3;
            System.out.println("Code: " + code);
            do {
            System.out.print("Enter code: ");
            int reciveCode = InPutScanner.SCANNERNUM.nextInt();

                if (code == reciveCode) {
                    limit=-2;
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

                    Response response = recoverNewCode( phone,password);
                    System.out.println(response.getMessage());
                } else {
                    limit--;
                    System.out.println("Wrong code you \n Number of remaining attempts " + limit);
                }
            } while (limit > 0);
            if (limit!=-2) {
                System.out.println("Try again letter");
            }
        } else
            System.out.println("You need to log in ");

    }

    private static Response recoverNewCode(String phone, String password) {
        Response response=new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call updateusercode(?,?,?,?)}");
            callableStatement.setString(1, phone);
            callableStatement.setString(2, password);
            callableStatement.registerOutParameter(3, Types.BOOLEAN);
            callableStatement.registerOutParameter(4, Types.VARCHAR);
            callableStatement.execute();
            response.setSuccess(callableStatement.getBoolean(3));
            response.setMessage(callableStatement.getString(4));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        refreshUser();
        return response;
    }

    public static void operatino() {
        boolean rec=true;
        while (rec) {
            System.out.println("------------------------------------------------");
            System.out.println("1.Show user");
            System.out.println("2.User active -> false");
            System.out.println("3.User active -> true");
            System.out.println("0.Exit");
            System.out.print("Enter number: ");
            int option = InPutScanner.SCANNERNUM.nextInt();
            switch (option) {
                case 1 -> showUser();
                case 2 -> blockUser();
                case 3 -> activeUser();
                case 0-> rec=false;
            }
        }
    }

    private static void activeUser() {
        List<Integer>index=new ArrayList<>();
        index.add(0);
        System.out.println("----------------------------------------------");
        for (User user : users) {
            if(!user.isActive()) {
                index.add(user.getId());
                System.out.println("* Id : " + user.getId());
                System.out.println("* Phone: " + user.getPhone());
                System.out.println("* First name: " + user.getFirst_name());
                System.out.println("* Last name: " + user.getLast_name());
                System.out.println("* Role: "+user.getRole());
                System.out.println("* Active: " + user.isActive());
                System.out.println("* Password: " + user.getPassword());
                System.out.println("--------------------------------------------");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        int id;
        do {
            System.out.println("Enter 0 if you want back");
            System.out.print("* Enter user id: ");
            id = InPutScanner.SCANNERNUM.nextInt();
            if(!index.contains(id))
                System.out.println("Wrong id");
        }while (!index.contains(id));
        if(id!=0) {
            Response response = activeUserById(id);
            System.out.println(response.getMessage());
        }
    }

    private static Response activeUserById(int id) {
        Response response=new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call active_user(?,?,?)}");
            callableStatement.setInt(1, id);
            callableStatement.registerOutParameter(2, Types.BOOLEAN);
            callableStatement.registerOutParameter(3, Types.VARCHAR);
            callableStatement.execute();
            response.setSuccess(callableStatement.getBoolean(2));
            response.setMessage(callableStatement.getString(3));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        refreshUser();
        return response;
    }

    private static void blockUser() {
        List<Integer>index=new ArrayList<>();
        index.add(0);
        System.out.println("----------------------------------------------");
        for (User user : users) {
            if(user.isActive()) {
                index.add(user.getId());
                System.out.println("* Id : " + user.getId());
                System.out.println("* Phone: " + user.getPhone());
                System.out.println("* First name: " + user.getFirst_name());
                System.out.println("* Last name: " + user.getLast_name());
                System.out.println("* Role: "+user.getRole());
                System.out.println("* Active: " + user.isActive());
                System.out.println("* Password: " + user.getPassword());
                System.out.println("--------------------------------------------");
            }
        }
        int id;
        do {
            System.out.println("* Enter 0 if you want back");
            System.out.print("*Enter user id: ");
            id = InPutScanner.SCANNERNUM.nextInt();
            if(!index.contains(id))
                System.out.println("Wrong id");
        }while (!index.contains(id));
         if (id!=0) {
             Response response = deleteUser(id);
             System.out.println(response.getMessage());
         }
    }

    private static Response deleteUser(int id) {
        Response response=new Response();
        try {
            CallableStatement callableStatement = connection.prepareCall("{call delete_user(?,?,?)}");
            callableStatement.setInt(1, id);
            callableStatement.registerOutParameter(2, Types.BOOLEAN);
            callableStatement.registerOutParameter(3, Types.VARCHAR);
            callableStatement.execute();
            response.setSuccess(callableStatement.getBoolean(2));
            response.setMessage(callableStatement.getString(3));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        refreshUser();
        return response;
    }

    private static void showUser() {
        System.out.println("----------------------------------------------");
        for (User user : users) {
            System.out.println("* Id : "+user.getId());
            System.out.println("* Phone: "+user.getPhone());
            System.out.println("* First name: "+user.getFirst_name());
            System.out.println("* Last name: "+user.getLast_name());
            System.out.println("* Role: "+user.getRole());
            System.out.println("* Active: "+user.isActive());
            System.out.println("* Password: "+user.getPassword());
            System.out.println("--------------------------------------------");
        }
    }
}
