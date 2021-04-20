package com.kks.portfolio_android.req;

public class LikeReq {

    int post_id;

    public LikeReq() {
    }

    public LikeReq(int post_id) {
        this.post_id = post_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
}
