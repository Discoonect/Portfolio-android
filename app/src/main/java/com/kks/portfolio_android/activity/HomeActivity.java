package com.kks.portfolio_android.activity;

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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.fragment.Fragment_Favorite;
import com.kks.portfolio_android.fragment.Fragment_Home;
import com.kks.portfolio_android.fragment.Fragment_Search;
import com.kks.portfolio_android.fragment.Fragment_User;
import com.kks.portfolio_android.fragment.Fragment_Write;

public class HomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    Fragment_Home fragment_home;
    Fragment_Search fragment_search;
    Fragment_Write fragment_write;
    Fragment_Favorite fragment_favorite;
    Fragment_User fragment_user;

    BottomNavigationView bottomNavigationView;

    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        fragment_home = new Fragment_Home();
        fragment_search = new Fragment_Search();
        fragment_write = new Fragment_Write();
        fragment_favorite = new Fragment_Favorite();
        fragment_user = new Fragment_User();

        Toolbar toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        loadFragment(fragment_home);

        bottomNavigationView = findViewById(R.id.bottom_navi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;

                switch (item.getItemId()){
                    case R.id.fragment_Home:
                        fragment = fragment_home;
                        break;
                    case R.id.fragment_Search:
                        fragment = fragment_search;

                        break;
                    case R.id.fragment_Write:
                        fragment = fragment_write;
                        break;
                    case R.id.fragment_Favorite:
                        fragment = fragment_favorite;
                        break;
                    case R.id.fragment_User:
                        fragment = fragment_user;
                        break;
                }
                return loadFragment(fragment);
            }
        });


//        fragment_home = new Fragment_Home();
//        fragment_search = new Fragment_Search();
//        fragment_write = new Fragment_Write();
//        fragment_user = new Fragment_User();
//        fragment_favorite = new Fragment_Favorite();
//
//        transaction = getSupportFragmentManager().beginTransaction();
//        transaction.add(R.id.frameLayout,fragment_home.newInstance()).commit();
    }



    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
            return true;
        }
        return false;
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
}