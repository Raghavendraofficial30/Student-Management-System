package com.sms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class to manage database connections using JDBC.
 * Implements a centralized connection factory pattern.
 */
public class DBConnection {

    // Database credentials and configuration
    // IMPORTANT: Update these credentials to match your local MySQL configuration
    private static final String URL = "jdbc:mysql://localhost:3306/student_management_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root"; // Change to your MySQL password

    // Static block to register the JDBC driver once when the class is loaded
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: MySQL JDBC Driver not found. Ensure mysql-connector-j jar is added to classpath.");
            e.printStackTrace();
        }
    }

    /**
     * Obtains a connection to the MySQL database.
     * 
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}

