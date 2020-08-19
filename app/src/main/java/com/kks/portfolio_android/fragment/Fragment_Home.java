package com.kks.portfolio_android.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.adapter.RecyclerViewAdapter_home;
import com.kks.portfolio_android.model.Posting;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Home extends Fragment {

    RecyclerView recyclerView;
    RecyclerViewAdapter_home adapter_home;
    ArrayList<Posting> postArrayList = new ArrayList<>();

    RequestQueue requestQueue;

    String path = "/api/v1/post";

    String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment__home,container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


}