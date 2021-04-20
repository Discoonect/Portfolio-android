package com.kks.portfolio_android.api;

import com.kks.portfolio_android.res.AlarmRes;
import com.kks.portfolio_android.util.Util;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface AlarmApi {

    @GET(Util.ALARM_POST)
    Call<AlarmRes> postlike(@Header("Authorization") String token,
                            @Query("offset") int offset,
                            @Query("limit") int limit);

    @GET(Util.ALARM_COMMENT)
    Call<AlarmRes> comment(@Header("Authorization") String token,
                           @Query("offset") int offset,
                           @Query("limit") int limit);

    @GET(Util.ALARM_FOLLOW)
    Call<AlarmRes> follow(@Header("Authorization") String token,
                          @Query("offset") int offset,
                          @Query("limit") int limit);
}
