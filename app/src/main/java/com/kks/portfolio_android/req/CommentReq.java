package com.kks.portfolio_android.req;

public class CommentReq {

    int post_id;
    String comment;
    int comment_id;

    public CommentReq() {
    }

    public CommentReq(int post_id, String comment) {
        this.post_id = post_id;
        this.comment = comment;
    }

    public CommentReq(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
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
