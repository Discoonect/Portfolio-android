package com.kks.portfolio_android.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.activity.HomeActivity;
import com.kks.portfolio_android.activity.PostingActivity;
import com.kks.portfolio_android.adapter.Adapter_comment;
import com.kks.portfolio_android.adapter.Adapter_home;
import com.kks.portfolio_android.adapter.Adapter_search;
import com.kks.portfolio_android.adapter.Adapter_user;
import com.kks.portfolio_android.model.Items;
import com.kks.portfolio_android.req.CommentReq;
import com.kks.portfolio_android.req.FollowReq;
import com.kks.portfolio_android.req.LikeReq;
import com.kks.portfolio_android.res.CommentRes;
import com.kks.portfolio_android.res.FollowRes;
import com.kks.portfolio_android.res.LikeRes;
import com.kks.portfolio_android.res.PostRes;
import com.kks.portfolio_android.req.UserReq;
import com.kks.portfolio_android.res.UserRes;
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
    public void login(Context context, String name, String passwd, CheckBox auto_login_check){
        UserReq userReq = new UserReq(name, passwd);

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
        UserReq userReq = new UserReq(name,passwd,phone );

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
        UserReq userReq = new UserReq(name,passwd,phone );

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
                if(response.isSuccessful()) {
                    List<Items> itemsList;
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

    public void getUserPage1(Context context, int user_id, ImageView page_img_profile,
                             TextView page_txt_userName,TextView page_txt_followerCnt,
                             TextView page_txt_introduce){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        UserApi userApi = retrofit.create(UserApi.class);

        Call<UserRes> userResCall = userApi.userPage1(user_id);

        userResCall.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if(response.isSuccessful()) {
                    String user_name = response.body().getItems().get(0).getUser_name();
                    String profile = response.body().getItems().get(0).getUser_profilephoto();
                    String profileUrl = Util.IMAGE_PATH + profile;
                    String introduce = response.body().getItems().get(0).getIntroduce();
                    int follower_cnt = response.body().getItems().get(0).getFollwer_cnt();

                    if (introduce==null) {
                        introduce = "";
                    }

                    if (profile!=null) {
                        Glide.with(context).load(profileUrl).into(page_img_profile);
                    } else {
                        page_img_profile.setImageResource(R.drawable.ic_baseline_account_circle_24);
                    }
                    page_txt_userName.setText(user_name);
                    page_txt_followerCnt.setText("" + follower_cnt);
                    page_txt_introduce.setText(introduce);
                }
            }
            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {
            }
        });

    }

    public void getUserPage2(Context context,int user_id,TextView page_txt_postingCnt,
                             TextView page_txt_followingCnt){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        UserApi userApi = retrofit.create(UserApi.class);

        Call<UserRes> userResCall = userApi.userPage2(user_id);

        userResCall.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if(response.isSuccessful()) {
                    int cnt_post = response.body().getItems().get(0).getCnt_post();
                    int following_cnt = response.body().getItems().get(0).getFollowing_cnt();

                    page_txt_postingCnt.setText(""+cnt_post);
                    page_txt_followingCnt.setText(""+following_cnt);
                }
            }
            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {
            }
        });
    }

    public void getPagePhoto(Context context,int user_id,int offset,int limit,RecyclerView recyclerView){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        UserApi userApi = retrofit.create(UserApi.class);

        Call<UserRes> userResCall = userApi.getPagePhoto(user_id,offset,limit);

        userResCall.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {

                if(response.isSuccessful()) {
                    List<Items> itemsList = response.body().getItems();
                    Adapter_user adapter_user = new Adapter_user(context,itemsList);
                    recyclerView.setAdapter(adapter_user);
                }
            }
            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {
            }
        });
    }

    public void checkFollow(Context context, String token, int following_id,
                            int user_id, Button page_btn_follow,Button page_btn_unFollow){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        FollowApi followApi = retrofit.create(FollowApi.class);

        Call<FollowRes> followResCall = followApi.checkFollow("Bearer " + token,following_id);

        followResCall.enqueue(new Callback<FollowRes>() {
            @Override
            public void onResponse(Call<FollowRes> call, Response<FollowRes> response) {
                if(response.isSuccessful()) {
                    int follow = response.body().getFollow();

                    if(user_id==following_id){
                        page_btn_follow.setVisibility(View.GONE);
                        page_btn_unFollow.setVisibility(View.GONE);
                    }else if(follow == 0){
                        page_btn_follow.setVisibility(View.VISIBLE);
                    }else if(follow==1){
                        page_btn_unFollow.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<FollowRes> call, Throwable t) {

            }
        });
    }

    public void followUser(Context context, int follow_id,String token,
                           Button page_btn_follow,Button page_btn_unFollow,TextView page_txt_followerCnt){
        FollowReq followReq = new FollowReq(follow_id);

        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        FollowApi followApi = retrofit.create(FollowApi.class);

        Call<FollowRes> followResCall = followApi.followUser("Bearer " + token,followReq);

        followResCall.enqueue(new Callback<FollowRes>() {
            @Override
            public void onResponse(Call<FollowRes> call, Response<FollowRes> response) {
                if(response.isSuccessful()) {
                    String message = response.body().getMessage();

                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    page_btn_follow.setVisibility(View.INVISIBLE);
                    page_btn_unFollow.setVisibility(View.VISIBLE);

                    int follower_cnt = Integer.parseInt(page_txt_followerCnt.getText().toString().trim())+1;

                    page_txt_followerCnt.setText(""+follower_cnt);
                }
            }

            @Override
            public void onFailure(Call<FollowRes> call, Throwable t) {

            }
        });
    }

    public void cancelFollow(Context context, int follow_id,String token,
                             Button page_btn_follow,Button page_btn_unFollow,TextView page_txt_followerCnt){
        FollowReq followReq = new FollowReq(follow_id);

        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        FollowApi followApi = retrofit.create(FollowApi.class);

        Call<FollowRes> followResCall = followApi.unFollow("Bearer " + token,followReq);

        followResCall.enqueue(new Callback<FollowRes>() {
            @Override
            public void onResponse(Call<FollowRes> call, Response<FollowRes> response) {
                if(response.isSuccessful()) {
                    String message = response.body().getMessage();

                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    page_btn_unFollow.setVisibility(View.INVISIBLE);
                    page_btn_follow.setVisibility(View.VISIBLE);

                    int follower_cnt = Integer.parseInt(page_txt_followerCnt.getText().toString().trim())-1;

                    page_txt_followerCnt.setText(""+follower_cnt);
                }
            }
            @Override
            public void onFailure(Call<FollowRes> call, Throwable t) {
            }
        });
    }

    public void getCommentData(Context context,int post_id,int offset,int limit,RecyclerView recyclerView){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        CommentApi commentApi = retrofit.create(CommentApi.class);

        Call<CommentRes> commentResCall = commentApi.getCommentData(post_id,offset,limit);

        Log.i("aaa","1");

        commentResCall.enqueue(new Callback<CommentRes>() {
            @Override
            public void onResponse(Call<CommentRes> call, Response<CommentRes> response) {
                List<Items> itemsList = new ArrayList<>();
                itemsList = response.body().getItems();
                Log.i("aaa",response.body().getItems().get(0).getUser_name());
                Adapter_comment adapter_comment = new Adapter_comment(context,itemsList);
                recyclerView.setAdapter(adapter_comment);

            }

            @Override
            public void onFailure(Call<CommentRes> call, Throwable t) {

            }
        });

    }

    public void uploadComment(Context context,int post_id,String comment,String token){
        CommentReq commentReq = new CommentReq(post_id,comment);
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        CommentApi commentApi =retrofit.create(CommentApi.class);

        Call<CommentRes> commentResCall = commentApi.uploadComment(token,commentReq);

        commentResCall.enqueue(new Callback<CommentRes>() {
            @Override
            public void onResponse(Call<CommentRes> call, Response<CommentRes> response) {
                Toast.makeText(context, R.string.comment_complete, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CommentRes> call, Throwable t) {

            }
        });

    }

    public void getLikeCntData(Context context,int post_id,TextView po_txt_cntFavorite){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        LikeApi likeApi = retrofit.create(LikeApi.class);

        Call<LikeRes> likeResCall = likeApi.getLikeCntData(post_id);

        likeResCall.enqueue(new Callback<LikeRes>() {
            @Override
            public void onResponse(Call<LikeRes> call, Response<LikeRes> response) {
                int likecnt = response.body().getCnt();
                Log.i("aaa",""+likecnt);
                Log.i("aaa",response.toString());
                String text = context.getString(R.string.how_many_like);
                po_txt_cntFavorite.setText(String.format(text,likecnt));
            }

            @Override
            public void onFailure(Call<LikeRes> call, Throwable t) {

            }
        });
    }

    public void clickLike(Context context,int post_id,String token,TextView po_txt_cntFavorite){
        LikeReq likeReq = new LikeReq(post_id);
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        LikeApi likeApi = retrofit.create(LikeApi.class);

        Call<LikeRes> likeResCall = likeApi.clickLike(token,likeReq);

        likeResCall.enqueue(new Callback<LikeRes>() {
            @Override
            public void onResponse(Call<LikeRes> call, Response<LikeRes> response) {
                getLikeCntData(context,post_id,po_txt_cntFavorite);
            }

            @Override
            public void onFailure(Call<LikeRes> call, Throwable t) {

            }
        });
    }

    public void clickDislike(Context context,int post_id,String token,TextView po_txt_cntFavorite){
        LikeReq likeReq = new LikeReq(post_id);
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        LikeApi likeApi = retrofit.create(LikeApi.class);

        Call<LikeRes> likeResCall = likeApi.clickDislike(token,likeReq);

        likeResCall.enqueue(new Callback<LikeRes>() {
            @Override
            public void onResponse(Call<LikeRes> call, Response<LikeRes> response) {
                getLikeCntData(context,post_id,po_txt_cntFavorite);
            }

            @Override
            public void onFailure(Call<LikeRes> call, Throwable t) {

            }
        });
    }

}
