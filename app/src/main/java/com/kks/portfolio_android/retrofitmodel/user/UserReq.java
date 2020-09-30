package com.kks.portfolio_android.retrofitmodel.user;

public class UserReq {
    private String user_name;
    private String user_passwd;
    private String user_phone;
    private int post_id;
    private int user_id;

    public UserReq(String user_name, String user_passwd) {
        this.user_name = user_name;
        this.user_passwd = user_passwd;
    }

    public UserReq(String user_name, String user_passwd, String user_phone) {
        this.user_name = user_name;
        this.user_passwd = user_passwd;
        this.user_phone = user_phone;
    }

    public UserReq(String user_name) {
        this.user_name = user_name;
    }

    public UserReq(){}

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_passwd() {
        return user_passwd;
    }

    public void setUser_passwd(String user_passwd) {
        this.user_passwd = user_passwd;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
