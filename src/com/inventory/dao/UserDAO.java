package com.inventory.dao;

import com.inventory.model.User;
import com.inventory.service.UserStore;

import java.util.List;
import java.util.Optional;

/**
 * DAO layer for users.  Delegates to the in‑memory {@link
 * UserStore}.  Provides methods for authentication and user
 * management.  When integrating with PostgreSQL you can replace the
 * calls to UserStore with JDBC logic.
 */
public class UserDAO {
    /**
     * Authenticates a user by email and password.
     *
     * @param email    the user's email
     * @param password the entered password
     * @return the matching user or {@code null} if none found
     */
    public User authenticate(String email, String password) {
        Optional<User> opt = UserStore.authenticate(email, password);
        return opt.orElse(null);
    }

    /**
     * Registers a new user.  Returns null if the email is already in
     * use.
     */
    public User register(String name, String email, String password, int roleId) {
        if (UserStore.findByEmail(email).isPresent()) return null;
        return UserStore.addUser(name, email, password, roleId);
    }

    public List<User> getAllUsers() {
        return UserStore.getAllUsers();
    }

    public boolean deleteUser(int userId) {
        return UserStore.deleteUser(userId);
    }
}