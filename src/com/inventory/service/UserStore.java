package com.inventory.service;

import com.inventory.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Simple in‑memory user store for the demo application.  Maintains a
 * list of users and provides methods to create, retrieve and
 * authenticate users.  A default admin and a default user are
 * inserted on class load.  Authentication is by email address.
 */
public class UserStore {
    private static final List<User> users = new ArrayList<>();
    private static int nextId = 3;

    static {
        // Default admin credentials; admin logs in via admin@gmail.com / 123456
        users.add(new User(1, "Admin", "admin@gmail.com", "123456", 1));
        // Default regular user for demonstration
        users.add(new User(2, "Kullanıcı", "user@example.com", "1234", 2));
    }

    public static Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public static Optional<User> authenticate(String email, String password) {
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password))
                .findFirst();
    }

    public static User addUser(String name, String email, String password, int roleId) {
        User user = new User(nextId++, name, email, password, roleId);
        users.add(user);
        return user;
    }

    public static boolean deleteUser(int userId) {
        return users.removeIf(u -> u.getId() == userId);
    }

    public static List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}