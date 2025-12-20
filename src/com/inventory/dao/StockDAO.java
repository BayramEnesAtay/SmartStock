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

    // Satış yapıldığında stok düşürülür
    public void sellProduct(int productId, int quantity) throws Exception {

        Connection conn = DbConnection.getConnection();

        PreparedStatement ps = conn.prepareStatement(
                "UPDATE stock SET quantity = quantity - ? WHERE product_id = ?"
        );

        ps.setInt(1, quantity);   // Satılan miktar
        ps.setInt(2, productId);  // Ürün

        ps.executeUpdate();
        conn.close();
    }
}
