package com.kks.portfolio_android.api;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.kks.portfolio_android.HomeActivity;
import com.kks.portfolio_android.MainActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.Sign_upActivity;
import com.kks.portfolio_android.adapter.Adapter_follow;
import com.kks.portfolio_android.adapter.Adapter_user;
import com.kks.portfolio_android.follow.Follower_Activity;
import com.kks.portfolio_android.follow.Following_Activity;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
                new JsonObjectRequest(Request.Method.POST, Util.BASE_URL + "/api/v1/user/login",body,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    boolean success = response.getBoolean("success");
                                    if(success==false){
                                        Toast.makeText(context, "문제있음", Toast.LENGTH_SHORT).show();
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
                                        editor.putString("auto","on");
                                        editor.apply();
                                    }else{
                                        editor.putString("auto","off");
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

    public void signUp(String name,String password,String phone,Context context){
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
                new JsonObjectRequest(Request.Method.POST, Util.BASE_URL + "/api/v1/user/signup",body,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(context, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(context, MainActivity.class);
                                context.startActivity(i);
                                ((Activity)context).finish();
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

    public void alertDialog_Unchecked(String message, Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.myDialogTheme);
        alertDialog.setTitle("아이디 중복체크");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton
                ("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int check_name=0;
                    }
                });
        final AlertDialog dialog = alertDialog.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void alertDialog_checked(String message, Context context){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.myDialogTheme);
        alertDialog.setTitle("아이디 중복체크");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton
                ("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        final AlertDialog dialog = alertDialog.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
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

    public void getUserPosting(Context context, int user_id, int offset, RecyclerView recyclerView){

        ArrayList<Posting> postingArrayList = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Util.BASE_URL + "/api/v1/post/getpostphotourl/"+user_id+"?offset="+offset+"&limit=25",
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

                            for(int i=0; i<items.length();i++){
                                JSONObject jsonObject = items.getJSONObject(i);

                                int post_id = jsonObject.getInt("id");

                                String photo = jsonObject.getString("photo_url");
                                String photo_url = Util.BASE_URL+"/public/uploads/"+photo;

                                Posting posting = new Posting(post_id,photo_url);

                                postingArrayList.add(posting);
                            }
                            Adapter_user  adapter_user = new Adapter_user(context, postingArrayList);
                            recyclerView.setAdapter(adapter_user);
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

    public void getSettingData(Context context, int user_id, ImageView setting_img_profile, EditText setting_edit_introduce){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.GET, Util.BASE_URL + "/api/v1/user/userpage/"+user_id,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try{
                                    boolean success = response.getBoolean("success");
                                    if(success==false){
                                        Toast.makeText(context, "문제있음", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    JSONArray items = response.getJSONArray("items");
                                    JSONObject jsonObject = items.getJSONObject(0);


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

    public void writeIntroduce(){

    }

    public void checkFollow(Context context, int user_id, int follow_id, String token, Button page_btn_follow,Button page_btn_unFollow){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.GET, Util.BASE_URL + "/api/v1/follow/checkfollow/"+follow_id,null,
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
                Util.BASE_URL + "/api/v1/follow/userfollowing/"+user_id+"?offset="+offset+"&limit=25", null,
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
                Util.BASE_URL + "/api/v1/follow/userfollower/"+user_id+"?offset="+offset+"&limit=25", null,
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
                        Log.i("aaa",response.toString());
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


}
