package com.kks.portfolio_android.api;

import com.kks.portfolio_android.req.CommentReq;
import com.kks.portfolio_android.req.LikeReq;
import com.kks.portfolio_android.res.LikeRes;
import com.kks.portfolio_android.util.Util;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LikeApi {

    @GET(Util.COUNT_LIKE_POST+"/{post_id}")
    Call<LikeRes> getLikeCntData(@Path("post_id") int post_id);

    @POST(Util.LIKE_POST)
    Call<LikeRes> clickLike(@Header("Authorization") String token,
                            @Body LikeReq likeReq);

    @DELETE(Util.DELETE_LIKE_POST)
    Call<LikeRes> clickDislike(@Header("Authorization") String token,
                               @Body LikeReq likeReq);

    @GET(Util.LIKE_POST_USER+"/{post_id}")
    Call<LikeRes> likePostUser(@Path("post_id") int post_id);
}
