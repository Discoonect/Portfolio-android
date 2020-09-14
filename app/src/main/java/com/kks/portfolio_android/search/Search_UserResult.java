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
import com.kks.portfolio_android.CommentActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.adapter.Adapter_SearchUser;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Search_UserResult extends AppCompatActivity {

    ImageView su_img_back;
    TextView su_txt_title;

    ArrayList<Posting> list = new ArrayList<>();

    Adapter_SearchUser adapter_searchUser;

    RecyclerView recyclerView;

    int offset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__user_result);

        Toast.makeText(this, "유저검색", Toast.LENGTH_SHORT).show();

        su_img_back = findViewById(R.id.su_img_back);
        su_txt_title = findViewById(R.id.su_txt_title);

        SharedPreferences sharedPreferences =
                getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);
        int sp_userId = sharedPreferences.getInt("user_id",0);

        String keyword = getIntent().getStringExtra("keyword");
        Log.i("aaa",keyword);

        recyclerView = findViewById(R.id.su_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Search_UserResult.this));

        su_img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        su_txt_title.setText("'"+keyword+"' 검색결과");

        getSearchUser(keyword,sp_userId);

    }

    private void getSearchUser(String keyword,int sp_userId) {
        list.clear();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Util.BASE_URL + "/api/v1/search/search?keyword="+keyword+"&offset="+offset+"&limit=30", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{

                            boolean success = response.getBoolean("success");

                            if (success == false) {
                                Toast.makeText(Search_UserResult.this, "떙", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONArray items = response.getJSONArray("items");
                            for(int i=0; i<items.length(); i++) {
                                JSONObject jsonObject = items.getJSONObject(i);

                                int user_id = jsonObject.getInt("user_id");

                                String profile = jsonObject.getString("user_profilephoto");
                                String user_name = jsonObject.getString("user_name");

                                Posting posting = new Posting(user_id,user_name,profile);

                                list.add(posting);

                                if(user_id==sp_userId){
                                    list.remove(posting);
                                }
                            }
                            adapter_searchUser = new Adapter_SearchUser(Search_UserResult.this, list);
                            recyclerView.setAdapter(adapter_searchUser);
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
        Volley.newRequestQueue(Search_UserResult.this).add(request);
    }
}