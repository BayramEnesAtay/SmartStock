package com.inventory.dao;

import com.inventory.db.DbConnection;
import com.inventory.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    /* =====================================================
       TÜM AKTİF ÜRÜNLERİ LİSTELE
       ===================================================== */
    public List<Product> getAllProducts() {

        List<Product> products = new ArrayList<>();

        String sql = """
            SELECT p.product_id,
                   p.name,
                   s.quantity,
                   p.unit_price
            FROM products p
            JOIN stock s ON p.product_id = s.product_id
            WHERE p.is_active = TRUE
            ORDER BY p.product_id
        """;

        try (Connection conn = DbConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    /* =====================================================
       ÜRÜN ADINA GÖRE ARAMA (SADECE AKTİFLER)
       ===================================================== */
    public List<Product> searchByName(String keyword) {

        List<Product> products = new ArrayList<>();

        String sql = """
            SELECT p.product_id,
                   p.name,
                   s.quantity,
                   p.unit_price
            FROM products p
            JOIN stock s ON p.product_id = s.product_id
            WHERE p.is_active = TRUE
              AND LOWER(p.name) LIKE ?
            ORDER BY p.product_id
        """;

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    /* =====================================================
       ÜRÜN EKLE (AKTİF OLARAK)
       ===================================================== */
    public void addProduct(String name, int quantity, double price, int minQuantity) {

        String productSql =
                "INSERT INTO products (name, unit_price, is_active) VALUES (?, ?, TRUE) RETURNING product_id";
        String stockSql =
                "INSERT INTO stock (product_id, quantity, min_quantity) VALUES (?, ?, ?)";

        try (Connection conn = DbConnection.getConnection()) {

            conn.setAutoCommit(false);

            PreparedStatement psProduct = conn.prepareStatement(productSql);
            psProduct.setString(1, name);
            psProduct.setDouble(2, price);

            ResultSet rs = psProduct.executeQuery();
            rs.next();
            int productId = rs.getInt(1);

            PreparedStatement psStock = conn.prepareStatement(stockSql);
            psStock.setInt(1, productId);
            psStock.setInt(2, quantity);
            psStock.setInt(3, minQuantity);
            psStock.executeUpdate();

            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* =====================================================
       ÜRÜN GÜNCELLE
       ===================================================== */
    public void updateProduct(Product p) {

        String productSql =
                "UPDATE products SET name = ?, unit_price = ? WHERE product_id = ?";
        String stockSql =
                "UPDATE stock SET quantity = ? WHERE product_id = ?";

        try (Connection conn = DbConnection.getConnection()) {

            conn.setAutoCommit(false);

            PreparedStatement psProduct = conn.prepareStatement(productSql);
            psProduct.setString(1, p.getName());
            psProduct.setDouble(2, p.getPrice());
            psProduct.setInt(3, p.getId());
            psProduct.executeUpdate();

            PreparedStatement psStock = conn.prepareStatement(stockSql);
            psStock.setInt(1, p.getQuantity());
            psStock.setInt(2, p.getId());
            psStock.executeUpdate();

            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* =====================================================
       ÜRÜN SİL
       KURAL:
       - Siparişi yoksa  → GERÇEK DELETE
       - Siparişi varsa → PASİF YAP
       ===================================================== */
    public void deleteProduct(int productId) {

        String orderCheckSql =
                "SELECT COUNT(*) FROM orders WHERE product_id = ?";
        String deactivateSql =
                "UPDATE products SET is_active = FALSE WHERE product_id = ?";
        String stockDeleteSql =
                "DELETE FROM stock WHERE product_id = ?";
        String productDeleteSql =
                "DELETE FROM products WHERE product_id = ?";

        try (Connection conn = DbConnection.getConnection()) {

            conn.setAutoCommit(false);

            // 1️⃣ Sipariş var mı kontrol et
            PreparedStatement psCheck =
                    conn.prepareStatement(orderCheckSql);
            psCheck.setInt(1, productId);
            ResultSet rs = psCheck.executeQuery();
            rs.next();
            int orderCount = rs.getInt(1);

            if (orderCount > 0) {
                // 🔥 Siparişi varsa → PASİF
                PreparedStatement psDeactivate =
                        conn.prepareStatement(deactivateSql);
                psDeactivate.setInt(1, productId);
                psDeactivate.executeUpdate();
            } else {
                // 🔥 Siparişi yoksa → GERÇEK SİL
                PreparedStatement psStock =
                        conn.prepareStatement(stockDeleteSql);
                psStock.setInt(1, productId);
                psStock.executeUpdate();

                PreparedStatement psProduct =
                        conn.prepareStatement(productDeleteSql);
                psProduct.setInt(1, productId);
                psProduct.executeUpdate();
            }

            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
