package com.kiddyinventory.Wrapper;

public class TokenWrapper {
    private String tokenTitle;
    private String token;

    public TokenWrapper(String tokenTitle, String token) {
        this.tokenTitle = tokenTitle;
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
