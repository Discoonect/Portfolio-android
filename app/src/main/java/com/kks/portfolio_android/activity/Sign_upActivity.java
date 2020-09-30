package com.kks.portfolio_android.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.alertDialog.CustomAlertDialog;
import com.kks.portfolio_android.api.NetworkClient;
import com.kks.portfolio_android.api.RetrofitApi;
import com.kks.portfolio_android.api.UserApi;
import com.kks.portfolio_android.retrofitmodel.user.UserReq;
import com.kks.portfolio_android.retrofitmodel.user.UserRes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Sign_upActivity extends AppCompatActivity {

    RequestQueue requestQueue;

    File photoFile;
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

    RetrofitApi retrofitApi = new RetrofitApi();

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
                    Toast.makeText(Sign_upActivity.this, R.string.please_check_id, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password1.length() < 4 || password1.length() > 12) {
                    Toast.makeText(Sign_upActivity.this, R.string.please_check_password_length,Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password1.equalsIgnoreCase(password2) == false) {
                    Toast.makeText(Sign_upActivity.this, R.string.incorrect_password,Toast.LENGTH_SHORT).show();
                    return;
                }

                if(phone.isEmpty()){
                    Toast.makeText(Sign_upActivity.this, R.string.please_insert_phoneNumber,Toast.LENGTH_SHORT).show();
                    return;
                }


                if(photoFile==null){
                    retrofitApi.signUp(Sign_upActivity.this,name,password1,phone);
                }else{
                    retrofitApi.signUpWithProfile(Sign_upActivity.this,name,password1,phone,photoFile);
                }
            }
        });
    }

    public void checkName(String name,Context context){
        CustomAlertDialog customAlertDialog = new CustomAlertDialog();
        UserReq userReq = new UserReq(name);

        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        UserApi userApi = retrofit.create(UserApi.class);

        Call<UserRes> userResCall = userApi.checkName(userReq);
        userResCall.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {

                if(response.isSuccessful()) {
                    String message = response.body().getMessage();
                    customAlertDialog.alertDialog_checked(message,context);
                    check_name=1;
                }else{
                    customAlertDialog.alertDialog_Unchecked("이미 사용중인 아이디 입니다",context);
                    check_name=0;
                }

            }
            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {

            }
        });
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(Sign_upActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_DENIED){
            return true;
        }else{
            return false;
        }
    }

    private void requestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(Sign_upActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(Sign_upActivity.this, R.string.need_accept_authority,Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 100 && resultCode == RESULT_OK){
            Bitmap photo = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

            ExifInterface exif = null;
            try {
                exif = new ExifInterface(photoFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            photo = rotateBitmap(photo, orientation);
            signup_img_profile.setImageBitmap(photo);
        }else if(requestCode == 300 && resultCode == RESULT_OK && data != null &&
                data.getData() != null){
            Uri imgPath = data.getData();
            Log.i("AAA", imgPath.toString());
            signup_img_profile.setImageURI(imgPath);

            // 실제 경로를 몰라도, 파일의 내용을 읽어와서, 임시파일 만들어서 서버로 보낸다.
//            String id = DocumentsContract.getDocumentId(imgPath);
            String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            try {
                InputStream inputStream = getContentResolver().openInputStream(imgPath);
                photoFile = new File(getCacheDir().getAbsolutePath()+"/"+fileName+".jpg");
                writeFile(inputStream, photoFile);
//                String filePath = photoFile.getAbsolutePath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void writeFile(InputStream in, File file) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if ( out != null ) {
                    out.close();
                }
                in.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


}