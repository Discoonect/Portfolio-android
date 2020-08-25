package com.kks.portfolio_android.api;

import android.content.Context;

import com.kks.portfolio_android.util.Util;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {

    public static Retrofit retrofit;

    public static Retrofit getRetrofitClient(Context context){
        if(retrofit == null){
            OkHttpClient httpClient = new OkHttpClient.Builder().build();
            retrofit = new Retrofit.Builder().baseUrl(Util.BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
