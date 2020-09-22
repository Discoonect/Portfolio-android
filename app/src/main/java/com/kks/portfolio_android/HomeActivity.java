package com.kks.portfolio_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.kks.portfolio_android.fragment.Fragment_Favorite;
import com.kks.portfolio_android.fragment.Fragment_Home;
import com.kks.portfolio_android.fragment.Fragment_Search;
import com.kks.portfolio_android.fragment.Fragment_User;
import com.kks.portfolio_android.fragment.Fragment_Write;

public class HomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private Fragment_Home fragment_home;
    private Fragment_Search fragment_search;
    private Fragment_Write fragment_write;
    private Fragment_User fragment_user;
    private Fragment_Favorite fragment_favorite;

    private ImageView fs_img_search;

    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        fragment_home = new Fragment_Home();
        fragment_search = new Fragment_Search();
        fragment_write = new Fragment_Write();
        fragment_user = new Fragment_User();
        fragment_favorite = new Fragment_Favorite();

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frameLayout,fragment_home.newInstance()).commit();
    }

    public void replaceFragment(Fragment fragment){
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout,fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.action_settings){
            Intent i = new Intent(this, SettingActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickHandler(View view){
        switch (view.getId()){
            case R.id.btn_fragment_home:
                replaceFragment(fragment_home);
                break;

            case R.id.btn_fragment_search:
                replaceFragment(fragment_search);
                break;

            case R.id.btn_fragment_write:
                replaceFragment(fragment_write);
                break;

            case R.id.btn_fragment_favorite:
                replaceFragment(fragment_favorite);
                break;

            case R.id.btn_fragment_user:
                replaceFragment(fragment_user);
                break;
        }
    }
}