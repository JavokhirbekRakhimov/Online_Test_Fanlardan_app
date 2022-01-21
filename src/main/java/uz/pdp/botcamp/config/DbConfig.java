package uz.pdp.botcamp.config;

import uz.pdp.botcamp.key.MyKey;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConfig {
    public static final String dbUser = "postgres"; //user
    public static final String dbPassword = MyKey.DbPassword; //parol
    static String url; //murojaat yo'li
    static String host = "localhost"; //kun.uz //192.168.22.11

    static String dbName = "test_app_my";
    static String port = "5432"; //postgres  //oracle //345 //mysql// 123


    //Ulanish un metod kk
    public static Connection getConnection()  {

        Connection connection = null;

        url = "jdbc:postgresql://" + host + ":" + port + "/" + dbName; //aynan qaysi yo'lga murojaat qilish
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(url, dbUser, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection; //failed success
    }
}
