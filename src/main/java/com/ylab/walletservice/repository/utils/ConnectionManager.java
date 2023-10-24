package com.ylab.walletservice.repository.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Provides a Singleton instance for managing database connections.
 * It reads database connection properties from a configuration file or system properties.
 * and provides methods to obtain a connection to the database.
 */
public class ConnectionManager {
    private static final ConnectionManager ourInstance = new ConnectionManager();

    private ConnectionManager() {
    }

    /**
     * Gets the singleton instance of ConnectionManager.
     *
     * @return instance of ConnectionManager
     */
    public static ConnectionManager getInstance() {
        return ourInstance;
    }

    /**
     * Retrieves a database connection based on the configured properties.
     *
     * @return a Connection object representing the database connection
     * @throws RuntimeException if there is an error reading the property file or connecting to the database
     */
    public Connection getConnection() {
        String url;
        String user;
        String password;
        String customEnv = System.getProperty("CUSTOM_ENV");
        if (customEnv == null) {
            Properties properties = new Properties();
            try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("db.properties")) {
                properties.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException("Property file reading error.", e);
            }
            url = properties.getProperty("url");
            user = properties.getProperty("username");
            password = properties.getProperty("password");
        } else {
            url = System.getProperty(customEnv + "_URL");
            user = System.getProperty(customEnv + "_USERNAME");
            password = System.getProperty(customEnv + "_PASSWORD");
        }
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return connection;
    }
}
