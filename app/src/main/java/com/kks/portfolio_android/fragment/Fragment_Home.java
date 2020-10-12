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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.kks.portfolio_android.activity.MainActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.adapter.Adapter_home;
import com.kks.portfolio_android.api.NetworkClient;
import com.kks.portfolio_android.api.PostApi;
import com.kks.portfolio_android.api.RetrofitApi;
import com.kks.portfolio_android.model.Items;
import com.kks.portfolio_android.res.PostRes;
import com.kks.portfolio_android.util.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_Home extends Fragment {

    RecyclerView recyclerView;

    List<Items> itemsList = new ArrayList<>();

    int offset;
    int limit=6;
    int cnt;

    TextView fh_textView;

    Adapter_home adapter_home;
    String token;

    public static Fragment_Home newInstance(){
        return new Fragment_Home();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment__home,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        offset=0;

        recyclerView = getView().findViewById(R.id.fh_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fh_textView = getView().findViewById(R.id.fh_textView);

        SharedPreferences sharedPreferences =
                getActivity().getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);

        if (token == null) {
            Toast.makeText(getContext(), "로그인을 해주세요", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(), MainActivity.class);
            startActivity(i);
            getActivity().finish();
        }

        getPostingData(getContext(),token,limit,recyclerView);

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
                        addPostData(getContext(),token,limit);
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

    public void getPostingData(Context context, String token,int limit,RecyclerView recyclerView){

        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        PostApi postApi = retrofit.create(PostApi.class);

        Call<PostRes> postResCall = postApi.getAllPost(token,offset,limit);

        postResCall.enqueue(new Callback<PostRes>() {
            @Override
            public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                if(response.code()==200) {
                    Log.i("aaa","cnt : "+response.body().getCnt()+"  offset : "+offset);
                    itemsList = response.body().getItems();
                    adapter_home = new Adapter_home(context,itemsList);
                    recyclerView.setAdapter(adapter_home);

                    offset = offset+response.body().getCnt();
                    cnt = response.body().getCnt();

                    if(cnt==0){
                        fh_textView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else{
                        recyclerView.setVisibility(View.VISIBLE);
                        fh_textView.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onFailure(Call<PostRes> call, Throwable t) {
            }
        });
    }

    public void addPostData(Context context, String token,int limit){
        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        PostApi postApi = retrofit.create(PostApi.class);

        Call<PostRes> postResCall = postApi.getAllPost(token,offset,limit);

        postResCall.enqueue(new Callback<PostRes>() {
            @Override
            public void onResponse(Call<PostRes> call, Response<PostRes> response) {
                if(response.code()==200) {
                    Log.i("aaa",response.toString());
                    Log.i("aaa","cnt : "+response.body().getCnt()+"   offset : "+offset+"   limit : "+limit);

                    for(int i=0; i<response.body().getItems().size(); i++){
                        Items item = response.body().getItems().get(i);
                        int post_id = item.getPost_id();
                        int user_id = item.getUser_id();
                        String photo_url = item.getPhoto_url();
                        String content = item.getContent();
                        String created_at = item.getCreated_at();
                        String user_profilephoto = item.getUser_profilephoto();
                        String user_name = item.getUser_name();
                        int mylike = item.getMylike();
                        int comment_cnt = item.getComment_cnt();
                        int like_cnt = item.getLike_cnt();

                        Items items = new Items(post_id,user_id,photo_url,content,created_at,user_profilephoto,
                                user_name,mylike,comment_cnt,like_cnt);
                        itemsList.add(items);
                    }

                    adapter_home.notifyDataSetChanged();

                    offset = offset + response.body().getCnt();
                    cnt = response.body().getCnt();


                }
            }

            @Override
            public void onFailure(Call<PostRes> call, Throwable t) {

            }
        });
    }

//    private void addNetworkData() {
//        JsonObjectRequest request = new JsonObjectRequest(
//                Request.Method.GET,
//                Util.BASE_URL + path + "?offset=" + offset + "&limit=25", null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try{
//                            boolean success = response.getBoolean("success");
//                            if (success == false) {
//                                Toast.makeText(getActivity(), "떙", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//
//                            JSONArray items = response.getJSONArray("items");
//
//                            for(int i=0; i<items.length(); i++){
//                                jsonObject = items.getJSONObject(i);
//
//                                int post_id = jsonObject.getInt("post_id");
//                                int user_id = jsonObject.getInt("user_id");
//                                String user_name = jsonObject.getString("user_name");
//                                String content = jsonObject.getString("content");
//                                String created_at = jsonObject.getString("created_at");
//                                int like_cnt = jsonObject.getInt("like_cnt");
//                                int comment_cnt = jsonObject.getInt("comment_cnt");
//                                int postlike = jsonObject.getInt("mylike");
//
//                                String photo = jsonObject.getString("photo_url");
//                                String photo_url = Util.BASE_URL+"/public/uploads/"+photo;
//
//                                Posting posting = new Posting(post_id,user_id,user_name,content,created_at,photo_url,comment_cnt,like_cnt,postlike);
//                                postArrayList.add(posting);
//
//                            }
//                            adapter_home.notifyDataSetChanged();
//                            offset = offset + response.getInt("count");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                    }
//                }
//        )
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//
//                Map<String, String> stringStringMap = new HashMap<String, String>();
//                stringStringMap.put("Authorization","Bearer "+token);
//                return stringStringMap;
//            }
//        };
//        requestQueue.add(request);
//    }

//    public void getPostingData(Context context,String token) {
//        JsonObjectRequest request = new JsonObjectRequest(
//                Request.Method.GET,Util.BASE_URL + path + "?offset=" + offset + "&limit=25", null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.i("aaa",response.toString());
//
//                        try{
//                            boolean success = response.getBoolean("success");
//                            if (success == false) {
//                                Toast.makeText(getActivity(), "떙", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//
//                            JSONArray items = response.getJSONArray("items");
//
//                            for(int i=0; i<items.length(); i++){
//                                jsonObject = items.getJSONObject(i);
//
//                                int post_id = jsonObject.getInt("post_id");
//                                int user_id = jsonObject.getInt("user_id");
//                                String user_name = jsonObject.getString("user_name");
//                                String content = jsonObject.getString("content");
//                                String created_at = jsonObject.getString("created_at");
//                                int like_cnt = jsonObject.getInt("like_cnt");
//                                int comment_cnt = jsonObject.getInt("comment_cnt");
//                                int postlike = jsonObject.getInt("mylike");
//
//                                String profile = jsonObject.getString("user_profilephoto");
//
//
//                                String photo = jsonObject.getString("photo_url");
//                                String photo_url = Util.BASE_URL+"/public/uploads/"+photo;
//
//                                Posting posting = new Posting(post_id,user_id,user_name,profile,content,created_at,photo_url,comment_cnt,like_cnt,postlike);
//
//                                postArrayList.add(posting);
//                            }
//                            adapter_home = new Adapter_home(getActivity(), postArrayList);
//                            recyclerView.setAdapter(adapter_home);
//
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                    }
//                }
//        )
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> stringStringMap = new HashMap<String, String>();
//                stringStringMap.put("Authorization","Bearer "+token);
//                return stringStringMap;
//            }
//        };
//        Volley.newRequestQueue(context).add(request);
//    }
}