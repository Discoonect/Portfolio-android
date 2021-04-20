package com.kks.portfolio_android.req;

public class PostReq {

    int post_id;
    String content;

    public PostReq(String content) {
        this.content = content;
    }

    public PostReq(int post_id) {
        this.post_id = post_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
}
