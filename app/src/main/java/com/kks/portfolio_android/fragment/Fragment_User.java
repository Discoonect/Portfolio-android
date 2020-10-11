package com.kks.portfolio_android.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.kks.portfolio_android.activity.MainActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.adapter.Adapter_user;
import com.kks.portfolio_android.api.NetworkClient;
import com.kks.portfolio_android.api.RetrofitApi;
import com.kks.portfolio_android.api.UserApi;
import com.kks.portfolio_android.follow.Follower_Activity;
import com.kks.portfolio_android.follow.Following_Activity;
import com.kks.portfolio_android.model.Items;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.res.UserRes;
import com.kks.portfolio_android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_User extends Fragment {
    TextView fu_txt_postingCnt;
    TextView fu_txt_followerCnt;
    TextView fu_txt_followingCnt;
    TextView fu_txt_userName;
    ImageView fu_img_profile;
    TextView fu_txt_introduce;

    RecyclerView recyclerView;

    List<Items> itemsList = new ArrayList<>();

    RetrofitApi retrofitApi = new RetrofitApi();

    Adapter_user adapter_user;

    String token;
    int offset;
    int user_id;
    int limit = 8;
    int cnt;


    public static Fragment_User newInstance(){
        return new Fragment_User();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment__user,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        offset=0;

        recyclerView = getView().findViewById(R.id.fu_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));

        SharedPreferences sharedPreferences =
                getContext().getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
        user_id = sharedPreferences.getInt("user_id",0);

        fu_txt_followerCnt = getView().findViewById(R.id.fu_txt_followerCnt);
        fu_txt_followingCnt = getView().findViewById(R.id.fu_txt_followingCnt);
        fu_txt_introduce = getView().findViewById(R.id.fu_txt_introduce);
        fu_img_profile = getView().findViewById(R.id.fu_img_profile);
        fu_txt_userName = getView().findViewById(R.id.fu_txt_userName);
        fu_txt_followerCnt = getView().findViewById(R.id.fu_txt_followerCnt);
        fu_txt_postingCnt = getView().findViewById(R.id.fu_txt_postingCnt);
        fu_txt_followingCnt = getView().findViewById(R.id.fu_txt_followingCnt);

        fu_txt_followerCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Follower_Activity.class);
                getContext().startActivity(i);
            }
        });

        fu_txt_followingCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Following_Activity.class);
                getContext().startActivity(i);
            }
        });

        retrofitApi.getMyPage1(getContext(),token,fu_txt_introduce,fu_img_profile,fu_txt_userName,fu_txt_followerCnt);
        retrofitApi.getMyPage2(getContext(),token,fu_txt_postingCnt,fu_txt_followingCnt);
        getPagePhoto(getContext(),user_id,limit,recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                if (lastPosition+1 == totalCount) {
                    Log.i("aaa","라스트 포지션 : "+lastPosition+ "   토탈 카운트 : "+totalCount);

                    if(cnt==limit){
                        addPagePhoto(getContext(),user_id,limit);
                        Log.i("aaa","토탈 : "+totalCount+ " 라스트 : "+lastPosition);
                    }else{
                        Toast.makeText(getContext(), "마지막 게시물입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void getPagePhoto(Context context, int user_id, int limit, RecyclerView recyclerView){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        UserApi userApi = retrofit.create(UserApi.class);

        Call<UserRes> userResCall = userApi.getPagePhoto(user_id,offset,limit);

        userResCall.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if(response.code()==200) {
                    List<Items> itemsList = response.body().getItems();
                    adapter_user = new Adapter_user(context,itemsList);
                    recyclerView.setAdapter(adapter_user);

                    offset = offset+response.body().getCnt();
                    cnt = response.body().getCnt();
                }
            }
            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {
            }
        });
    }

    public void addPagePhoto(Context context, int user_id, int limit){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        UserApi userApi = retrofit.create(UserApi.class);

        Call<UserRes> userResCall = userApi.getPagePhoto(user_id,offset,limit);

        userResCall.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if(response.code()==200) {
                    for(int i=0; i<response.body().getItems().size(); i++){
                        Items item = response.body().getItems().get(i);
                        int id = item.getId();
                        int user_id = item.getUser_id();
                        String photo_url = item.getPhoto_url();

                        Items items = new Items(id,user_id,photo_url);
                        itemsList.add(items);
                    }

                    adapter_user.notifyDataSetChanged();

                    offset = offset+response.body().getCnt();
                    cnt = response.body().getCnt();
                }
            }
            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {
            }
        });
    }
}