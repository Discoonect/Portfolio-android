package com.kks.portfolio_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.kks.portfolio_android.R;
import com.kks.portfolio_android.api.RetrofitApi;
import com.kks.portfolio_android.util.Util;

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

        RetrofitApi retrofitApi = new RetrofitApi();

        main_btn_login = findViewById(R.id.main_btn_login);
        main_btn_regist = findViewById(R.id.main_btn_regist);

        main_edit_name = findViewById(R.id.main_edit_name);
        main_edit_password = findViewById(R.id.main_edit_password);

        auto_login_check = findViewById(R.id.auto_login_check);



//        SharedPreferences sharedPreferences =
//                getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
//        auto = sharedPreferences.getString("auto",null);
//        token = sharedPreferences.getString("token",null);

        if(auto!=null){
            if(auto.equals(Util.AUTO_LOGIN_ON)){
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
                Log.i("aaa",name+passwd);

                if(name.isEmpty()||passwd.isEmpty()){
                    Toast.makeText(MainActivity.this, R.string.please_enter_email_password, Toast.LENGTH_SHORT).show();
                    return;
                }
                retrofitApi.login(MainActivity.this,name,passwd,auto_login_check);
            }
        });
    }
}