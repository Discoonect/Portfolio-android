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
import com.kks.portfolio_android.CommentActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.adapter.Adapter_follower;
import com.kks.portfolio_android.adapter.Adapter_following;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Following_Activity extends AppCompatActivity {

    RequestQueue requestQueue;
    JSONObject jsonObject;
    ArrayList<Posting> postArrayList = new ArrayList<>();

    Adapter_following adapter_following;
    RecyclerView recyclerView;

    ImageView following_img_back;

    int offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_);
        postArrayList.clear();

        following_img_back = findViewById(R.id.following_img_back);

        SharedPreferences sharedPreferences =
                getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);

        recyclerView = findViewById(R.id.following_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Following_Activity.this));

        requestQueue = Volley.newRequestQueue(Following_Activity.this);

        following_img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getFollowingData(token);

    }

    private void getFollowingData(String token) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Util.BASE_URL + "/api/v1/follow/myfollowing?offset="+offset+"&limit=25", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("aaa",response.toString());
                        try{
                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                Toast.makeText(Following_Activity.this, "떙", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONArray items = response.getJSONArray("items");
                            for(int i=0;i<items.length(); i++){
                                jsonObject = items.getJSONObject(i);
                                int following_id = jsonObject.getInt("following_id");

                                String user_name = jsonObject.getString("user_name");
                                String profile = jsonObject.getString("user_profilephoto");
                                int user_id = jsonObject.getInt("user_id");

                                Posting posting = new Posting(following_id,user_name,profile);

                                postArrayList.add(posting);

                                if (following_id == user_id){
                                    postArrayList.remove(posting);
                                }
                            }

                            adapter_following = new Adapter_following(Following_Activity.this, postArrayList);
                            recyclerView.setAdapter(adapter_following);

                            int cnt = response.getInt("cnt");

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
        requestQueue.add(request);
    }
}