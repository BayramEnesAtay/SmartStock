package com.inventory.model;

/**
 * Represents a user of the inventory system.  In addition to the
 * username and password this version stores the person's full name,
 * email address and role.  Role id 1 indicates an administrator and
 * 2 indicates a regular user.  In a real system passwords should
 * never be stored in plain text.
 */
public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private int roleId;

    public User(int id, String name, String email, String password, int roleId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}