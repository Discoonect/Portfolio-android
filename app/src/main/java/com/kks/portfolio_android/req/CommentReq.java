package com.kks.portfolio_android.req;

public class CommentReq {

    int post_id;
    String comment;

    public CommentReq() {
    }

    public CommentReq(int post_id, String comment) {
        this.post_id = post_id;
        this.comment = comment;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
