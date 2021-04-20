package com.kks.portfolio_android.model;

public class User {

    private int user_id;
    private String user_name;
    private String user_profile;
    private String user_introduce;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
