package com.inventory.dao;

import com.inventory.db.DbConnection;
import com.inventory.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public List<Order> getAllOrders() {

        List<Order> orders = new ArrayList<>();

        String sql = """
            SELECT o.order_id,
                   p.name AS product_name,
                   o.order_quantity,
                   o.status
            FROM orders o
            JOIN products p ON o.product_id = p.product_id
            ORDER BY o.order_id DESC
        """;

        try (Connection conn = DbConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                orders.add(
                        new Order(
                                rs.getInt("order_id"),
                                rs.getString("product_name"),
                                rs.getInt("order_quantity"),
                                rs.getString("status")
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }
}
