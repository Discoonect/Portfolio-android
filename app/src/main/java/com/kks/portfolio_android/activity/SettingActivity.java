package com.kks.portfolio_android.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.api.NetworkClient;
import com.kks.portfolio_android.api.RetrofitApi;
import com.kks.portfolio_android.api.UserApi;
import com.kks.portfolio_android.api.VolleyApi;
import com.kks.portfolio_android.res.UserRes;
import com.kks.portfolio_android.util.Util;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class SettingActivity extends AppCompatActivity {

    Button setting_btn_leaveMember;
    Button setting_btn_logout;
    Button setting_btn_gallery;
    Button setting_btn_basicPhoto;
    Button setting_btn_save;

    ImageView setting_img_back;
    ImageView setting_img_profile;

    EditText setting_edit_introduce;

    TextView setting_txt_userName;

    RequestQueue requestQueue;

    File photoFile;

    RetrofitApi retrofitApi = new RetrofitApi();
    VolleyApi volleyApi = new VolleyApi();

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        SharedPreferences sharedPreferences =
                getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
        int user_id = sharedPreferences.getInt("user_id",0);

        if (token == null) {
            Toast.makeText(this, R.string.please_login, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }

        setting_btn_leaveMember = findViewById(R.id.setting_btn_leaveMember);
        setting_btn_logout = findViewById(R.id.setting_btn_logout);
        setting_btn_gallery = findViewById(R.id.setting_btn_gallery);
        setting_btn_basicPhoto = findViewById(R.id.setting_btn_basicPhoto);
        setting_btn_save = findViewById(R.id.setting_btn_save);
        setting_edit_introduce = findViewById(R.id.setting_edit_introduce);
        setting_img_profile = findViewById(R.id.setting_img_profile);
        setting_img_back = findViewById(R.id.setting_img_back);
        setting_txt_userName = findViewById(R.id.setting_txt_userName);

        retrofitApi.getUserPage1(SettingActivity.this,user_id,setting_img_profile,setting_txt_userName,null,setting_edit_introduce);

        setting_btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrofitApi.userLogout(SettingActivity.this,SettingActivity.this,token);
            }
        });

        setting_btn_leaveMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog_adios(R.string.adios_member,R.string.adios_are_you_sure,token);
            }
        });

        setting_img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setting_btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 23){
                    if(checkPermission()){
                        displayFileChoose();
                    }else{
                        requestPermission();
                    }
                }
            }
        });

        setting_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String introduce = setting_edit_introduce.getText().toString().trim();

                if(photoFile == null){
                    retrofitApi.writeIntroduce(SettingActivity.this,token,introduce);
                }else{
                    retrofitApi.uploadProfile(SettingActivity.this,photoFile,token,introduce);
                }
            }
        });

        setting_btn_basicPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrofitApi.deleteProfilePhoto(SettingActivity.this,token,user_id,setting_img_profile,
                        setting_txt_userName,setting_edit_introduce);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();




    }

    public static void alertDialog(Context context, Activity activity, int title, int message){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context,R.style.myDialogTheme);
        alertDialog .setTitle(title);
        alertDialog .setMessage(message);
        alertDialog .setPositiveButton
                ("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(context,MainActivity.class);
                        ActivityCompat.finishAffinity(activity);
                        context.startActivity(i);
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

    void alertDialog_adios(int title, int message, String token){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingActivity.this,R.style.myDialogTheme);
        alertDialog .setTitle(title);
        alertDialog .setMessage(message);
        alertDialog .setPositiveButton
                (R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        retrofitApi.userAdios(SettingActivity.this,token);
                    }
                });
        alertDialog .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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

    private void displayFileChoose() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "SELECT IMAGE"), 300);
    }

    private void requestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(this, R.string.need_accept_authority,Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 500);
        }
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_DENIED){
            return true;
        }else{
            return false;
        }
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
            setting_img_profile.setImageBitmap(photo);
        }else if(requestCode == 300 && resultCode == RESULT_OK && data != null &&
                data.getData() != null){
            Uri imgPath = data.getData();
            Log.i("AAA", imgPath.toString());
            setting_img_profile.setImageURI(imgPath);

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