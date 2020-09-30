package com.kks.portfolio_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.kks.portfolio_android.adapter.Adapter_comment;
import com.kks.portfolio_android.model.Comments;
import com.kks.portfolio_android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {

    ImageView cm_btn_back;
    EditText cm_edit_comment;
    Button cm_btn_complete;


    RequestQueue requestQueue;
    JSONObject jsonObject = new JSONObject();
    ArrayList<Comments> list = new ArrayList<>();

    RecyclerView recyclerView;
    Adapter_comment adapter_comment;

    int post_id;
    int offset;
    int limit = 25;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        SharedPreferences sharedPreferences =
                getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);

        requestQueue = Volley.newRequestQueue(CommentActivity.this);

        cm_btn_back = findViewById(R.id.cm_btn_back);
        cm_edit_comment = findViewById(R.id.cm_edit_comment);
        cm_btn_complete = findViewById(R.id.cm_btn_complete);

        post_id = getIntent().getIntExtra("post_id",0);

        cm_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.cm_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));

        cm_btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = cm_edit_comment.getText().toString().trim();
                uploadComment(post_id,comment,token);
            }
        });
        getCommentData(post_id,limit);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (token == null) {
            Toast.makeText(this,R.string.please_login, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void uploadComment(int post_id, String comment, String token) {
        JSONObject body = new JSONObject();
        try {
            body.put("post_id", post_id);
            body.put("comment", comment);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, Util.ADD_COMMENT, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            boolean success = response.getBoolean("success");

                            if (success == false) {
                                Toast.makeText(CommentActivity.this, R.string.success_fail, Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Toast.makeText(CommentActivity.this, R.string.comment_complete, Toast.LENGTH_SHORT).show();

                            cm_edit_comment.setText(R.string.text_clear);
                            offset = 0;
                            getCommentData(post_id,limit);
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
        Volley.newRequestQueue(CommentActivity.this).add(request);

    }

    private void getCommentData(int post_id,int limit) {
        list.clear();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Util.GET_COMMENT+ post_id + Util.OFFSET + offset + Util.LIMIT + limit, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("aaa",response.toString());
                        try{
                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                Toast.makeText(CommentActivity.this, R.string.success_fail, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            JSONArray items = response.getJSONArray("items");

                            for(int i=0; i<items.length(); i++){
                                jsonObject = items.getJSONObject(i);
                                int post_user_id = jsonObject.getInt("post_user_id");
                                int post_id = jsonObject.getInt("post_id");
                                int comment_id = jsonObject.getInt("comment_id");
                                int user_id = jsonObject.getInt("user_id");
                                String user_name = jsonObject.getString("user_name");
                                String comment = jsonObject.getString("comment");
                                String created_at = jsonObject.getString("created_at");
                                String profile = jsonObject.getString("user_profilephoto");

                                Comments comments = new Comments(post_user_id,post_id,comment_id,user_id,user_name,comment,created_at,profile);
                                list.add(comments);
                            }
                            adapter_comment = new Adapter_comment(CommentActivity.this, list);
                            recyclerView.setAdapter(adapter_comment);

                            offset = offset + response.getInt("cnt");
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
        );
        Volley.newRequestQueue(CommentActivity.this).add(request);
    }

}