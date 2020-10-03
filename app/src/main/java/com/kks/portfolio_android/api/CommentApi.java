package com.kks.portfolio_android.api;

import com.kks.portfolio_android.req.CommentReq;
import com.kks.portfolio_android.res.CommentRes;
import com.kks.portfolio_android.util.Util;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommentApi {

    @GET(Util.GET_COMMENT+"/{post_id}")
    Call<CommentRes> getCommentData(@Path("post_id") int post_id,
                                    @Query("offset") int offset,
                                    @Query("limit") int limit);

    @POST(Util.ADD_COMMENT)
    Call<CommentRes> uploadComment(@Header("Authorization") String token,
                                   @Body CommentReq commentReq);
}
