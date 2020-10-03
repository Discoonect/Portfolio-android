    package com.kks.portfolio_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Items {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("post_id")
    @Expose
    private Integer post_id;

    @SerializedName("user_id")
    @Expose
    private Integer user_id;

    @SerializedName("photo_url")
    @Expose
    private String photo_url;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("user_profilephoto")
    @Expose
    private String user_profilephoto;

    @SerializedName("user_name")
    @Expose
    private String user_name;

    @SerializedName("mylike")
    @Expose
    private Integer mylike;

    @SerializedName("comment_cnt")
    @Expose
    private Integer comment_cnt;

    @SerializedName("like_cnt")
    @Expose
    private Integer like_cnt;

    @SerializedName("introduce")
    @Expose
    private String introduce;

    @SerializedName("follower")
    @Expose
    private Integer follwer_cnt;

    @SerializedName("cnt_like")
    @Expose
    private Integer cnt_like;

    @SerializedName("cnt_post")
    @Expose
    private Integer cnt_post;

    @SerializedName("following")
    @Expose
    private Integer following_cnt;

    @SerializedName("post_user_id")
    @Expose
    private Integer post_user_id;

    @SerializedName("comment_id")
    @Expose
    private Integer comment_id;

    @SerializedName("comment")
    @Expose
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getComment_id() {
        return comment_id;
    }

    public void setComment_id(Integer comment_id) {
        this.comment_id = comment_id;
    }

    public Integer getPost_user_id() {
        return post_user_id;
    }

    public void setPost_user_id(Integer post_user_id) {
        this.post_user_id = post_user_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCnt_like() {
        return cnt_like;
    }

    public void setCnt_like(Integer cnt_like) {
        this.cnt_like = cnt_like;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUser_profilephoto() {
        return user_profilephoto;
    }

    public void setUser_profilephoto(String user_profilephoto) {
        this.user_profilephoto = user_profilephoto;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Integer getMylike() {
        return mylike;
    }

    public void setMylike(Integer mylike) {
        this.mylike = mylike;
    }

    public Integer getComment_cnt() {
        return comment_cnt;
    }

    public void setComment_cnt(Integer comment_cnt) {
        this.comment_cnt = comment_cnt;
    }

    public Integer getLike_cnt() {
        return like_cnt;
    }

    public void setLike_cnt(Integer like_cnt) {
        this.like_cnt = like_cnt;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Integer getFollwer_cnt() {
        return follwer_cnt;
    }

    public void setFollwer_cnt(Integer follwer_cnt) {
        this.follwer_cnt = follwer_cnt;
    }

    public Integer getCnt_post() {
        return cnt_post;
    }

    public void setCnt_post(Integer cnt_post) {
        this.cnt_post = cnt_post;
    }

    public Integer getFollowing_cnt() {
        return following_cnt;
    }

    public void setFollowing_cnt(Integer following_cnt) {
        this.following_cnt = following_cnt;
    }


}
