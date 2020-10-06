package com.kks.portfolio_android.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.adapter.Adapter_SearchUser;
import com.kks.portfolio_android.adapter.Adapter_searchPosting;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Search_PostingResult extends AppCompatActivity {

    ImageView sp_img_back;
    TextView sp_txt_title;

    Adapter_searchPosting adapter_searchPosting;
    RecyclerView recyclerView;

    ArrayList<Posting> list = new ArrayList<>();

    int offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__posting_result);

        Toast.makeText(this, "포스팅검색", Toast.LENGTH_SHORT).show();

        sp_img_back = findViewById(R.id.sp_img_back);
        sp_txt_title = findViewById(R.id.sp_txt_title);

        String keyword = getIntent().getStringExtra("keyword");

        recyclerView = findViewById(R.id.sp_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Search_PostingResult.this));

        sp_img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sp_txt_title.setText("'"+keyword+"' 검색결과");

        getSearchPosting(keyword);

    }

    private void getSearchPosting(String keyword) {
        list.clear();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Util.BASE_URL + "/api/v1/search?keyword="+keyword+"&offset="+offset+"&limit=30", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("aaa","서치 포스팅 리스폰스 :"+response.toString());
                        try{

                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                Toast.makeText(Search_PostingResult.this, "떙", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONArray items = response.getJSONArray("items");
                            for(int i=0; i<items.length(); i++) {
                                JSONObject jsonObject = items.getJSONObject(i);

                                int user_id = jsonObject.getInt("user_id");
                                int post_id = jsonObject.getInt("id");
                                String content = jsonObject.getString("content");
                                String photo = jsonObject.getString("photo_url");
                                String photo_url = Util.BASE_URL+"/public/uploads/"+photo;

                                Posting posting = new Posting(post_id,user_id,photo_url,content);
                                list.add(posting);

                            }
                            adapter_searchPosting = new Adapter_searchPosting(Search_PostingResult.this, list);
                            recyclerView.setAdapter(adapter_searchPosting);
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
        Volley.newRequestQueue(Search_PostingResult.this).add(request);


    }
}