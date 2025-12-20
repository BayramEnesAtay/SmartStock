package com.inventory.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    private static final String URL =
            System.getenv("DB_URL");

    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("PASSWORD");

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("PostgreSQL bağlantı hatası!", e);
        }
    }
}
