package com.kks.portfolio_android.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kks.portfolio_android.activity.MainActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.api.VolleyApi;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_Favorite extends Fragment {

    String token;
    int user_id;

    int offset;
    int limit = 25;

    Button ff_btn_posting;
    Button ff_btn_comment;
    Button ff_btn_follow;


    VolleyApi volleyApi = new VolleyApi();

    ArrayList<Posting> postArrayList = new ArrayList<>();

    RecyclerView recyclerView;

    public static Fragment_Favorite newInstance(){
        return new Fragment_Favorite();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment__favorite,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ff_btn_posting = getView().findViewById(R.id.ff_btn_posting);
        ff_btn_comment = getView().findViewById(R.id.ff_btn_comment);
        ff_btn_follow = getView().findViewById(R.id.ff_btn_follow);


        recyclerView = getView().findViewById(R.id.ff_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPreferences sharedPreferences =
                getActivity().getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
        user_id = sharedPreferences.getInt("user_id",user_id);

        if (token == null) {
            Toast.makeText(getContext(), "로그인을 해주세요", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(), MainActivity.class);
            startActivity(i);
            getActivity().finish();
        }

        volleyApi.postLikeAlarm(getContext(),offset,limit,token,recyclerView);

        ff_btn_posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volleyApi.postLikeAlarm(getContext(),offset,limit,token,recyclerView);
            }
        });

        ff_btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volleyApi.postCommentAlarm(getContext(),offset,limit,token,recyclerView);
            }
        });

        ff_btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volleyApi.followAlram(getContext(),offset,limit,token,recyclerView);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

    }
}