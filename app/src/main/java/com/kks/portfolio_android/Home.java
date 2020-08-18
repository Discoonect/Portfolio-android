package com.kks.portfolio_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.kks.portfolio_android.fragment.Fragment_Favorite;
import com.kks.portfolio_android.fragment.Fragment_Home;
import com.kks.portfolio_android.fragment.Fragment_Search;
import com.kks.portfolio_android.fragment.Fragment_Setting;
import com.kks.portfolio_android.fragment.Fragment_Write;

public class Home extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private Fragment_Home fragment_home;
    private Fragment_Search fragment_search;
    private Fragment_Write fragment_write;
    private Fragment_Setting fragment_setting;
    private Fragment_Favorite fragment_favorite;

    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentManager = getSupportFragmentManager();

        fragment_home = new Fragment_Home();
        fragment_search = new Fragment_Search();
        fragment_write = new Fragment_Write();
        fragment_setting = new Fragment_Setting();
        fragment_favorite = new Fragment_Favorite();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout,fragment_home).commitAllowingStateLoss();
    }

    public void clickHandler(View view){

        transaction = fragmentManager.beginTransaction();

        switch (view.getId()){
            case R.id.btn_fragment_home:
                transaction.replace(R.id.frameLayout,fragment_home).commitAllowingStateLoss();
                break;

            case R.id.btn_fragment_search:
                transaction.replace(R.id.frameLayout,fragment_search).commitAllowingStateLoss();
                break;

            case R.id.btn_fragment_write:
                transaction.replace(R.id.frameLayout,fragment_write).commitAllowingStateLoss();
                break;

            case R.id.btn_fragment_favorite:
                transaction.replace(R.id.frameLayout,fragment_favorite).commitAllowingStateLoss();
                break;

            case R.id.btn_fragment_setting:
                transaction.replace(R.id.frameLayout,fragment_setting).commitAllowingStateLoss();
                break;
        }

    }
}