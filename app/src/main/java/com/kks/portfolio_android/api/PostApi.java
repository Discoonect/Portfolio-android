package com.kks.portfolio_android.api;


import com.kks.portfolio_android.retrofitmodel.post.PostRes;
import com.kks.portfolio_android.retrofitmodel.user.UserRes;
import com.kks.portfolio_android.util.Util;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface PostApi {

    @GET(Util.GET_ALL_POST)
    Call<PostRes> getAllPost(@Header("Authorization") String token,
                             @Query("offset") int offset,
                             @Query("limit") int limit);

    // 파일 전송을 가능하게 해준다. => @Multipart
    @Multipart
    @POST(Util.UPLOAD_POST)
    Call<UserRes> createPost(@Header("Authorization") String token,
                             @Part MultipartBody.Part file,
                             @Part("content") RequestBody requestBody);

    @GET(Util.BEST_POST)
    Call<PostRes> bestPost(@Query("offset") int offset,
                           @Query("limit") int limit);
}
