package com.example.deanery.model;

public class User {

    private int id;
    private int roleId;

    public User(int id, int roleId) {
        this.id = id;
        this.roleId = roleId;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
