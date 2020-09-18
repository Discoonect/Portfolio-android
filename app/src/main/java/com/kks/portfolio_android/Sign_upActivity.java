package com.kks.portfolio_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kks.portfolio_android.api.VolleyApi;
import com.kks.portfolio_android.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class Sign_upActivity extends AppCompatActivity {

    RequestQueue requestQueue;


    EditText signup_edit_password1;
    EditText signup_edit_password2;
    EditText signup_edit_phone;
    EditText signup_edit_id;

    Button signup_btn_signup;
    Button signup_btn_cancle;
    Button signup_btn_checkId;
    Button signup_btn_gallery;

    ImageView signup_img_profile;

    int offset;
    String name;
    String password1;
    String password2;
    String phone;

    VolleyApi volleyApi = new VolleyApi();

    int check_name=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        signup_edit_password1 = findViewById(R.id.singup_edit_password1);
        signup_edit_password2 = findViewById(R.id.singup_edit_password2);
        signup_edit_phone = findViewById(R.id.singup_edit_phone);
        signup_edit_id = findViewById(R.id.signup_edit_id);

        signup_btn_cancle = findViewById(R.id.signup_btn_cancle);
        signup_btn_signup = findViewById(R.id.signup_btn_signup);
        signup_btn_checkId = findViewById(R.id.signup_btn_checkId);
        signup_btn_gallery = findViewById(R.id.signup_btn_gallery);

        signup_img_profile = findViewById(R.id.signup_img_profile);


        signup_btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        signup_btn_checkId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = signup_edit_id.getText().toString().trim();
                checkName(name,Sign_upActivity.this);
                Log.i("aaa",""+check_name);
            }
        });

        signup_btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= 23){
                    if(checkPermission()){
                        displayFileChoose();
                    }else{
                        requestPermission();
                    }
                }
            }
        });

        signup_btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password1 = signup_edit_password1.getText().toString().trim();
                password2 = signup_edit_password2.getText().toString().trim();
                phone = signup_edit_phone.getText().toString().trim();
                name= signup_edit_id.getText().toString().trim();

                if (check_name==0) {
                    Toast.makeText(Sign_upActivity.this, "아이디 중복체크를 해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password1.length() < 4 || password1.length() > 12) {
                    Toast.makeText(Sign_upActivity.this, "비밀번호 길이는 4자리 이상,12자리 이하입니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password1.equalsIgnoreCase(password2) == false) {
                    Toast.makeText(Sign_upActivity.this, "비밀번호가 일치하지 않습니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(phone.isEmpty()){
                    Toast.makeText(Sign_upActivity.this, "핸드폰 번호를 입력해주세요.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                volleyApi.signUp(name,password1,phone,Sign_upActivity.this);
            }
        });
    }

    private void requestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(Sign_upActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(Sign_upActivity.this, "권한 수락이 필요합니다.",
                    Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(Sign_upActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 500);
        }
    }

    private void displayFileChoose() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "SELECT IMAGE"), 300);
    }

    public void checkName(String name,Context context){
        JSONObject body = new JSONObject();
        try {
            body.put("user_name", name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.POST,
                        Util.BASE_URL + "/api/v1/user/checkid", body,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    boolean success = response.getBoolean("success");
                                    String message = response.getString("message");

                                    if (success) {
                                        volleyApi.alertDialog_checked(message,context);
                                        check_name=1;
                                    } else {
                                        volleyApi.alertDialog_Unchecked(message,context);
                                        check_name=0;
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
                );
        requestQueue.add(request);
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(Sign_upActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_DENIED){
            return true;
        }else{
            return false;
        }
    }
}