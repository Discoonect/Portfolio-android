package com.kks.portfolio_android.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.kks.portfolio_android.activity.MainActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.adapter.Adapter_user;
import com.kks.portfolio_android.api.RetrofitApi;
import com.kks.portfolio_android.follow.Follower_Activity;
import com.kks.portfolio_android.follow.Following_Activity;
import com.kks.portfolio_android.model.Items;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_User extends Fragment {

    RequestQueue requestQueue;


    TextView fu_txt_postingCnt;
    TextView fu_txt_followerCnt;
    TextView fu_txt_followingCnt;
    TextView fu_txt_userName;
    ImageView fu_img_profile;
    TextView fu_txt_introduce;

    RecyclerView recyclerView;

    List<Items> itemsList = new ArrayList<>();

    JSONObject jsonObject = new JSONObject();

    RetrofitApi retrofitApi = new RetrofitApi();

    String token;
    int offset;
    int user_id;
    int limit = 25;


    public static Fragment_User newInstance(){
        return new Fragment_User();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment__user,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        itemsList.clear();

        recyclerView = getView().findViewById(R.id.fu_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));

        SharedPreferences sharedPreferences =
                getContext().getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
        user_id = sharedPreferences.getInt("user_id",0);

        fu_txt_followerCnt = getView().findViewById(R.id.fu_txt_followerCnt);
        fu_txt_followingCnt = getView().findViewById(R.id.fu_txt_followingCnt);
        fu_txt_introduce = getView().findViewById(R.id.fu_txt_introduce);
        fu_img_profile = getView().findViewById(R.id.fu_img_profile);
        fu_txt_userName = getView().findViewById(R.id.fu_txt_userName);
        fu_txt_followerCnt = getView().findViewById(R.id.fu_txt_followerCnt);
        fu_txt_postingCnt = getView().findViewById(R.id.fu_txt_postingCnt);
        fu_txt_followingCnt = getView().findViewById(R.id.fu_txt_followingCnt);



        fu_txt_followerCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Follower_Activity.class);
                getContext().startActivity(i);
            }
        });

        fu_txt_followingCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Following_Activity.class);
                getContext().startActivity(i);
            }
        });

        retrofitApi.getMyPage1(getContext(),token,fu_txt_introduce,fu_img_profile,fu_txt_userName,fu_txt_followerCnt);
        retrofitApi.getMyPage2(getContext(),token,fu_txt_postingCnt,fu_txt_followingCnt);
        retrofitApi.getPagePhoto(getContext(),user_id,offset,limit,recyclerView);
    }

//    private void getMyPosting(int user_id) {
//        requestQueue = Volley.newRequestQueue(getContext());
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
//                Util.BASE_URL + "/api/v1/post/getpostphotourl/"+user_id+"?offset="+offset+"&limit=25",
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.i("aaa",response.toString());
//
//                        try {
//                            boolean success = response.getBoolean("success");
//                            if (success == false) {
//                                Toast.makeText(getActivity(), "다시 시도 해주세요.", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//
//                            JSONArray items = response.getJSONArray("items");
//
//                            for(int i=0; i<items.length();i++){
//                                jsonObject = items.getJSONObject(i);
//
//                                int post_id = jsonObject.getInt("id");
//                                int user_id = jsonObject.getInt("user_id");
//
//                                String photo = jsonObject.getString("photo_url");
//                                String photo_url = Util.BASE_URL+"/public/uploads/"+photo;
//
//                                Posting posting = new Posting(post_id,user_id,photo_url);
//
//                                postingArrayList.add(posting);
//                            }
//                            adapter_user = new Adapter_user(getActivity(), postingArrayList);
//                            recyclerView.setAdapter(adapter_user);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                    }
//                }
//        );
//        requestQueue.add(jsonObjectRequest);
//    }

    private void getUserData2(String token) {

        requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Util.BASE_URL + "/api/v1/user/mypage2",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                Toast.makeText(getActivity(), "다시 시도 해주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONArray items = response.getJSONArray("items");
                            jsonObject = items.getJSONObject(0);

                            int posting_cnt = jsonObject.getInt("cnt_post");
                            int following_cnt = jsonObject.getInt("following");

                            fu_txt_postingCnt.setText(""+posting_cnt);
                            fu_txt_followingCnt.setText(""+following_cnt);
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
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> stringStringMap = new HashMap<String, String>();
                stringStringMap.put("Authorization","Bearer "+token);
                return stringStringMap;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void getUserData(String token) {
        requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Util.BASE_URL + "/api/v1/user/mypage",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("aaa",response.toString());

                        try {
                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                Toast.makeText(getActivity(), "다시 시도 해주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONArray items = response.getJSONArray("items");
                            jsonObject = items.getJSONObject(0);

                            String user_name = jsonObject.getString("user_name");
                            String user_profile = jsonObject.getString("user_profilephoto");
                            String userProfile_url = Util.BASE_URL+"/public/uploads/"+user_profile;
                            String introduce = jsonObject.getString("introduce");

                            int follower_cnt = jsonObject.getInt("follower");

                            if(!user_profile.equals("null")){
                                Glide.with(getContext()).load(userProfile_url).into(fu_img_profile);
                            }else{
                                fu_img_profile.setImageResource(R.drawable.ic_baseline_account_circle_24);
                            }

                            fu_txt_userName.setText(user_name);
                            fu_txt_followerCnt.setText(""+follower_cnt);
                            if(introduce=="null"){
                                fu_txt_introduce.setText("");
                            }else{
                                fu_txt_introduce.setText(introduce);
                            }


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
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> stringStringMap = new HashMap<String, String>();
                stringStringMap.put("Authorization","Bearer "+token);
                return stringStringMap;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}