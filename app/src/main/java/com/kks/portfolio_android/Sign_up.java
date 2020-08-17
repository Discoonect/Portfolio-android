package com.kks.portfolio_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class Sign_up extends AppCompatActivity {

    RequestQueue requestQueue;

    EditText signup_edit_email;
    EditText signup_edit_password1;
    EditText signup_edit_password2;
    EditText signup_edit_phone;

    Button signup_btn_signup;
    Button signup_btn_cancle;

    int offset;
    String email;
    String password1;
    String password2;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signup_edit_email = findViewById(R.id.signup_edit_email);
        signup_edit_password1 = findViewById(R.id.singup_edit_password1);
        signup_edit_password2 = findViewById(R.id.singup_edit_password2);
        signup_edit_phone = findViewById(R.id.singup_edit_phone);

        signup_btn_cancle = findViewById(R.id.signup_btn_cancle);
        signup_btn_signup = findViewById(R.id.signup_btn_signup);



        signup_btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Sign_up.this,MainActivity.class);
                startActivity(i);
                finish();
                return;
            }
        });

        signup_btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = signup_edit_email.getText().toString().trim();
                password1 = signup_edit_password1.getText().toString().trim();
                password2 = signup_edit_password2.getText().toString().trim();
                phone = signup_edit_phone.getText().toString().trim();

                if(email.contains("@") == false){
                    Toast.makeText(Sign_up.this, "이메일 형식이 올바르지 않습니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password1.length() < 4 || password1.length() > 12){
                    Toast.makeText(Sign_up.this, "비밀번호 길이는 4자리 이상,12자리 이하입니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password1.equalsIgnoreCase(password2) == false){
                    Toast.makeText(Sign_up.this, "비밀번호가 일치하지 않습니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                signUpData();
            }
        });
    }

    private void signUpData(){
        JSONObject body = new JSONObject();
        try{
            body.put("user_email",email);
            body.put("user_passwd",password1);
            body.put("user_phone",phone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(Sign_up.this);
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.POST,Util.BASE_URL + "/api/v1/user",body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(Sign_up.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                        Log.i("aaa",response.toString());
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