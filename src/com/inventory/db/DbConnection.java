package com.inventory.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/StockManagement";

    private static final String USER = "postgres";
    private static final String PASSWORD = "A.b.c.179";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("PostgreSQL bağlantı hatası!", e);
        }
    }
}
