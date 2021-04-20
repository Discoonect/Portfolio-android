package com.kks.portfolio_android.follow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.adapter.Adapter_follow;
import com.kks.portfolio_android.api.VolleyApi;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Follower_Activity extends AppCompatActivity {

    RequestQueue requestQueue;
    JSONObject jsonObject;

    RecyclerView recyclerView;
    Adapter_follow adapter_follow;

    ArrayList<Posting> postArrayList = new ArrayList<>();

    ImageView follower_img_back;

    int offset;

    VolleyApi volleyApi = new VolleyApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);
        postArrayList.clear();

        follower_img_back = findViewById(R.id.follower_img_back);

        SharedPreferences sharedPreferences =
                getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);

        String token = sharedPreferences.getString("token",null);
        int sp_user_id = sharedPreferences.getInt("user_id",0);

        recyclerView = findViewById(R.id.follower_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Follower_Activity.this));

        requestQueue = Volley.newRequestQueue(Follower_Activity.this);

        follower_img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        int user_id = getIntent().getIntExtra("user_id",0);


        if(user_id==0){
            volleyApi.getFollowerData(Follower_Activity.this,sp_user_id,offset,recyclerView);
        }else{
            volleyApi.getFollowerData(Follower_Activity.this,user_id,offset,recyclerView);
        }
    }
}