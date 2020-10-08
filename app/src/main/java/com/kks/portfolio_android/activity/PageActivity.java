package com.kks.portfolio_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.api.RetrofitApi;
import com.kks.portfolio_android.api.VolleyApi;
import com.kks.portfolio_android.follow.Follower_Activity;
import com.kks.portfolio_android.follow.Following_Activity;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import org.json.JSONObject;

import java.util.ArrayList;

public class PageActivity extends AppCompatActivity {

    String token;

    RecyclerView recyclerView;

    ArrayList<Posting> postingArrayList = new ArrayList<>();

    ImageView page_img_profile;
    ImageView page_img_back;

    TextView page_txt_userName;
    TextView page_txt_postingCnt;
    TextView page_txt_followerCnt;
    TextView page_txt_followingCnt;
    TextView page_txt_introduce;

    Button page_btn_follow;
    Button page_btn_unFollow;

    VolleyApi volleyApi = new VolleyApi();
    RetrofitApi retrofitApi = new RetrofitApi();

    int sp_user_id;
    int offset;
    int user_id;
    int limit = 25;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        postingArrayList.clear();

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
        retrofitApi.getPagePhoto(PageActivity.this,user_id,offset,limit,recyclerView);
        retrofitApi.checkFollow(PageActivity.this,token,user_id,sp_user_id,page_btn_follow,page_btn_unFollow);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}