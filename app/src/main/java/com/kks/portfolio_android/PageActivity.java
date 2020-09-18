package com.kks.portfolio_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.kks.portfolio_android.adapter.Adapter_page;
import com.kks.portfolio_android.adapter.Adapter_user;
import com.kks.portfolio_android.api.VolleyApi;
import com.kks.portfolio_android.follow.Follower_Activity;
import com.kks.portfolio_android.follow.Following_Activity;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PageActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    String token;

    RecyclerView recyclerView;
    Adapter_page adapter_page;

    ArrayList<Posting> postingArrayList = new ArrayList<>();

    JSONObject jsonObject = new JSONObject();

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

    int sp_user_id;
    int offset;
    int user_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        recyclerView = findViewById(R.id.page_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(PageActivity.this,3));

        SharedPreferences sharedPreferences =
                getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
        sp_user_id = sharedPreferences.getInt("user_id",0);

        int user_id = getIntent().getIntExtra("user_id",0);

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        postingArrayList.clear();

        int user_id = getIntent().getIntExtra("user_id",0);
        Log.i("aaa","Page   user_id :"+user_id);
        volleyApi.getUserPage1(PageActivity.this,user_id,page_img_profile,page_txt_userName,page_txt_followerCnt,page_txt_introduce);
        volleyApi.getUserPage2(PageActivity.this,user_id,page_txt_postingCnt,page_txt_followingCnt);
        volleyApi.getUserPosting(PageActivity.this,user_id,offset,recyclerView);
        volleyApi.checkFollow(PageActivity.this,sp_user_id,user_id,token,page_btn_follow,page_btn_unFollow);
    }
}