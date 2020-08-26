package com.kks.portfolio_android.model;

public class Posting {
    private int id;
    private String user_name;
    private String photo_url;
    private String content;
    private String createdAt;
    private int postlike;
    private int cnt_comment;
    private int cnt_favorite;

    public Posting(int id, String user_name, String photo_url, String content, String createdAt, int postlike) {
        this.id = id;
        this.user_name = user_name;
        this.photo_url = photo_url;
        this.content = content;
        this.createdAt = createdAt;
        this.postlike = postlike;
    }

    public Posting(int id, String user_name, String content, String createdAt,String photo_url,int cnt_comment,int cnt_favorite) {
        this.id = id;
        this.user_name = user_name;
        this.content = content;
        this.createdAt = createdAt;
        this.photo_url = photo_url;
        this.cnt_comment = cnt_comment;
        this.cnt_favorite = cnt_favorite;

    }

    public Posting(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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

    public int getCnt_comment() {
        return cnt_comment;
    }

    public void setCnt_comment(int cnt_comment) {
        this.cnt_comment = cnt_comment;
    }

    public int getCnt_favorite() {
        return cnt_favorite;
    }

    public void setCnt_favorite(int cnt_favorite) {
        this.cnt_favorite = cnt_favorite;
    }
}
