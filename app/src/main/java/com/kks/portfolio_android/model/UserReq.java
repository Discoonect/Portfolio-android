package com.kks.portfolio_android.model;

public class UserReq {
    private String name;
    private String passwd;

    public UserReq(String name, String passwd) {
        this.name = name;
        this.passwd = passwd;
    }
    public UserReq(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
