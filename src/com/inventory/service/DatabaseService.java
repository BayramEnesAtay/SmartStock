package com.inventory.service;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface intended for future database integration.  The demo
 * application does not implement this interface; it serves as a
 * placeholder to indicate where JDBC logic would live once a
 * PostgreSQL connection is added.  The methods contain TODO
 * comments to remind developers to fill them in.
 */
public interface DatabaseService {

    /**
     * Obtain a JDBC connection to the PostgreSQL database.
     *
     * @return a new {@link Connection}
     * @throws SQLException if the connection cannot be established
     */
    Connection getConnection() throws SQLException;
    // TODO: Implement this method with proper JDBC connection details
}