package com.kks.portfolio_android.model;

public class Comments {
    private int post_id;
    private int comment_id;
    private int user_id;
    private String user_profile;
    private String user_name;
    private String comment;
    private String created_at;

    public Comments(){}

    public Comments(int post_id, int comment_id, int user_id, String user_name, String comment, String created_at) {
        this.post_id = post_id;
        this.comment_id = comment_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.comment = comment;
        this.created_at = created_at;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
