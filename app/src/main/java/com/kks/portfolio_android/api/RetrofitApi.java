package com.kks.portfolio_android.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.activity.HomeActivity;
import com.kks.portfolio_android.activity.MainActivity;
import com.kks.portfolio_android.activity.SettingActivity;
import com.kks.portfolio_android.adapter.Adapter_comment;
import com.kks.portfolio_android.adapter.Adapter_home;
import com.kks.portfolio_android.adapter.Adapter_plu;
import com.kks.portfolio_android.adapter.Adapter_search;
import com.kks.portfolio_android.adapter.Adapter_user;
import com.kks.portfolio_android.model.Alram;
import com.kks.portfolio_android.model.Items;
import com.kks.portfolio_android.req.CommentReq;
import com.kks.portfolio_android.req.FollowReq;
import com.kks.portfolio_android.req.LikeReq;
import com.kks.portfolio_android.req.PostReq;
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
//                    Toast.makeText(context, R.string.sign_up_complete, Toast.LENGTH_SHORT).show();
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
//                            Toast.makeText(context, R.string.sign_up_complete, Toast.LENGTH_SHORT).show();
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

    public void getPostingData(Context context, String token,int offset,int limit,RecyclerView recyclerView){

        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        PostApi postApi = retrofit.create(PostApi.class);

        Call<PostRes> postResCall = postApi.getAllPost(token,offset,limit);

        postResCall.enqueue(new Callback<PostRes>() {
            @Override
            public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                if(response.code()==200) {
                    Log.i("aaa",response.toString());
                    Log.i("aaa","cnt : "+response.body().getCnt());
                    List<Items> itemsList;
                    itemsList = response.body().getItems();
                    Adapter_home adapter_home = new Adapter_home(context,itemsList);
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
                             TextView txt_introduce){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        UserApi userApi = retrofit.create(UserApi.class);

        Call<UserRes> userResCall = userApi.userPage1(user_id);

        userResCall.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                Log.i("aaa",response.toString());
                if(response.isSuccessful()) {
                    Items items = response.body().getItems().get(0);


                    String user_name = items.getUser_name();
                    String profile = items.getUser_profilephoto();
                    String introduce = items.getIntroduce();

                    if(page_txt_followerCnt!=null){
                        int follower_cnt = items.getFollwer_cnt();
                        page_txt_followerCnt.setText("" + follower_cnt);
                    }

                    if(items.getIntroduce()==null || items.getIntroduce().equals("")){
                        txt_introduce.setText("");
                    }else{
                        Log.i("aaa",introduce);
                        txt_introduce.setText(introduce);
                    }

                    if (profile!=null) {
                        Glide.with(context).load(Util.IMAGE_PATH+profile).into(page_img_profile);
                    } else {
                        page_img_profile.setImageResource(R.drawable.ic_baseline_account_circle_24);
                    }

                    page_txt_userName.setText(user_name);
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

    public void getCommentData(Context context,int post_id,int offset,int limit,RecyclerView recyclerView,Adapter_comment adapter_comment){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        CommentApi commentApi = retrofit.create(CommentApi.class);

        Call<CommentRes> commentResCall = commentApi.getCommentData(post_id,offset,limit);

        commentResCall.enqueue(new Callback<CommentRes>() {
            @Override
            public void onResponse(Call<CommentRes> call, Response<CommentRes> response) {
                List<Items> itemsList;
                itemsList = response.body().getItems();
                adapter_comment.setItemsList(itemsList);
                recyclerView.setAdapter(adapter_comment);
            }

            @Override
            public void onFailure(Call<CommentRes> call, Throwable t) {

            }
        });

    }

    public void uploadComment(Context context,int post_id,String comment,String token,EditText cm_edit_comment,
                              Adapter_comment adapter_comment,int offset, int limit,RecyclerView recyclerView){
        CommentReq commentReq = new CommentReq(post_id,comment);

        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        CommentApi commentApi =retrofit.create(CommentApi.class);

        Call<CommentRes> commentResCall = commentApi.uploadComment(token,commentReq);

        commentResCall.enqueue(new Callback<CommentRes>() {
            @Override
            public void onResponse(Call<CommentRes> call, Response<CommentRes> response) {
                cm_edit_comment.setText(R.string.text_clear);
                getCommentData(context,post_id,offset,limit,recyclerView,adapter_comment);
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
                Log.i("aaa",response.toString());
                getLikeCntData(context,post_id,po_txt_cntFavorite);
            }

            @Override
            public void onFailure(Call<LikeRes> call, Throwable t) {

            }
        });
    }

    public void getMyPage1(Context context,String token,TextView fu_txt_introduce,ImageView fu_img_profile,
                           TextView fu_txt_userId,TextView fu_txt_followerCnt){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        UserApi userApi = retrofit.create(UserApi.class);

        Call<UserRes> userResCall = userApi.getMyPage1(token);

        userResCall.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if(response.code()==200){
                    Items items = response.body().getItems().get(0);
                    String user_name = items.getUser_name();
                    String profile = items.getUser_profilephoto();

                    String introduce = items.getIntroduce();
                    int follower_cnt = items.getFollwer_cnt();

                    if (introduce==null) {
                        fu_txt_introduce.setText("");
                    }else{
                        fu_txt_introduce.setText(introduce);
                    }

                    if (profile!=null) {
                        Glide.with(context).load(Util.IMAGE_PATH+profile).into(fu_img_profile);
                    } else {
                        fu_img_profile.setImageResource(R.drawable.ic_baseline_account_circle_24);
                    }
                    fu_txt_userId.setText(user_name);
                    fu_txt_followerCnt.setText("" + follower_cnt);

                }else{
                    Log.i("aaa",response.toString());
                }
            }
            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {
                Log.i("aaa",t.toString());
            }
        });
    }

    public void getMyPage2(Context context,String token,TextView fu_txt_postingCnt,TextView fu_txt_followingCnt){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        UserApi userApi = retrofit.create(UserApi.class);

        Call<UserRes> userResCall = userApi.getMyPage2(token);

        userResCall.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if(response.code()==200){
                    Items items = response.body().getItems().get(0);

                    int posting_cnt = items.getCnt_post();
                    int following_cnt = items.getFollowing_cnt();

                    fu_txt_postingCnt.setText(""+posting_cnt);
                    fu_txt_followingCnt.setText(""+following_cnt);
                }else{
                    Log.i("aaa",response.toString());
                }
            }

            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {

            }
        });
    }

    public void deletePost(Context context,String token,int post_id){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        PostApi postApi = retrofit.create(PostApi.class);

        Call<PostRes> postResCall = postApi.deletePost(token,post_id);

        postResCall.enqueue(new Callback<PostRes>() {
            @Override
            public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                if(response.code()==200) {
                    Toast.makeText(context, R.string.delete_posting_complete, Toast.LENGTH_SHORT).show();
                    ((Activity) context).finish();
                }else{
                    Log.i("aaa", response.toString());
                }
            }

            @Override
            public void onFailure(Call<PostRes> call, Throwable t) {
                Log.i("aaa",t.toString());
            }
        });
    }

    public void likePostUser(Context context,int post_id,RecyclerView recyclerView){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        LikeApi likeApi = retrofit.create(LikeApi.class);

        Call<LikeRes> likeResCall = likeApi.likePostUser(post_id);

        likeResCall.enqueue(new Callback<LikeRes>() {
            @Override
            public void onResponse(Call<LikeRes> call, Response<LikeRes> response) {
                if(response.code()==200){
                    List<Items> itemsList = response.body().getItems();
                    Adapter_plu adapter_plu = new Adapter_plu(context,itemsList);
                    recyclerView.setAdapter(adapter_plu);
                }else{
                    Log.i("aaa",response.toString());
                }
            }

            @Override
            public void onFailure(Call<LikeRes> call, Throwable t) {

            }
        });
    }

    public void userAdios(Context context,String token){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        UserApi userApi = retrofit.create(UserApi.class);

        Call<UserRes> userResCall = userApi.userAdios(token);

        userResCall.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if(response.code()==200){
                    SharedPreferences sharedPreferences = context.getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    Intent i = new Intent(context, MainActivity.class);
                    context.startActivity(i);
                    ((Activity)context).finish();

                }else{
                    Log.i("aaa",response.toString());
                }
            }

            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {

            }
        });
    }

    public void userLogout(Context context,Activity activity,String token){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        UserApi userApi = retrofit.create(UserApi.class);

        Call<UserRes> userResCall = userApi.userLogout(token);

        userResCall.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if(response.code()==200) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    SettingActivity.alertDialog(context, activity, R.string.logout_complete, R.string.move_to_login);
                }
            }

            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {

            }
        });
    }

    public void uploadProfile(Context context,File photoFile,String token,String introduce){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), photoFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("photo",
                photoFile.getName(), fileBody);

        UserApi userApi = retrofit.create(UserApi.class);
        Call<UserRes> call = userApi.uploadProfile(token, part);
        call.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, retrofit2.Response<UserRes> response) {
                if(response.code()==200){
                    writeIntroduce(context,token,introduce);
                }else{
                    Log.i("aaa",response.toString());
                }
            }
            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {
                Log.i("aaa", t.toString());
            }
        });
    }

    public void writeIntroduce(Context context,String token,String introduce){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        UserApi userApi = retrofit.create(UserApi.class);
        UserReq userReq = new UserReq();
        userReq.setIntroduce(introduce);

        Call<UserRes> userResCall = userApi.writeIntroduce(token,userReq);

        userResCall.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if(response.code()==200) {
                    Toast.makeText(context, R.string.setting_complete, Toast.LENGTH_SHORT).show();
                    ((Activity) context).finish();
                }else{
                    Log.i("aaa",response.toString());
                }
            }

            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {

            }
        });
    }

    public void deleteProfilePhoto(Context context, String token, int user_id, ImageView setting_img_profile,
                                   TextView setting_txt_userName, EditText setting_edit_introduce){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        UserApi userApi = retrofit.create(UserApi.class);
        Call<UserRes> userResCall = userApi.deleteProfilePhoto(token);

        userResCall.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                Toast.makeText(context, R.string.change_basic_image_complete, Toast.LENGTH_SHORT).show();
                getUserPage1(context,user_id,setting_img_profile,setting_txt_userName,null,setting_edit_introduce);
            }

            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {

            }
        });
    }

    public void deleteComment(Context context, String token,int comment_id){
        CommentReq commentReq = new CommentReq(comment_id);

        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        CommentApi commentApi = retrofit.create(CommentApi.class);

        Call<CommentRes> commentResCall = commentApi.deleteComment(token,commentReq);

        commentResCall.enqueue(new Callback<CommentRes>() {
            @Override
            public void onResponse(Call<CommentRes> call, Response<CommentRes> response) {
                if(response.code()==200){

                    Toast.makeText(context, R.string.delete_comment_complete, Toast.LENGTH_SHORT).show();
                }else{
                    Log.i("aaa",response.toString());
                }
            }

            @Override
            public void onFailure(Call<CommentRes> call, Throwable t) {

            }
        });

    }

    public void updatePost(Context context,String token,int post_id,String content){
        PostReq postReq = new PostReq(content);

        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        PostApi postApi = retrofit.create(PostApi.class);

        Call<PostRes> postResCall = postApi.updatePost(token,post_id,postReq);

        postResCall.enqueue(new Callback<PostRes>() {
            @Override
            public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                if(response.code()==200) {
                    Toast.makeText(context, R.string.revise_complete, Toast.LENGTH_SHORT).show();
                    ((Activity) context).finish();

                }else{
                    Log.i("aaa",response.toString());
                }
            }

            @Override
            public void onFailure(Call<PostRes> call, Throwable t) {

            }
        });
    }

    public void addPostData(Context context, String token,int offset,int limit){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        PostApi postApi = retrofit.create(PostApi.class);

        Call<PostRes> postResCall = postApi.getAllPost(token,offset,limit);

        postResCall.enqueue(new Callback<PostRes>() {
            @Override
            public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                if(response.code()==200) {
                    Log.i("aaa","cnt : "+response.body().getCnt());
                    List<Items> itemsList;
                    itemsList = response.body().getItems();
                    Adapter_home adapter_home = new Adapter_home(context,itemsList);
                    adapter_home.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PostRes> call, Throwable t) {

            }
        });
    }



}
