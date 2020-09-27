package com.kks.portfolio_android.model;

public class Alram {

    int id;
    String photo;
    String profile;
    String content;
    int comment_id;

    public Alram(){}

    public Alram(int id, String photo, String profile, String content) {
        this.id = id;
        this.photo = photo;
        this.profile = profile;
        this.content = content;
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
