package com.example.iteach.model;

public class LoginModel {
    String name, password, role, key;

    public LoginModel() {
    }

    public LoginModel(String name, String password, String role, String key) {
        this.name = name;
        this.password = password;
        this.role = role;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
