package com.kks.portfolio_android.model;

public class Posting {
    private int id;
    private int user_id;
    private String photo_url;
    private String content;
    private String createdAt;
    private int postlike;

    public Posting(int id, int user_id, String photo_url, String content, String createdAt, int postlike) {
        this.id = id;
        this.user_id = user_id;
        this.photo_url = photo_url;
        this.content = content;
        this.createdAt = createdAt;
        this.postlike = postlike;
    }

    public Posting(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getPostlike() {
        return postlike;
    }

    public void setPostlike(int postlike) {
        this.postlike = postlike;
    }
}
