package com.kks.portfolio_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kks.portfolio_android.R;
import com.kks.portfolio_android.api.RetrofitApi;
import com.kks.portfolio_android.api.VolleyApi;

public class PostLikeUserActivity extends AppCompatActivity {

    ImageView plu_img_back;
    TextView plu_txt_cnt;

    RecyclerView recyclerView;

    int post_id;

    RetrofitApi retrofitApi = new RetrofitApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_like_user);

        plu_img_back = findViewById(R.id.plu_img_back);
        plu_txt_cnt = findViewById(R.id.plu_txt_cnt);

        recyclerView = findViewById(R.id.plu_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        post_id = getIntent().getIntExtra("post_id",0);

        plu_img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        retrofitApi.likePostUser(this,post_id,recyclerView);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}