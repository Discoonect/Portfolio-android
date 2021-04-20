package com.kks.portfolio_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.api.NetworkClient;
import com.kks.portfolio_android.api.PostApi;
import com.kks.portfolio_android.api.RetrofitApi;
import com.kks.portfolio_android.model.Items;
import com.kks.portfolio_android.res.PostRes;
import com.kks.portfolio_android.util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RevisePostingActivity extends AppCompatActivity {

    String token;
    int user_id;
    int post_id;

    Button rv_btn_cancel;
    Button rv_btn_revise;
    ImageView rv_img_img;
    EditText rv_edit_content;

    RetrofitApi retrofitApi = new RetrofitApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_posting);

        rv_btn_cancel = findViewById(R.id.rv_btn_cancel);
        rv_btn_revise = findViewById(R.id.rv_btn_revise);
        rv_img_img = findViewById(R.id.rv_img_img);
        rv_edit_content = findViewById(R.id.rv_edit_content);

        SharedPreferences sharedPreferences =
                getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
        user_id = sharedPreferences.getInt("user_id",0);

        post_id = getIntent().getIntExtra("post_id",0);

        rv_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rv_btn_revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = rv_edit_content.getText().toString().trim();
                retrofitApi.updatePost(RevisePostingActivity.this,token,post_id,content);

            }
        });

        getPostData(this,token,post_id,rv_img_img,rv_edit_content);
    }

    public void getPostData(Context context,String token,int post_id,ImageView rv_img_img,EditText rv_edit_content){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        PostApi postApi = retrofit.create(PostApi.class);

        Call<PostRes> postResCall = postApi.getOnePost(token,post_id);
        postResCall.enqueue(new Callback<PostRes>() {
            @Override
            public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                if(response.code()==200){
                    Items items = response.body().getItems().get(0);

                    String photo = items.getPhoto_url();
                    Glide.with(context).load(Util.IMAGE_PATH + photo).into(rv_img_img);

                    String content = items.getContent();
                    rv_edit_content.setText(content);
                }else{
                    Log.i("aaa",response.toString());
                }
            }

            @Override
            public void onFailure(Call<PostRes> call, Throwable t) {

            }
        });
    }
}