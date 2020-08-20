package com.kks.portfolio_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import org.json.JSONException;
import org.json.JSONObject;

public class Sign_up extends AppCompatActivity {

    RequestQueue requestQueue;

    EditText signup_edit_email;
    EditText signup_edit_password1;
    EditText signup_edit_password2;
    EditText signup_edit_phone;
    EditText signup_edit_nickname;

    Button signup_btn_signup;
    Button signup_btn_cancle;
    Button signup_btn_checkNickname;

    int offset;
    String email;
    String password1;
    String password2;
    String phone;
    String nickname;

    boolean check_nickname=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signup_edit_email = findViewById(R.id.signup_edit_email);
        signup_edit_password1 = findViewById(R.id.singup_edit_password1);
        signup_edit_password2 = findViewById(R.id.singup_edit_password2);
        signup_edit_phone = findViewById(R.id.singup_edit_phone);
        signup_edit_nickname = findViewById(R.id.signup_edit_nickname);

        signup_btn_cancle = findViewById(R.id.signup_btn_cancle);
        signup_btn_signup = findViewById(R.id.signup_btn_signup);
        signup_btn_checkNickname = findViewById(R.id.signup_btn_checkNickname);


        signup_btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Sign_up.this, MainActivity.class);
                startActivity(i);
                finish();
                return;
            }
        });

        signup_btn_checkNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkNicknameData();
            }
        });

        signup_btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = signup_edit_email.getText().toString().trim();
                password1 = signup_edit_password1.getText().toString().trim();
                password2 = signup_edit_password2.getText().toString().trim();
                phone = signup_edit_phone.getText().toString().trim();
                nickname = signup_edit_nickname.getText().toString().trim();

                if (!check_nickname) {
                    Toast.makeText(Sign_up.this, "닉네임 중복체크를 해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (email.contains("@") == false) {
                    Toast.makeText(Sign_up.this, "이메일 형식이 올바르지 않습니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password1.length() < 4 || password1.length() > 12) {
                    Toast.makeText(Sign_up.this, "비밀번호 길이는 4자리 이상,12자리 이하입니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password1.equalsIgnoreCase(password2) == false) {
                    Toast.makeText(Sign_up.this, "비밀번호가 일치하지 않습니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(check_nickname==false) {
                    Toast.makeText(Sign_up.this, "닉네임 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.i("aaa","닉네임  "+check_nickname);
                signUpData();
            }
        });
    }

    private void signUpData() {
        JSONObject body = new JSONObject();
        try {
            body.put("user_email", email);
            body.put("user_passwd", password1);
            body.put("user_phone", phone);
            body.put("user_nickname", nickname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("aaa","회원가입 body  : "+body.toString());
        requestQueue = Volley.newRequestQueue(Sign_up.this);
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.POST, Util.BASE_URL + "/api/v1/user", body,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(Sign_up.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Sign_up.this, MainActivity.class);
                                startActivity(i);
                                finish();
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

    private void checkNicknameData() {
        nickname = signup_edit_nickname.getText().toString().trim();
        JSONObject body = new JSONObject();
        try {
            body.put("user_nickname", nickname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(Sign_up.this);
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.POST,
                        Util.BASE_URL + "/api/v1/user/checknickname", body,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    boolean success = response.getBoolean("success");
                                    String message = response.getString("message");
                                    if (success) {
                                        alertDialog_checked(message);
                                    } else {
                                        alertDialog_Unchecked(message);
                                    }
                                    check_nickname=success;
                                    Log.i("aaa","success  "+success);
                                    Log.i("aaa","닉네임  "+check_nickname);
                                    return;
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

    void alertDialog_checked(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Sign_up.this, R.style.myDialogTheme);
        alertDialog.setTitle("닉네임 중복체크");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton
                ("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        check_nickname = true;
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

    void alertDialog_Unchecked(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Sign_up.this, R.style.myDialogTheme);
        alertDialog.setTitle("닉네임 중복체크");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton
                ("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        check_nickname = false;
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
}