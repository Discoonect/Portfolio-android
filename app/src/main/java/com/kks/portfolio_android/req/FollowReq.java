package com.kks.portfolio_android.req;

public class FollowReq {

    int following_id;

    public FollowReq(){}

    public FollowReq(int following_id) {
        this.following_id = following_id;
    }

    public int getFollowing_id() {
        return following_id;
    }

    public void setFollowing_id(int following_id) {
        this.following_id = following_id;
    }
}
