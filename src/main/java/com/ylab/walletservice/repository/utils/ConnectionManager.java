package com.ylab.walletservice.repository.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final ConnectionManager ourInstance = new ConnectionManager();

    private ConnectionManager() {
    }

    public static ConnectionManager getInstance() {
        return ourInstance;
    }

    public Connection getConnection() {
        Connection connection;
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("db.properties"));
            Class.forName(properties.getProperty("driver"));
            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return connection;
    }
}
