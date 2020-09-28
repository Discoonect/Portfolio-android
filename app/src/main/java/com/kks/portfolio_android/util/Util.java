package com.kks.portfolio_android.util;

public class Util {
    public static final String BASE_URL = "http://test-env.eba-gegdfakf.ap-northeast-2.elasticbeanstalk.com";
    public static final String IMAGE_PATH = BASE_URL+"/public/uploads/";
    public static final String OFFSET = "?offset=";
    public static final String LIMIT = "&limit=";
    public static final String ADD_COMMENT = BASE_URL+"/api/v1/comment/addcomment";
    public static final String COUNT_COMMENT = BASE_URL+"/api/v1/comment/countcomment/";
    public static final String GET_COMMENT = BASE_URL+"/api/v1/comment/getcomment/";
    public static final String DELETE_POSTING = BASE_URL+"/api/v1/post/deletepost/";
    public static final String LIKE_POST = BASE_URL+"/api/v1/like/likepost";
    public static final String DELETE_LIKE_POST = BASE_URL+"/api/v1/like/deletelikepost";
    public static final String COUNT_LIKE_POST = BASE_URL+"/api/v1/like/countlikepost/";
    public static final String GET_ONE_POST = BASE_URL+"/api/v1/post/getonepost/";
    public static final String CHECK_ID = BASE_URL+"/api/v1/user/checkid";
    public static final String LOGOUT = BASE_URL+"/api/v1/user/logout";
    public static final String AUTO_LOGIN_ON = "on";
    public static final String PREFERENCE_NAME = "Portfolio";
}
