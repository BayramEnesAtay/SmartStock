package com.inventory.dao;

import com.inventory.db.DbConnection;
import com.inventory.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /* =====================================================
       LOGIN
       ===================================================== */
    public User authenticate(String email, String password) {

        String sql = """
            SELECT user_id, name, email, role_id
            FROM users
            WHERE email = ?
              AND password = ?
        """;

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String role = rs.getInt("role_id") == 1 ? "ADMIN" : "USER";

                return new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        role
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // login başarısız
    }

    /* =====================================================
       REGISTER (SIGN UP) → SADECE USER
       ===================================================== */
    public boolean register(String name, String email, String password) {

        String checkSql = "SELECT COUNT(*) FROM users WHERE email = ?";
        String insertSql = """
            INSERT INTO users (name, email, password, role_id)
            VALUES (?, ?, ?, 2)
        """; // role_id = 2 → USER

        try (Connection conn = DbConnection.getConnection()) {

            // email kontrol
            PreparedStatement psCheck = conn.prepareStatement(checkSql);
            psCheck.setString(1, email);
            ResultSet rs = psCheck.executeQuery();
            rs.next();

            if (rs.getInt(1) > 0) {
                return false; // email zaten var
            }

            // kullanıcı ekle
            PreparedStatement psInsert = conn.prepareStatement(insertSql);
            psInsert.setString(1, name);
            psInsert.setString(2, email);
            psInsert.setString(3, password);
            psInsert.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /* =====================================================
       ADMIN → USER EKLE (ADMIN EKLEYEMEZ)
       ===================================================== */
    public boolean addUserByAdmin(String name, String email, String password) {
        // admin paneli de sadece USER ekleyebilir
        return register(name, email, password);
    }

    /* =====================================================
       TÜM KULLANICILARI LİSTELE
       ===================================================== */
    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        String sql = """
            SELECT user_id, name, email, role_id
            FROM users
            ORDER BY user_id
        """;

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String role = rs.getInt("role_id") == 1 ? "ADMIN" : "USER";

                users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        role
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    /* =====================================================
       USER SİL (ADMIN PANELİ)
       ===================================================== */
    public void deleteUser(int userId) {

        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
