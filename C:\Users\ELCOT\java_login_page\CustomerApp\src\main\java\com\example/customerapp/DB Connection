package com.example.customerapp;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Loads database credentials from db.properties (on the classpath)
 * and hands out JDBC connections to PostgreSQL.
 */
public final class DBConnection {

    private static final String PROPERTIES_FILE = "db.properties";
    private static Properties props;

    private DBConnection() {
    }

    private static synchronized Properties loadProperties() {
        if (props == null) {
            props = new Properties();
            try (InputStream input = DBConnection.class.getClassLoader()
                    .getResourceAsStream(PROPERTIES_FILE)) {
                if (input == null) {
                    throw new RuntimeException(
                            "Could not find " + PROPERTIES_FILE + " on the classpath. " +
                            "Make sure it exists under src/main/resources.");
                }
                props.load(input);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load " + PROPERTIES_FILE, e);
            }
        }
        return props;
    }

    public static Connection getConnection() throws SQLException {
        Properties p = loadProperties();
        String url = p.getProperty("db.url");
        String username = p.getProperty("db.username");
        String password = p.getProperty("db.password");
        return DriverManager.getConnection(url, username, password);
    }
}
