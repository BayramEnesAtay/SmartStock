package com.inventory.service;

import com.inventory.db.DbConnection;
import java.sql.Connection;

public class DatabaseService {

    public Connection getConnection() {
        return DbConnection.getConnection();
    }
}
