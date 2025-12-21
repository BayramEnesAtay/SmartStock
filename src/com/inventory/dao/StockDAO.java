package com.inventory.dao;

import com.inventory.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * StockDAO
 * --------
 * Satış / depo çıkışı sonrası stok düşürür.
 * Trigger varsa otomatik sipariş oluşur.
 */
public class StockDAO {

    // 🔥 userId parametresi eklendi
    public void sellProduct(int productId, int quantity, int userId) throws Exception {

        Connection conn = DbConnection.getConnection();
        conn.setAutoCommit(false);

        // stok düş
        PreparedStatement ps = conn.prepareStatement(
                "UPDATE stock SET quantity = quantity - ? WHERE product_id = ?"
        );
        ps.setInt(1, quantity);
        ps.setInt(2, productId);
        ps.executeUpdate();

        // 🔥 consumption_log'a user_id ile yaz
        PreparedStatement logPs = conn.prepareStatement(
                """
                INSERT INTO consumption_log (product_id, used_quantity, user_id)
                VALUES (?, ?, ?)
                """
        );
        logPs.setInt(1, productId);
        logPs.setInt(2, quantity);
        logPs.setInt(3, userId);
        logPs.executeUpdate();

        conn.commit();
        conn.close();
    }
}
