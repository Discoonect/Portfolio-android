package com.kks.portfolio_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.api.VolleyApi;
import com.kks.portfolio_android.util.Util;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    EditText main_edit_name;
    EditText main_edit_password;

    CheckBox auto_login_check;

    Button main_btn_login;
    Button main_btn_regist;

    String name;
    String passwd;

    String token="";
    String auto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VolleyApi volleyApi = new VolleyApi();

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
                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
                return;
            }
        }

        main_btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Sign_upActivity.class);
                startActivity(i);
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
                volleyApi.login(name,passwd,MainActivity.this,auto_login_check);
            }
        });
    }
}