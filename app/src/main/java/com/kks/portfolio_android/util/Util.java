package com.kks.portfolio_android.util;

public class Util {
    public static final String BASE_URL = "http://test-env.eba-gegdfakf.ap-northeast-2.elasticbeanstalk.com";
    public static final String IMAGE_PATH = BASE_URL+"/public/uploads/";
    public static final String OFFSET = "?offset=";
    public static final String LIMIT = "&limit=";
    public static final String AUTO_LOGIN_ON = "on";
    public static final String AUTO_LOGIN_OFF = "off";
    public static final String PREFERENCE_NAME = "Portfolio";

    public static final String LOGIN = "/api/v1/user/login";
    public static final String SIGN_UP = "/api/v1/user/signup";
    public static final String CHECK_ID = "/api/v1/user/checkid";
    public static final String LOGOUT = BASE_URL+"/api/v1/user/logout";
    public static final String PROFILE_PHOTO = "/api/v1/user/profilephoto";
    public static final String USER_PAGE1 = "/api/v1/user/userpage";
    public static final String USER_PAGE2 = "/api/v1/user/userpage2";

    public static final String GET_ALL_POST = "/api/v1/post/getallpost";
    public static final String UPLOAD_POST = "/api/v1/post/uploadpost";
    public static final String DELETE_POSTING = BASE_URL+"/api/v1/post/deletepost/";
    public static final String GET_ONE_POST = BASE_URL+"/api/v1/post/getonepost/";
    public static final String BEST_POST = "/api/v1/post/bestpost";
    public static final String GET_PAGE_PHOTO = "/api/v1/post/getpostphotourl";

    public static final String ADD_COMMENT = "/api/v1/comment/addcomment";
    public static final String COUNT_COMMENT = BASE_URL+"/api/v1/comment/countcomment/";
    public static final String GET_COMMENT = "/api/v1/comment/getcomment";
    public static final String DELETE_COMMENT = BASE_URL+"/api/v1/comment/deletecomment";

    public static final String LIKE_POST = "/api/v1/like/likepost";
    public static final String DELETE_LIKE_POST = "/api/v1/like/deletelikepost";
    public static final String COUNT_LIKE_POST = "/api/v1/like/countlikepost";

    public static final String CHECK_FOLLOW = "/api/v1/follow/checkfollow";
    public static final String FOLLOW_USER = "/api/v1/follow/following";
    public static final String CANCEL_FOLLOW = "/api/v1/follow/deletefollow";



}
