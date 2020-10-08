package com.kks.portfolio_android.util;

public class Util {
    public static final String BASE_URL = "http://test-env.eba-gegdfakf.ap-northeast-2.elasticbeanstalk.com";
    public static final String IMAGE_PATH = "https://wassup-sns.s3.ap-northeast-2.amazonaws.com/";
    public static final String OFFSET = "?offset=";
    public static final String LIMIT = "&limit=";
    public static final String AUTO_LOGIN_ON = "on";
    public static final String AUTO_LOGIN_OFF = "off";
    public static final String PREFERENCE_NAME = "Portfolio";

    public static final String LOGIN = "/api/v1/user/login";
    public static final String SIGN_UP = "/api/v1/user/signup";
    public static final String CHECK_ID = "/api/v1/user/checkid";
    public static final String LOGOUT = "/api/v1/user/logout";
    public static final String PROFILE_PHOTO = "/api/v1/user/profilephoto";
    public static final String USER_PAGE1 = "/api/v1/user/userpage";
    public static final String USER_PAGE2 = "/api/v1/user/userpage2";
    public static final String MY_PAGE1 = "/api/v1/user/mypage";
    public static final String MY_PAGE2 = "/api/v1/user/mypage2";
    public static final String ADIOS = "/api/v1/user/adios";
    public static final String WRITE_INTRODUCE = "/api/v1/user/myintroduce";
    public static final String CHANGE_BASIC_PROFILE = "/api/v1/user/profilephoto";

    public static final String GET_ALL_POST = "/api/v1/post/all";
    public static final String UPLOAD_POST = "/api/v1/post/upload";
    public static final String DELETE_POSTING = "/api/v1/post";
    public static final String GET_ONE_POST = "/api/v1/post";
    public static final String BEST_POST = "/api/v1/post/best";
    public static final String GET_PAGE_PHOTO = "/api/v1/post/photourl";
    public static final String UPDATE_POST = "/api/v1/post";

    public static final String ADD_COMMENT = "/api/v1/comment";
    public static final String GET_COMMENT = "/api/v1/comment";
    public static final String DELETE_COMMENT = "/api/v1/comment";

    public static final String LIKE_POST = "/api/v1/like/post";
    public static final String DELETE_LIKE_POST = "/api/v1/like/post";
    public static final String COUNT_LIKE_POST = "/api/v1/like/count";
    public static final String LIKE_POST_USER = "/api/v1/like/user";

    public static final String CHECK_FOLLOW = "/api/v1/follow/check";
    public static final String FOLLOW_USER = "/api/v1/follow/";
    public static final String CANCEL_FOLLOW = "/api/v1/follow/";



}
