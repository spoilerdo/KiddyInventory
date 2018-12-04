package com.kiddyinventory.Entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class JwtUser extends User {
    private int userID;

    public JwtUser(int userID, String username, String password,
                      Collection<? extends GrantedAuthority> authorities) {
        super(username,password, authorities);
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }
}
