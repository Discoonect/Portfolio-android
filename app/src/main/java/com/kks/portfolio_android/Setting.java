package com.kks.portfolio_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kks.portfolio_android.util.Util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Setting extends AppCompatActivity {

    Button setting_btn_leaveMember;
    Button setting_btn_logout;

    String token;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setting_btn_leaveMember = findViewById(R.id.setting_btn_leaveMember);
        setting_btn_logout = findViewById(R.id.setting_btn_logout);

        setting_btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences =
                        getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
                token = sharedPreferences.getString("token",null);

                userLogout();
            }
        });

        setting_btn_leaveMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences =
                        getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
                token = sharedPreferences.getString("token",null);

                alertDialog_adios("회원 탈퇴","정말로 탈퇴하시겠습니까?");

            }
        });
    }

    private void userLogout() {
        requestQueue = Volley.newRequestQueue(Setting.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE,
                Util.BASE_URL + "/api/v1/user/logout",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        SharedPreferences sharedPreferences = getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();

                        Log.i("aaa",token);
                        alertDialog("로그아웃 완료","로그인 화면으로 이동합니다.");

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

    private void userAdios(){
        requestQueue = Volley.newRequestQueue(Setting.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE,
                Util.BASE_URL + "/api/v1/user/adios",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        SharedPreferences sharedPreferences = getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();

                        Log.i("aaa",token);
                        Log.i("aaa",sharedPreferences.getString("auto",null));

                        Intent i = new Intent(Setting.this,MainActivity.class);
                        startActivity(i);
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
        requestQueue.add(jsonObjectRequest);
    }

    void alertDialog(String title, String message){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Setting.this,R.style.myDialogTheme);
        alertDialog .setTitle(title);
        alertDialog .setMessage(message);
        alertDialog .setPositiveButton
                ("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Setting.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
        final AlertDialog dialog = alertDialog.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });
        alertDialog .setCancelable(false);
        alertDialog .show();
    }

    void alertDialog_adios(String title, String message){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Setting.this,R.style.myDialogTheme);
        alertDialog .setTitle(title);
        alertDialog .setMessage(message);
        alertDialog .setPositiveButton
                ("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        userAdios();


                    }
                });
        alertDialog .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
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
        alertDialog .setCancelable(false);
        alertDialog .show();
    }

}