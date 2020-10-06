package com.kks.portfolio_android.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.kks.portfolio_android.activity.HomeActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.adapter.Adapter_favorite;
import com.kks.portfolio_android.adapter.Adapter_follow;
import com.kks.portfolio_android.adapter.Adapter_plu;
import com.kks.portfolio_android.model.Alram;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.res.UserRes;
import com.kks.portfolio_android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class VolleyApi {

    public VolleyApi() {
    }

    public void login(String name, String passwd, Context context, CheckBox auto_login_check){
        JSONObject body = new JSONObject();
        try{
            body.put("user_name",name);
            body.put("user_passwd",passwd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.POST, Util.LOGIN,body,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    boolean success = response.getBoolean("success");
                                    if(success==false){
                                        Toast.makeText(context, R.string.success_fail, Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    String token = response.getString("token");
                                    int user_id = response.getInt("user_id");

                                    SharedPreferences sp = context.getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("token",token);
                                    editor.putInt("user_id",user_id);

                                    editor.apply();

                                    if(auto_login_check.isChecked()==true){
                                        editor.putString("auto",Util.AUTO_LOGIN_ON);
                                        editor.apply();
                                    }else{
                                        editor.putString("auto",Util.AUTO_LOGIN_OFF);
                                        editor.apply();
                                    }
                                    Intent i = new Intent(context, HomeActivity.class);
                                    context.startActivity(i);
                                    ((Activity)context).finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("aaa",error.toString());
                            }
                        }
                );
        requestQueue.add(request);
    }

    public void signUpWithProfile(String name, String password, String phone, Context context, File photoFile){
        JSONObject body = new JSONObject();
        try {
            body.put("user_name", name);
            body.put("user_passwd", password);
            body.put("user_phone", phone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.POST, Util.SIGN_UP,body,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String token = response.getString("token");

                                    Retrofit retrofit = NetworkClient.getRetrofitClient(context);
                                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), photoFile);
                                    MultipartBody.Part part = MultipartBody.Part.createFormData("photo", photoFile.getName(), fileBody);
                                    UserApi userApi = retrofit.create(UserApi.class);

                                    Call<UserRes> call = userApi.uploadProfile("Bearer " + token, part);
                                    call.enqueue(new Callback<UserRes>() {
                                        @Override
                                        public void onResponse(Call<UserRes> call, retrofit2.Response<UserRes> response) {
                                            Toast.makeText(context, R.string.sign_up_complete, Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onFailure(Call<UserRes> call, Throwable t) {
                                            Log.i("aaa", t.toString());
                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("aaa",error.toString());
                            }
                        }
                );
        requestQueue.add(request);
    }

    public void signUp(String name, String password, String phone, Context context){
        JSONObject body = new JSONObject();
        try {
            body.put("user_name", name);
            body.put("user_passwd", password);
            body.put("user_phone", phone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.POST, Util.SIGN_UP,body,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("aaa",response.toString());
                                Toast.makeText(context, R.string.sign_up_complete, Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("aaa",error.toString());
                            }
                        }
                );
        requestQueue.add(request);
    }

    public void getUserPage1(Context context, int user_id, ImageView page_img_profile, TextView page_txt_userName,TextView page_txt_followerCnt,TextView page_txt_introduce){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.GET, Util.BASE_URL + "/api/v1/user/userpage/"+user_id,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("aaa",response.toString());
                                try{
                                    boolean success = response.getBoolean("success");
                                    if(success==false){
                                        Toast.makeText(context, "문제있음", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    JSONArray items = response.getJSONArray("items");
                                    JSONObject jsonObject = items.getJSONObject(0);

                                    String user_name = jsonObject.getString("user_name");
                                    String user_profile = jsonObject.getString("user_profilephoto");
                                    String userProfile_url = Util.BASE_URL+"/public/uploads/"+user_profile;
                                    String introduce = jsonObject.getString("introduce");

                                    if(introduce.equals("null")){
                                        introduce = "";
                                    }

                                    int follower_cnt = jsonObject.getInt("follower");

                                    if(!user_profile.equals("null")){
                                        Glide.with(context).load(userProfile_url).into(page_img_profile);
                                    }else{
                                        page_img_profile.setImageResource(R.drawable.ic_baseline_account_circle_24);
                                    }

                                    page_txt_userName.setText(user_name);
                                    page_txt_followerCnt.setText(""+follower_cnt);
                                    page_txt_introduce.setText(introduce);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("aaa",error.toString());
                            }
                        }
                );
        requestQueue.add(request);
    }

    public void getUserPage2(Context context,int user_id,TextView page_txt_postingCnt,TextView page_txt_followingCnt){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Util.BASE_URL + "/api/v1/user/userpage2/"+user_id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                Toast.makeText(context, "다시 시도 해주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONArray items = response.getJSONArray("items");
                            JSONObject jsonObject = items.getJSONObject(0);

                            int posting_cnt = jsonObject.getInt("cnt_post");
                            int following_cnt = jsonObject.getInt("following");

                            page_txt_postingCnt.setText(""+posting_cnt);
                            page_txt_followingCnt.setText(""+following_cnt);
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
        requestQueue.add(jsonObjectRequest);
    }

//    public void getUserPosting(Context context, int user_id, int offset, RecyclerView recyclerView){
//
//        ArrayList<Posting> postingArrayList = new ArrayList<>();
//
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
//                Util.BASE_URL + "/api/v1/post/getpostphotourl/"+user_id+"?offset="+offset+"&limit=25",
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        try {
//                            boolean success = response.getBoolean("success");
//                            if (success == false) {
//                                Toast.makeText(context, "다시 시도 해주세요.", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//
//                            JSONArray items = response.getJSONArray("items");
//
//                            for(int i=0; i<items.length();i++){
//                                JSONObject jsonObject = items.getJSONObject(i);
//
//                                int post_id = jsonObject.getInt("id");
//
//                                String photo = jsonObject.getString("photo_url");
//                                String photo_url = Util.BASE_URL+"/public/uploads/"+photo;
//
//                                Posting posting = new Posting(post_id,photo_url);
//
//                                postingArrayList.add(posting);
//                            }
//                            Adapter_user  adapter_user = new Adapter_user(context, postingArrayList);
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

    public void getSettingData(Context context, int user_id, TextView setting_txt_userName,ImageView setting_img_profile, EditText setting_edit_introduce){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.GET, Util.BASE_URL + "/api/v1/user/userpage/"+user_id,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("aaa",response.toString());

                                try{
                                    boolean success = response.getBoolean("success");
                                    if(success==false){
                                        Toast.makeText(context, "문제있음", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    JSONArray items = response.getJSONArray("items");
                                    JSONObject jsonObject = items.getJSONObject(0);

                                    String user_name = jsonObject.getString("user_name");
                                    setting_txt_userName.setText(user_name);


                                    String user_profile = jsonObject.getString("user_profilephoto");
                                    String userProfile_url = Util.BASE_URL+"/public/uploads/"+user_profile;

                                    String introduce = jsonObject.getString("introduce");
                                    if(introduce.equals("null")){
                                        setting_edit_introduce.setHint("20자 이내로 작성해주세요");
                                    } else {
                                        setting_edit_introduce.setText(introduce);
                                    }

                                    if(!user_profile.equals("null")){
                                        Glide.with(context).load(userProfile_url).into(setting_img_profile);
                                    }else{
                                        setting_img_profile.setImageResource(R.drawable.ic_baseline_account_circle_24);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("aaa",error.toString());
                            }
                        }
                );
        requestQueue.add(request);
    }

    public void writeIntroduce(Context context,String introduce,String token){
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject body = new JSONObject();
        try {
            body.put("introduce", introduce);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                Util.BASE_URL + "/api/v1/user/myintroduce", body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("aaa",response.toString());
                        try{
                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                Toast.makeText(context, "떙", Toast.LENGTH_SHORT).show();
                                return;
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
        requestQueue.add(request);
    }

    public void checkFollow(Context context, int user_id, int follow_id, String token, Button page_btn_follow,Button page_btn_unFollow){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.GET, Util.BASE_URL + "/api/v1/follow/checkfollow/"+follow_id,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try{
                                    boolean success = response.getBoolean("success");
                                    if(success==false){
                                        Toast.makeText(context, "문제있음", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    int follow = response.getInt("follow");

                                    if(user_id==follow_id){
                                        page_btn_follow.setVisibility(View.GONE);
                                        page_btn_unFollow.setVisibility(View.GONE);
                                    }else if(follow == 0){
                                        page_btn_follow.setVisibility(View.VISIBLE);
                                    }else if(follow==1){
                                        page_btn_unFollow.setVisibility(View.VISIBLE);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("aaa",error.toString()+"에러");
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

    public void getFollowingData(Context context,int user_id,int offset,RecyclerView recyclerView) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        ArrayList<Posting> postArrayList = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Util.BASE_URL + "/api/v1/follow/following/"+user_id+"?offset="+offset+"&limit=25", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("aaa",response.toString());
                        try{
                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                Toast.makeText(context, "떙", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONArray items = response.getJSONArray("items");
                            for(int i=0;i<items.length(); i++){
                                JSONObject jsonObject = items.getJSONObject(i);
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
                            Adapter_follow adapter_follow;

                            adapter_follow = new Adapter_follow(context, postArrayList);
                            recyclerView.setAdapter(adapter_follow);

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
        );
        requestQueue.add(request);
    }

    public void getFollowerData(Context context,int user_id,int offset,RecyclerView recyclerView) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        ArrayList<Posting> postArrayList = new ArrayList<>();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Util.BASE_URL + "/api/v1/follow/follower/"+user_id+"?offset="+offset+"&limit=25", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("aaa",response.toString());
                        try{
                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                Toast.makeText(context, "떙", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONArray items = response.getJSONArray("items");
                            for(int i=0;i<items.length(); i++){
                                JSONObject jsonObject = items.getJSONObject(i);
                                int following_id = jsonObject.getInt("following_id");

                                String user_name = jsonObject.getString("user_name");
                                String profile = jsonObject.getString("user_profilephoto");
                                int user_id = jsonObject.getInt("user_id");

                                Posting posting = new Posting(user_id,user_name,profile);

                                postArrayList.add(posting);

                                if (following_id == user_id){
                                    postArrayList.remove(posting);
                                }
                            }
                            Adapter_follow adapter_follow;

                            adapter_follow = new Adapter_follow(context, postArrayList);
                            recyclerView.setAdapter(adapter_follow);

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
        );
        requestQueue.add(request);
    }

    public void followUser(Context context,int following_id,String token,Button page_btn_follow,Button page_btn_unFollow,TextView page_txt_followerCnt){
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject body = new JSONObject();
        try {
            body.put("following_id", following_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Util.BASE_URL + "/api/v1/follow/following", body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{
                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                Toast.makeText(context, "떙", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String message = response.getString("message");

                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                            page_btn_follow.setVisibility(View.INVISIBLE);
                            page_btn_unFollow.setVisibility(View.VISIBLE);

                            int follower_cnt = Integer.parseInt(page_txt_followerCnt.getText().toString().trim())+1;

                            page_txt_followerCnt.setText(""+follower_cnt);

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

    public void cancleFollow(Context context,int following_id,String token,Button page_btn_follow,Button page_btn_unFollow,TextView page_txt_followerCnt){
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject body = new JSONObject();
        try {
            body.put("following_id", following_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Util.BASE_URL + "/api/v1/follow/deletefollow", body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("aaa",response.toString());
                        try{
                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                Toast.makeText(context, "떙", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String message = response.getString("message");

                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                            page_btn_unFollow.setVisibility(View.INVISIBLE);
                            page_btn_follow.setVisibility(View.VISIBLE);

                            int follower_cnt = Integer.parseInt(page_txt_followerCnt.getText().toString().trim())-1;

                            page_txt_followerCnt.setText(""+follower_cnt);

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

    public void likePostUser(Context context,int post_id,RecyclerView recyclerView,TextView textView){

        ArrayList<Posting> list = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

//        JsonObjectRequest request = new JsonObjectRequest(
//                Request.Method.GET,
//                Util.BASE_URL + "/api/v1/like/likepostuser/"+post_id, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.i("aaa",response.toString());
//                        try{
//                            boolean success = response.getBoolean("success");
//                            if (success == false) {
//                                Toast.makeText(context, "떙", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//
//                            JSONArray items = response.getJSONArray("items");
//
//                            for(int i=0; i<items.length(); i++) {
//                                JSONObject jsonObject = items.getJSONObject(i);
//
//                                int id = jsonObject.getInt("id");
//                                String name = jsonObject.getString("user_name");
//                                String profile = jsonObject.getString("user_profilephoto");
//                                String time = jsonObject.getString("postliketime");
//
//                                Posting posting = new Posting(id,name,time,profile);
//
//                                list.add(posting);
//                            }
//
//                            int cnt = response.getInt("cnt");
//
//                            textView.setText("좋아요 ("+cnt+")");
//
//                            Adapter_plu adapter_plu = new Adapter_plu(context, list);
//                            recyclerView.setAdapter(adapter_plu);
//
//                        } catch (Exception e) {
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
//        requestQueue.add(request);
    }

    public void postLikeAlarm(Context context,int offset,int limit,String token,RecyclerView recyclerView){
        ArrayList<Alram> list = new ArrayList<>();
        list.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.GET, Util.BASE_URL + "/api/v1/alarm/postlike?offset="+offset+"&limit="+limit,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("aaa",response.toString());

                                try {
                                    JSONArray items = response.getJSONArray("items");

                                    for(int i=0; i<items.length(); i++){
                                        JSONObject jsonObject = items.getJSONObject(i);

                                        String profile = jsonObject.getString("user_profilephoto");
                                        String name = jsonObject.getString("user_name");
                                        int post_id = jsonObject.getInt("post_id");
                                        String photo = Util.BASE_URL+"/public/uploads/"+jsonObject.getString("photo_url");
                                        String time = jsonObject.getString("created_at");
                                        int user_id = jsonObject.getInt("user_id");

                                        //시간 맞추기
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                        df.setTimeZone(TimeZone.getTimeZone("UTC"));

                                        //포스팅 작성시간 표시
                                        try {
                                            Date date = df.parse(time);
                                            df.setTimeZone(TimeZone.getDefault());
                                            String strDate = df.format(date);
                                            String content = name+" 님이 게시물을 좋아합니다.\n"+strDate;

                                            Alram alram = new Alram(user_id,post_id,photo,profile,content,1);
                                            list.add(alram);

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    Adapter_favorite adapter_favorite = new Adapter_favorite(context, list);
                                    recyclerView.setAdapter(adapter_favorite);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("aaa",error.toString());
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

    public void postCommentAlarm(Context context,int offset,int limit,String token, RecyclerView recyclerView){
        ArrayList<Alram> list = new ArrayList<>();
        list.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.GET, Util.BASE_URL + "/api/v1/alarm/comment?offset="+offset+"&limit="+limit,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("aaa",response.toString());

                                try {
                                    JSONArray items = response.getJSONArray("items");

                                    for(int i=0; i<items.length(); i++){
                                        JSONObject jsonObject = items.getJSONObject(i);

                                        int id = jsonObject.getInt("post_id");
                                        String name = jsonObject.getString("user_name");
                                        String profile = jsonObject.getString("user_profilephoto");
                                        String comment = jsonObject.getString("comment");
                                        String time = jsonObject.getString("created_at");
                                        String photo = Util.BASE_URL+"/public/uploads/"+jsonObject.getString("photo_url");
                                        int user_id = jsonObject.getInt("user_id");

                                        if(comment.length()>13){
                                            comment = comment.substring(0,13)+"...";
                                        }

                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                        df.setTimeZone(TimeZone.getTimeZone("UTC"));

                                        try {
                                            Date date = df.parse(time);
                                            df.setTimeZone(TimeZone.getDefault());
                                            String strDate = df.format(date);

                                            String content = name+" 님이 댓글을 달았습니다.\n"+comment+"\n"+strDate;

                                            Alram alram = new Alram(user_id,id,photo,profile,content,2);
                                            list.add(alram);

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    Adapter_favorite adapter_favorite = new Adapter_favorite(context, list);
                                    recyclerView.setAdapter(adapter_favorite);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("aaa",error.toString());
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

    public void followAlram(Context context,int offset,int limit,String token,RecyclerView recyclerView){
        ArrayList<Alram> list = new ArrayList<>();
        list.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.GET, Util.BASE_URL + "/api/v1/alarm/follow?offset="+offset+"&limit="+limit,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("aaa",response.toString());

                                try {
                                    JSONArray items = response.getJSONArray("items");

                                    for(int i=0; i<items.length(); i++){
                                        JSONObject jsonObject = items.getJSONObject(i);

                                        int id = jsonObject.getInt("user_id");
                                        String name = jsonObject.getString("user_name");
                                        String profile = jsonObject.getString("user_profilephoto");
                                        String time = jsonObject.getString("created_at");
                                        int user_id = jsonObject.getInt("user_id");

                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                        df.setTimeZone(TimeZone.getTimeZone("UTC"));

                                        try {
                                            Date date = df.parse(time);
                                            df.setTimeZone(TimeZone.getDefault());
                                            String strDate = df.format(date);

                                            String content = name+" 님이 회원님을 팔로우 했습니다. \n"+strDate;

                                            Alram alram = new Alram(user_id,id,profile,content,3);
                                            list.add(alram);

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    Adapter_favorite adapter_favorite = new Adapter_favorite(context, list);
                                    recyclerView.setAdapter(adapter_favorite);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("aaa",error.toString());
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

    public void deleteProfilePhoto(Context context,String token,int user_id,TextView txt_userName,ImageView img_profile,EditText edit_introduce){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.DELETE,
                        Util.BASE_URL + "/api/v1/user/deleteprofilephoto",null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                boolean success = false;
                                try {
                                    success = response.getBoolean("success");

                                    if (success == false) {
                                        Toast.makeText(context, R.string.success_fail, Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    Toast.makeText(context, "기본 이미지 변경 완료", Toast.LENGTH_SHORT).show();

                                    getSettingData(context,user_id,txt_userName,img_profile,edit_introduce);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("aaa",error.toString());
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

    public void deleteComment(Context context,int comment_id,String token) {
        JSONObject body = new JSONObject();
        try {
            body.put("comment_id", comment_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,Util.DELETE_COMMENT,body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                Toast.makeText(context, "떙", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(context, R.string.delete_comment_complete, Toast.LENGTH_SHORT).show();
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
        )  {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        } ;
        Volley.newRequestQueue(context).add(request);
    }




}
