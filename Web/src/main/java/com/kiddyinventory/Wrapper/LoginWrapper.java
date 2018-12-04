package com.kiddyinventory.Wrapper;

public class LoginWrapper {
    private String username;
    private String password;

    public LoginWrapper() {
    }

    public LoginWrapper(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
