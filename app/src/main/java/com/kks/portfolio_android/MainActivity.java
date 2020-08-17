package com.kks.portfolio_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText main_edit_email;
    EditText main_edit_password;

    CheckBox auto_login_check;

    Button main_btn_login;
    Button main_btn_regist;
    Button main_btn_guest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_btn_guest = findViewById(R.id.main_btn_guest);
        main_btn_login = findViewById(R.id.main_btn_login);
        main_btn_regist = findViewById(R.id.main_btn_regist);

        main_edit_email = findViewById(R.id.main_edit_email);
        main_edit_password = findViewById(R.id.main_edit_password);

        auto_login_check = findViewById(R.id.auto_login_check);

        main_btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Sign_up.class);
                startActivity(i);
                finish();
                return;
            }
        });



    }
}