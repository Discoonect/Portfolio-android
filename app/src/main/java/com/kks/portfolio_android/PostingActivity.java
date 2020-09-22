package com.kks.portfolio_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.kks.portfolio_android.adapter.Adapter_home;
import com.kks.portfolio_android.api.PostApi;
import com.kks.portfolio_android.model.Comments;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class PostingActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    JSONObject jsonObject = new JSONObject();
    ArrayList<Posting> list = new ArrayList<>();

    ImageView po_img_profile;
    TextView po_txt_userName;
    ImageView po_img_menu;
    ImageView po_img_photo;
    ImageView po_img_like;
    ImageView po_img_comment;
    TextView po_txt_cntComment;
    TextView po_txt_content;
    TextView po_txt_created;
    TextView po_txt_cntFavorite;
    ImageView po_img_back;

    int mylike;

    int user_id;
    int post_id;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        list.clear();

        po_img_profile = findViewById(R.id.po_img_profile);
        po_txt_cntComment = findViewById(R.id.po_txt_cntComment);
        po_txt_userName = findViewById(R.id.po_txt_userName);
        po_img_menu = findViewById(R.id.po_img_menu);
        po_img_photo = findViewById(R.id.po_img_photo);
        po_img_like = findViewById(R.id.po_img_like);
        po_img_comment = findViewById(R.id.po_img_comment);
        po_txt_content = findViewById(R.id.po_txt_content);
        po_txt_created = findViewById(R.id.po_txt_created);
        po_txt_cntFavorite = findViewById(R.id.po_txt_cntFavorite);
        po_img_back = findViewById(R.id.po_img_back);

        SharedPreferences sharedPreferences =
                getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);

        post_id = getIntent().getIntExtra("post_id",0);
        user_id = getIntent().getIntExtra("user_id",0);

        po_img_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PostingActivity.this,CommentActivity.class);
                i.putExtra("post_id",post_id);
                startActivity(i);
            }
        });

        po_txt_cntComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PostingActivity.this,CommentActivity.class);
                i.putExtra("post_id",post_id);
                startActivity(i);
            }
        });

        po_img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        
        po_img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mylike==1){
                    clickDislike(post_id,token);
                }else{
                    clickLike(post_id,token);
                }
            }
        });

        po_img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(PostingActivity.this,po_img_menu);
                popupMenu.inflate(R.menu.fh_post_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.fh_menu_edit:
                                Toast.makeText(PostingActivity.this, "수정", Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.fh_menu_delete:
                                deletePosting(post_id,token);
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

        po_img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostingActivity.this,PageActivity.class);

                if(user_id==0){
                    user_id = list.get(0).getUser_id();
                    Log.i("aaa",""+ user_id);
                }

                i.putExtra("user_id",user_id);
                startActivity(i);

            }
        });

        getPostData(post_id,token);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void deletePosting(int post_id, String token) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, Util.BASE_URL + "/api/v1/post/deletepost/"+post_id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(PostingActivity.this, "포스팅 삭제 성공", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> stringStringMap = new HashMap<String, String>();
                stringStringMap.put("Authorization","Bearer "+token);
                return stringStringMap;
            }
        };
        Volley.newRequestQueue(PostingActivity.this).add(request);
    }

    private void clickLike(int post_id, String token) {
        JSONObject body = new JSONObject();
        try {
            body.put("post_id", post_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Util.BASE_URL + "/api/v1/like/likepost",
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mylike=1;
                        po_img_like.setImageResource(R.drawable.ic_baseline_favorite_24);
                        getLikeCntData(post_id);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("aaa",error.toString());
                    }
                }
        )  {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        } ;
        Volley.newRequestQueue(PostingActivity.this).add(request);
    }

    private void clickDislike(int post_id,String token) {
        JSONObject body = new JSONObject();
        try {
            body.put("post_id", post_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Util.BASE_URL + "/api/v1/like/deletelikepost",
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mylike=0;
                        po_img_like.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        getLikeCntData(post_id);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("aaa",error.toString());
                    }
                }
        )  {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        } ;
        Volley.newRequestQueue(PostingActivity.this).add(request);
    }

    private void getLikeCntData(int post_id) {
        JsonObjectRequest request1 = new JsonObjectRequest(
                Request.Method.GET, Util.BASE_URL + "/api/v1/like/countlikepost/" + post_id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int likecnt = response.getInt("cnt");
                            po_txt_cntFavorite.setText(""+likecnt+"명이 좋아합니다");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        Volley.newRequestQueue(PostingActivity.this).add(request1);
    }

    private void getPostData(int post_id,String token) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Util.BASE_URL + "/api/v1/post/getonepost/"+post_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("aaa",response.toString());
                        try{

                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                Toast.makeText(PostingActivity.this, "떙", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONArray items = response.getJSONArray("items");
                            JSONObject jsonObject = items.getJSONObject(0);

                            int post_id = jsonObject.getInt("post_id");

                            SharedPreferences sharedPreferences =
                                    getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
                            int sp_user_id = sharedPreferences.getInt("user_id",0);
                            int user_id = jsonObject.getInt("user_id");
                            Posting posting = new Posting(user_id);
                            list.add(posting);

                            if(sp_user_id==user_id){
                                po_img_menu.setVisibility(View.VISIBLE);
                            }else{
                                po_img_menu.setVisibility(View.INVISIBLE);
                            }

                            String user_profile = jsonObject.getString("user_profilephoto");
                            if(user_profile == "null"){
                                po_img_profile.setImageResource(R.drawable.ic_baseline_account_circle_24);
                            }else{
                                Glide.with(PostingActivity.this).load(Util.BASE_URL+"/public/uploads/"+user_profile).into(po_img_profile);
                            }

                            String user_name = jsonObject.getString("user_name");
                            po_txt_userName.setText(user_name);

                            String content = jsonObject.getString("content");
                            po_txt_content.setText(content);

                            int comment_cnt = jsonObject.getInt("comment_cnt");
                            po_txt_cntComment.setText("댓글 "+comment_cnt+"개");

                            int like_cnt = jsonObject.getInt("like_cnt");
                            po_txt_cntFavorite.setText(""+like_cnt+"명이 좋아합니다");

                            mylike = jsonObject.getInt("mylike");
                            if(mylike == 1){
                                po_img_like.setImageResource(R.drawable.ic_baseline_favorite_24);
                            }else {
                                po_img_like.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                            }

                            String photo = jsonObject.getString("photo_url");
                            String photo_url = Util.BASE_URL+"/public/uploads/"+photo;

                            Glide.with(PostingActivity.this).load(photo_url).into(po_img_photo);

                            String created_at = jsonObject.getString("created_at");
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            df.setTimeZone(TimeZone.getTimeZone("UTC"));
                            try {
                                Date date = df.parse(created_at);
                                df.setTimeZone(TimeZone.getDefault());
                                String strDate = df.format(date);
                                po_txt_created.setText(strDate);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> stringStringMap = new HashMap<String, String>();
                stringStringMap.put("Authorization","Bearer "+token);
                return stringStringMap;
            }
        };
        Volley.newRequestQueue(PostingActivity.this).add(request);
    }
}