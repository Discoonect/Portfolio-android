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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kks.portfolio_android.activity.MainActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.api.NetworkClient;
import com.kks.portfolio_android.api.PostApi;
import com.kks.portfolio_android.api.RetrofitApi;
import com.kks.portfolio_android.model.Items;
import com.kks.portfolio_android.res.PostRes;
import com.kks.portfolio_android.search.Search_PostingResult;
import com.kks.portfolio_android.search.Search_UserResult;
import com.kks.portfolio_android.adapter.Adapter_search;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_Search extends Fragment {

    ImageButton fs_img_search;
    TextView fs_edit_search;

    int offset;
    int limit = 25;
    int cnt;

    List<Items> itemsList;

    Adapter_search adapter_search;
    RecyclerView recyclerView;

    String token;

    public static Fragment_Search newInstance(){
        return new Fragment_Search();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment__search,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        offset=0;

        SharedPreferences sharedPreferences =
                getActivity().getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);

        if (token == null) {
            Toast.makeText(getContext(), "로그인을 해주세요", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(), MainActivity.class);
            startActivity(i);
            getActivity().finish();
        }

        recyclerView = getView().findViewById(R.id.fs_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));

        fs_img_search = getView().findViewById(R.id.fs_img_search);
        fs_edit_search = getView().findViewById(R.id.fs_edit_search);

        getBestPost(getContext(),limit,recyclerView);

        fs_img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Search_PostingResult.class);
                Intent j = new Intent(getContext(), Search_UserResult.class );

                String keyword = fs_edit_search.getText().toString().trim();
                String keyword_search = keyword.substring(0,1);
                Log.i("aaa",keyword_search);

                if(keyword_search.equals("@")){
                    j.putExtra("keyword",keyword);
                    getContext().startActivity(j);
                }else{
                    i.putExtra("keyword",keyword);
                    getContext().startActivity(i);
                }
                fs_edit_search.setText("");
            }
        });

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
                    if(cnt==limit){
                        addBestPost(getContext(),limit);
                    }else{
                        Toast.makeText(getContext(), "마지막 게시물입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void getBestPost(Context context,int limit, RecyclerView recyclerView){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        PostApi postApi = retrofit.create(PostApi.class);
        Call<PostRes> postResCall = postApi.bestPost(offset,limit);
        postResCall.enqueue(new Callback<PostRes>() {
            @Override
            public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                Log.i("aaa",response.toString());
                if(response.isSuccessful()) {
                    itemsList = response.body().getItems();
                    adapter_search = new Adapter_search(context,itemsList);
                    recyclerView.setAdapter(adapter_search);

                    offset = offset+response.body().getCnt();
                    cnt = response.body().getCnt();
                }
            }

            @Override
            public void onFailure(Call<PostRes> call, Throwable t) {
            }
        });
    }

    public void addBestPost(Context context,int limit){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        PostApi postApi = retrofit.create(PostApi.class);
        Call<PostRes> postResCall = postApi.bestPost(offset,limit);
        postResCall.enqueue(new Callback<PostRes>() {
            @Override
            public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                Log.i("aaa",response.toString());
                if(response.code()==200) {

                    for(int i=0; i<response.body().getItems().size(); i++){
                        Items item = response.body().getItems().get(i);

                        int post_id = item.getPost_id();
                        int user_id = item.getUser_id();
                        String photo_url = item.getPhoto_url();
                        int cnt_like = item.getCnt_like();

                        Items items = new Items(post_id,user_id,photo_url,cnt_like);
                        itemsList.add(items);
                    }

                    adapter_search.notifyDataSetChanged();

                    offset = offset+response.body().getCnt();
                    cnt = response.body().getCnt();
                }
            }
            @Override
            public void onFailure(Call<PostRes> call, Throwable t) {
            }
        });
    }

//    private void getFamousPosting(){
//        requestQueue = Volley.newRequestQueue(getContext());
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
//                Util.BASE_URL + "/api/v1/post/bestpost?offset="+offset+"&limit=30",
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        try {
//                            boolean success = response.getBoolean("success");
//                            if (success == false) {
//                                Toast.makeText(getActivity(), "다시 시도 해주세요.", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//
//                            JSONArray items = response.getJSONArray("items");
//
//                            for(int i=0; i<items.length();i++){
//                                jsonObject = items.getJSONObject(i);
//
//                                int user_id = jsonObject.getInt("user_id");
//
//                                 int like_cnt = jsonObject.getInt("cnt_like");
//                                int post_id = jsonObject.getInt("post_id");
//                                String photo = jsonObject.getString("photo_url");
//                                String photo_url = Util.BASE_URL+"/public/uploads/"+photo;
//
//                                Posting posting = new Posting(post_id,user_id,photo_url,like_cnt);
//                                postingArrayList.add(posting);
//                            }
//
//                            adapter_search = new Adapter_search(getActivity(),postingArrayList);
//                            recyclerView.setAdapter(adapter_search);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                }
//        );
//        requestQueue.add(jsonObjectRequest);
//    }

}