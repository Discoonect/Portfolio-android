package com.kks.portfolio_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kks.portfolio_android.util.Util;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;

    EditText main_edit_name;
    EditText main_edit_password;

    CheckBox auto_login_check;

    Button main_btn_login;
    Button main_btn_regist;
    Button main_btn_guest;

    String name;
    String passwd;

    String token="";
    String auto;

    File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_btn_guest = findViewById(R.id.main_btn_guest);
        main_btn_login = findViewById(R.id.main_btn_login);
        main_btn_regist = findViewById(R.id.main_btn_regist);

        main_edit_name = findViewById(R.id.main_edit_name);
        main_edit_password = findViewById(R.id.main_edit_password);

        auto_login_check = findViewById(R.id.auto_login_check);



        SharedPreferences sharedPreferences =
                getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
        auto = sharedPreferences.getString("auto",null);
        token = sharedPreferences.getString("token",null);

        if(auto!=null){
            if(auto.equals("on")){
                Intent i = new Intent(MainActivity.this, Home.class);
                startActivity(i);
                finish();
                return;
            }
        }




        main_btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Sign_up.class);
                startActivity(i);
                finish();
                return;
            }
        });

        main_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = main_edit_name.getText().toString().trim();
                passwd = main_edit_password.getText().toString().trim();

                if(name.isEmpty()||passwd.isEmpty()){
                    Toast.makeText(MainActivity.this, "이메일 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                login();
            }
        });
    }




    private void login(){
        JSONObject body = new JSONObject();
        try{
            body.put("user_name",name);
            body.put("user_passwd",passwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.POST, Util.BASE_URL + "/api/v1/user/login",body,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    boolean success = response.getBoolean("success");
                                    if(success==false){
                                        Toast.makeText(MainActivity.this, "문제있음", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    String token = response.getString("token");
                                    int user_id = response.getInt("user_id");

                                    SharedPreferences sp = getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("token",token);
                                    editor.putInt("user_id",user_id);
                                    Log.i("aaa",""+user_id);
                                    editor.apply();

                                    if(auto_login_check.isChecked()==true){
                                        editor.putString("auto","on");
                                        editor.apply();
                                    }else{
                                        editor.putString("auto","off");
                                        editor.apply();
                                    }

                                    Log.i("aaa",sp.getString("auto",null));

                                    Intent i = new Intent(MainActivity.this,Home.class);
                                    startActivity(i);
                                    finish();
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
}