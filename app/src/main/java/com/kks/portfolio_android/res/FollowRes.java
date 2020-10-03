package com.kks.portfolio_android.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowRes {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("follow")
    int follow;

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
