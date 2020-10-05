package com.kks.portfolio_android.api;

import com.kks.portfolio_android.req.FollowReq;
import com.kks.portfolio_android.res.FollowRes;
import com.kks.portfolio_android.res.UserRes;
import com.kks.portfolio_android.util.Util;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FollowApi {

    @GET(Util.CHECK_FOLLOW+"/{following_id}")
    Call<FollowRes> checkFollow(@Header("Authorization") String token,
                                @Path("following_id") int following_id);

    @POST(Util.FOLLOW_USER)
    Call<FollowRes> followUser(@Header("Authorization") String token,
                               @Body FollowReq followReq);

    @DELETE(Util.CANCEL_FOLLOW)
    Call<FollowRes> unFollow(@Header("Authorization") String token,
                             @Body FollowReq followReq);

}
