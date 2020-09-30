package com.kks.portfolio_android.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.kks.portfolio_android.R;
import com.kks.portfolio_android.activity.HomeActivity;
import com.kks.portfolio_android.adapter.Adapter_home;
import com.kks.portfolio_android.adapter.Adapter_search;
import com.kks.portfolio_android.retrofitmodel.Items;
import com.kks.portfolio_android.retrofitmodel.post.PostRes;
import com.kks.portfolio_android.retrofitmodel.user.UserReq;
import com.kks.portfolio_android.retrofitmodel.user.UserRes;
import com.kks.portfolio_android.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class RetrofitApi {

    UserReq userReq = new UserReq();

    public void login(Context context, String name, String passwd, CheckBox auto_login_check){
        userReq = new UserReq(name, passwd);

        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        UserApi userApi = retrofit.create(UserApi.class);

        Call<UserRes> userResCall = userApi.login(userReq);

        userResCall.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if(response.isSuccessful()) {
                    Log.i("aaa",response.toString());
                    String token = response.body().getToken();
                    int user_id = response.body().getUser_id();

                    SharedPreferences sp = context.getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("token",token);
                    editor.putInt("user_id",user_id);

                    editor.apply();

                    if(auto_login_check.isChecked()==true){
                        editor.putString("auto",Util.AUTO_LOGIN_ON);
                        editor.apply();
                    }else{
                        editor.putString("auto",Util.AUTO_LOGIN_OFF);
                        editor.apply();
                    }
                    Intent i = new Intent(context, HomeActivity.class);
                    context.startActivity(i);
                    ((Activity)context).finish();
                }
            }

            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {
                Log.i("aaa",t.toString());

            }
        });
    }

    public void signUp(Context context,String name, String passwd, String phone){
        userReq = new UserReq(name,passwd,phone );

        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        UserApi userApi = retrofit.create(UserApi.class);

        Call<UserRes> userResCall = userApi.signUp(userReq);
        userResCall.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(context, R.string.sign_up_complete, Toast.LENGTH_SHORT).show();
                    ((Activity)context).finish();
                }
            }

            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {
                Log.i("aaa",t.toString());
            }
        });
    }

    public void signUpWithProfile(Context context, String name, String passwd, String phone, File photoFile){
        userReq = new UserReq(name,passwd,phone );

        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        UserApi userApi = retrofit.create(UserApi.class);

        Call<UserRes> userResCall = userApi.signUp(userReq);
        userResCall.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if(response.isSuccessful()) {
                    String token = response.body().getToken();

                    Retrofit retrofit = NetworkClient.getRetrofitClient(context);
                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), photoFile);
                    MultipartBody.Part part = MultipartBody.Part.createFormData("photo", photoFile.getName(), fileBody);
                    UserApi userApi = retrofit.create(UserApi.class);

                    Call<UserRes> call1 = userApi.uploadProfile("Bearer " + token, part);
                    call1.enqueue(new Callback<UserRes>() {
                        @Override
                        public void onResponse(Call<UserRes> call, retrofit2.Response<UserRes> response) {
                            Toast.makeText(context, R.string.sign_up_complete, Toast.LENGTH_SHORT).show();
                            ((Activity)context).finish();
                        }
                        @Override
                        public void onFailure(Call<UserRes> call, Throwable t) {
                            Log.i("aaa", t.toString());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {
                Log.i("aaa",t.toString());
            }
        });
    }

    public void getPostingData(Context context, String token, RecyclerView recyclerView){

        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        PostApi postApi = retrofit.create(PostApi.class);

        Call<PostRes> postResCall = postApi.getAllPost("Bearer "+ token,0,25);

        postResCall.enqueue(new Callback<PostRes>() {
            @Override
            public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                Log.i("aaa",response.toString());
                if(response.isSuccessful()) {
                    List<Items> itemsList = new ArrayList<>();
                    itemsList = response.body().getItems();
                    Adapter_home adapter_home = new Adapter_home(context, itemsList);
                    recyclerView.setAdapter(adapter_home);
                }
            }
            @Override
            public void onFailure(Call<PostRes> call, Throwable t) {

            }
        });
    }

    public void getBestPost(Context context,int offset, int limit,RecyclerView recyclerView){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        PostApi postApi = retrofit.create(PostApi.class);

        Call<PostRes> postResCall = postApi.bestPost(offset,limit);

        postResCall.enqueue(new Callback<PostRes>() {
            @Override
            public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                Log.i("aaa",response.toString());
                if(response.isSuccessful()) {
                    List<Items> itemsList = new ArrayList<>();
                    itemsList = response.body().getItems();
                    Adapter_search adapter_search = new Adapter_search(context,itemsList);
                    recyclerView.setAdapter(adapter_search);
                }
            }

            @Override
            public void onFailure(Call<PostRes> call, Throwable t) {

            }
        });
    }

    public void getUserPage1(Context context){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        UserApi userApi = retrofit.create(UserApi.class);

    }
}
