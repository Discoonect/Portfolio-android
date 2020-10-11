package com.kks.portfolio_android.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.adapter.Adapter_user;
import com.kks.portfolio_android.api.NetworkClient;
import com.kks.portfolio_android.api.RetrofitApi;
import com.kks.portfolio_android.api.UserApi;
import com.kks.portfolio_android.api.VolleyApi;
import com.kks.portfolio_android.follow.Follower_Activity;
import com.kks.portfolio_android.follow.Following_Activity;
import com.kks.portfolio_android.model.Items;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.res.UserRes;
import com.kks.portfolio_android.util.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PageActivity extends AppCompatActivity {

    String token;

    RecyclerView recyclerView;

    ImageView page_img_profile;
    ImageView page_img_back;

    TextView page_txt_userName;
    TextView page_txt_postingCnt;
    TextView page_txt_followerCnt;
    TextView page_txt_followingCnt;
    TextView page_txt_introduce;

    Button page_btn_follow;
    Button page_btn_unFollow;

    Adapter_user adapter_user;

    List<Items> itemsList = new ArrayList<>();

    RetrofitApi retrofitApi = new RetrofitApi();

    int sp_user_id;
    int offset;
    int user_id;
    int limit = 8;
    int cnt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        itemsList.clear();

        recyclerView = findViewById(R.id.page_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(PageActivity.this,3));

        SharedPreferences sharedPreferences =
                getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
        sp_user_id = sharedPreferences.getInt("user_id",0);
        user_id = getIntent().getIntExtra("user_id",0);

        page_img_profile = findViewById(R.id.page_img_profile);
        page_img_back = findViewById(R.id.page_img_back);
        page_txt_userName = findViewById(R.id.page_txt_userName);
        page_txt_postingCnt = findViewById(R.id.page_txt_postingCnt);
        page_txt_followerCnt = findViewById(R.id.page_txt_followerCnt);
        page_txt_followingCnt = findViewById(R.id.page_txt_followingCnt);
        page_txt_introduce = findViewById(R.id.page_txt_introduce);
        page_btn_follow = findViewById(R.id.page_btn_follow);
        page_btn_unFollow = findViewById(R.id.page_btn_unFollow);



        page_img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        page_txt_followerCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PageActivity.this, Follower_Activity.class);
                i.putExtra("user_id",user_id);
                startActivity(i);
            }
        });

        page_txt_followingCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PageActivity.this, Following_Activity.class);
                i.putExtra("user_id",user_id);
                startActivity(i);
            }
        });

        page_btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrofitApi.followUser(PageActivity.this,user_id,token,page_btn_follow,page_btn_unFollow,page_txt_followerCnt);
            }
        });

        page_btn_unFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrofitApi.cancelFollow(PageActivity.this,user_id,token,page_btn_follow,page_btn_unFollow,page_txt_followerCnt);
            }
        });

        retrofitApi.getUserPage1(PageActivity.this,user_id,page_img_profile,page_txt_userName,page_txt_followerCnt,page_txt_introduce);
        retrofitApi.getUserPage2(PageActivity.this,user_id,page_txt_postingCnt,page_txt_followingCnt);
        getPagePhoto(PageActivity.this,user_id,limit,recyclerView);
        retrofitApi.checkFollow(PageActivity.this,token,user_id,sp_user_id,page_btn_follow,page_btn_unFollow);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                if (lastPosition+1 == totalCount) {
                    if(cnt==limit){
                        addPagePhoto(PageActivity.this,user_id,limit);
                    }else{
                        Toast.makeText(PageActivity.this, "마지막 게시물입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void getPagePhoto(Context context, int user_id, int limit, RecyclerView recyclerView){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        UserApi userApi = retrofit.create(UserApi.class);

        Call<UserRes> userResCall = userApi.getPagePhoto(user_id,offset,limit);

        userResCall.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if(response.code()==200) {
                    itemsList = response.body().getItems();
                    adapter_user = new Adapter_user(context,itemsList);
                    recyclerView.setAdapter(adapter_user);

                    offset = offset+response.body().getCnt();
                    cnt = response.body().getCnt();
                }
            }
            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {
            }
        });
    }

    public void addPagePhoto(Context context, int user_id, int limit){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        UserApi userApi = retrofit.create(UserApi.class);

        Call<UserRes> userResCall = userApi.getPagePhoto(user_id,offset,limit);

        userResCall.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if(response.code()==200) {
                    for(int i=0; i<response.body().getItems().size(); i++){
                        Items item = response.body().getItems().get(i);
                        int id = item.getId();
                        int user_id = item.getUser_id();
                        String photo_url = item.getPhoto_url();

                        Items items = new Items(id,user_id,photo_url);
                        itemsList.add(items);
                    }

                    adapter_user.notifyDataSetChanged();

                    offset = offset+response.body().getCnt();
                    cnt = response.body().getCnt();
                }
            }
            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {
            }
        });
    }
}