package com.kks.portfolio_android.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kks.portfolio_android.model.Items;

import java.util.ArrayList;
import java.util.List;

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

    @SerializedName("items")
    @Expose
    private List<Items> items = null;

    @SerializedName("user_name")
    String user_name;

    @SerializedName("user_profilephoto")
    String user_profilephoto;

    @SerializedName("introduce")
    String introduce;

    @SerializedName("follower")
    int follwer_cnt;

    // 게터 세터 만들어야 함
    public List<Items> getItems() {
        return items;
    }

    public void setItems(ArrayList<Items> items) {
        this.items = items;
    }

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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_profilephoto() {
        return user_profilephoto;
    }

    public void setUser_profilephoto(String user_profilephoto) {
        this.user_profilephoto = user_profilephoto;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getFollwer_cnt() {
        return follwer_cnt;
    }

    public void setFollwer_cnt(int follwer_cnt) {
        this.follwer_cnt = follwer_cnt;
    }
}
