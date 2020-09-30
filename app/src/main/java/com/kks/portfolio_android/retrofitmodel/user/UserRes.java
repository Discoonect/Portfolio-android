package com.kks.portfolio_android.retrofitmodel.user;

import com.google.gson.annotations.SerializedName;

public class UserRes {
    // @SerializedName 에는, 실제로 json 으로 받아오는 키 이름을 셋팅.
    @SerializedName("success")
    boolean success;

    @SerializedName("token")
    String token;

    @SerializedName("user_id")
    int user_id;

    @SerializedName("message")
    String message;

    // 게터 세터 만들어야 함

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
