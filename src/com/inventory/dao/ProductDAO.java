package com.inventory.dao;

import com.inventory.db.DbConnection;
import com.inventory.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductDAO
 * ----------
 * Ürün ve stok bilgilerini veritabanından okur.
 * UI tarafına hazır Product nesneleri döndürür.
 *
 * NOT:
 * - Frontend veri üretmez
 * - String[] kullanılmaz
 * - JOIN işlemleri DAO katmanında yapılır
 */
public class ProductDAO {

    /* =====================================================
       TÜM ÜRÜNLERİ LİSTELE
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
       ÜRÜN ADINA GÖRE ARAMA
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
            WHERE LOWER(p.name) LIKE ?
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
       ÜRÜN EKLE
       products + stock tabloları birlikte kullanılır
       ===================================================== */
    public void addProduct(String name, int quantity, double price) {

        String productSql =
                "INSERT INTO products (name, unit_price) VALUES (?, ?) RETURNING product_id";
        String stockSql =
                "INSERT INTO stock (product_id, quantity) VALUES (?, ?)";

        try (Connection conn = DbConnection.getConnection()) {

            conn.setAutoCommit(false); // transaction başlat

            // 1️⃣ products tablosuna ekle
            PreparedStatement psProduct =
                    conn.prepareStatement(productSql);

            psProduct.setString(1, name);
            psProduct.setDouble(2, price);

            ResultSet rs = psProduct.executeQuery();
            rs.next();
            int productId = rs.getInt(1);

            // 2️⃣ stock tablosuna ekle
            PreparedStatement psStock =
                    conn.prepareStatement(stockSql);

            psStock.setInt(1, productId);
            psStock.setInt(2, quantity);
            psStock.executeUpdate();

            conn.commit(); // her şey OK

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

            // products
            PreparedStatement psProduct =
                    conn.prepareStatement(productSql);
            psProduct.setString(1, p.getName());
            psProduct.setDouble(2, p.getPrice());
            psProduct.setInt(3, p.getId());
            psProduct.executeUpdate();

            // stock
            PreparedStatement psStock =
                    conn.prepareStatement(stockSql);
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
       ===================================================== */
    public void deleteProduct(int productId) {

        String stockSql = "DELETE FROM stock WHERE product_id = ?";
        String productSql = "DELETE FROM products WHERE product_id = ?";

        try (Connection conn = DbConnection.getConnection()) {

            conn.setAutoCommit(false);

            PreparedStatement psStock =
                    conn.prepareStatement(stockSql);
            psStock.setInt(1, productId);
            psStock.executeUpdate();

            PreparedStatement psProduct =
                    conn.prepareStatement(productSql);
            psProduct.setInt(1, productId);
            psProduct.executeUpdate();

            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
