package com.kks.portfolio_android.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kks.portfolio_android.activity.MainActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.adapter.Adapter_favorite;
import com.kks.portfolio_android.api.AlarmApi;
import com.kks.portfolio_android.api.NetworkClient;
import com.kks.portfolio_android.api.VolleyApi;
import com.kks.portfolio_android.model.Alram;
import com.kks.portfolio_android.model.Items;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.res.AlarmRes;
import com.kks.portfolio_android.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_Favorite extends Fragment {

    String token;
    int user_id;

    int offset;
    int limit = 25;


    Button ff_btn_posting;
    Button ff_btn_comment;
    Button ff_btn_follow;
    TextView ff_textView;

    Adapter_favorite adapter_favorite;

    List<Items> itemsList = new ArrayList<>();

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
        ff_textView = getView().findViewById(R.id.ff_textView);

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

        postlike(getContext(),ff_textView,recyclerView);

        ff_btn_posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postlike(getContext(),ff_textView,recyclerView);
            }
        });

        ff_btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentAlarm(getContext());
            }
        });

        ff_btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmFollow(getContext());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void postlike(Context context,TextView ff_textView,RecyclerView recyclerView){
        itemsList.clear();
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        AlarmApi alarmApi = retrofit.create(AlarmApi.class);

        Call<AlarmRes> alarmResCall = alarmApi.postlike(token,offset,limit);


        alarmResCall.enqueue(new Callback<AlarmRes>() {
            @Override
            public void onResponse(Call<AlarmRes> call, Response<AlarmRes> response) {
                itemsList = response.body().getItems();

                //시간 맞추기
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                df.setTimeZone(TimeZone.getTimeZone("UTC"));

                //포스팅 작성시간 표시
                try {
                    for(int i=0;i<itemsList.size();i++){
                        Date date = df.parse(itemsList.get(i).getCreated_at());
                        df.setTimeZone(TimeZone.getDefault());
                        String strDate = df.format(date);
                        String content = itemsList.get(i).getUser_name()+" 님이 게시물을 좋아합니다.\n"+strDate;

                        itemsList.get(i).setCreated_at(content);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                adapter_favorite = new Adapter_favorite(context,itemsList);
                recyclerView.setAdapter(adapter_favorite);

                if(response.body().getCnt()==0){
                    ff_textView.setVisibility(View.VISIBLE);
                    ff_textView.setText("좋아요를 누른 사람이 없어요!");
                    recyclerView.setVisibility(View.GONE);
                }else{
                    ff_textView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AlarmRes> call, Throwable t) {

            }
        });
    }

    public void commentAlarm(Context context){
        itemsList.clear();
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        AlarmApi alarmApi = retrofit.create(AlarmApi.class);

        Call<AlarmRes> alarmResCall = alarmApi.comment(token,offset,limit);


        alarmResCall.enqueue(new Callback<AlarmRes>() {
            @Override
            public void onResponse(Call<AlarmRes> call, Response<AlarmRes> response) {
                itemsList = response.body().getItems();

                //시간 맞추기
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                df.setTimeZone(TimeZone.getTimeZone("UTC"));

                //포스팅 작성시간 표시
                try {
                    for(int i=0;i<itemsList.size();i++){
                        Date date = df.parse(itemsList.get(i).getCreated_at());
                        df.setTimeZone(TimeZone.getDefault());
                        String strDate = df.format(date);
                        String content = itemsList.get(i).getUser_name()+" 님이 댓글을 달았습니다.\n"+itemsList.get(i).getComment()+"\n"+strDate;

                        itemsList.get(i).setCreated_at(content);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                adapter_favorite = new Adapter_favorite(context,itemsList);
                recyclerView.setAdapter(adapter_favorite);

                if(response.body().getCnt()==0){
                    ff_textView.setVisibility(View.VISIBLE);
                    ff_textView.setText("댓글 작성한 사람이 없어요!");
                    recyclerView.setVisibility(View.GONE);
                }else{
                    ff_textView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AlarmRes> call, Throwable t) {

            }
        });
    }

    public void alarmFollow(Context context){
        itemsList.clear();
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        AlarmApi alarmApi = retrofit.create(AlarmApi.class);

        Call<AlarmRes> alarmResCall = alarmApi.follow(token,offset,limit);
        alarmResCall.enqueue(new Callback<AlarmRes>() {
            @Override
            public void onResponse(Call<AlarmRes> call, Response<AlarmRes> response) {
                itemsList = response.body().getItems();

                //시간 맞추기
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                df.setTimeZone(TimeZone.getTimeZone("UTC"));

                //포스팅 작성시간 표시
                try {
                    for(int i=0;i<itemsList.size();i++){
                        Date date = df.parse(itemsList.get(i).getCreated_at());
                        df.setTimeZone(TimeZone.getDefault());
                        String strDate = df.format(date);
                        String content = itemsList.get(i).getUser_name()+" 님이 회원님을 팔로우 했습니다. \n"+strDate;

                        itemsList.get(i).setCreated_at(content);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                adapter_favorite = new Adapter_favorite(context,itemsList);
                recyclerView.setAdapter(adapter_favorite);

                if(response.body().getCnt()==0){
                    ff_textView.setVisibility(View.VISIBLE);
                    ff_textView.setText("팔로우 한 사람이 없어요!");
                    recyclerView.setVisibility(View.GONE);
                }else{
                    ff_textView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AlarmRes> call, Throwable t) {

            }
        });
    }
}