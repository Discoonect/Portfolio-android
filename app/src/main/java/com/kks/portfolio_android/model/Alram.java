package com.kks.portfolio_android.model;

public class Alram {

    int id;
    int user_id;
    String photo;
    String profile;
    String content;
    int comment_id;
    int status;
    int position;

    public Alram(){}

    public Alram(int user_id,int id, String photo, String profile, String content,int status) {
        this.user_id = user_id;
        this.id = id;
        this.photo = photo;
        this.profile = profile;
        this.content = content;
        this.status = status;
    }

    public Alram(int user_id,int id, String profile, String content,int status) {
        this.user_id = user_id;
        this.id = id;
        this.profile = profile;
        this.content = content;
        this.status = status;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }
}
