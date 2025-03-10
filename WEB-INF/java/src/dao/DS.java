package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DS {
    private Properties properties;

    public DS() throws IOException {
        try{
            properties = new Properties();
            properties.load(new FileInputStream("/home/infoetu/yvan.serikoff.etu/config.prop"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        try{
            Class.forName(properties.getProperty("driver"));
            return DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("login"), properties.getProperty("password"));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}