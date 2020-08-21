package com.kks.portfolio_android.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.adapter.RecyclerViewAdapter_home;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_Home extends Fragment {

    RecyclerView recyclerView;
    RecyclerViewAdapter_home adapter_home;

    JSONObject jsonObject = new JSONObject();
    ArrayList<Posting> postArrayList = new ArrayList<>();

    RequestQueue requestQueue;

    String path = "/api/v1/post/followerpost";
    int offset;

    String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment__home,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        postArrayList.clear();

        recyclerView = getView().findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager() )
//                        .findLastCompletelyVisibleItemPosition();
//                int totalCount = recyclerView.getAdapter().getItemCount();
//
//                if ((lastPosition + 1) == totalCount) {
//                    addNetworkData();
//                }
//            }
//        });

        SharedPreferences sharedPreferences =
                getActivity().getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
        Log.i("aaa",token);

        requestQueue = Volley.newRequestQueue(getActivity());

        getPostingData();
    }

    private void addNetworkData() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Util.BASE_URL + path + "?offset=" + offset + "&limit=25", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                Toast.makeText(getActivity(), "떙", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONArray items = response.getJSONArray("items");

                            for(int i=0; i<items.length(); i++){
                                jsonObject = items.getJSONObject(i);

                                int post_id = jsonObject.getInt("post_id");
                                String user_name = jsonObject.getString("user_name");
                                String content = jsonObject.getString("content");
                                String created_at = jsonObject.getString("created_at");
                                String photo = jsonObject.getString("photo_url");
                                String photo_url = Util.BASE_URL+"/public/uploads/"+photo;

                                Posting posting = new Posting(post_id,user_name,content,created_at,photo_url);
                                postArrayList.add(posting);

                            }
                            adapter_home.notifyDataSetChanged();
                            offset = offset + response.getInt("count");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> stringStringMap = new HashMap<String, String>();
                stringStringMap.put("Authorization","Bearer "+token);
                return stringStringMap;
            }
        };
        requestQueue.add(request);
    }


    private void getPostingData() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Util.BASE_URL + path + "?offset=" + offset + "&limit=25", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                Toast.makeText(getActivity(), "떙", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONArray items = response.getJSONArray("items");

                            for(int i=0; i<items.length(); i++){
                                jsonObject = items.getJSONObject(i);

                                int post_id = jsonObject.getInt("post_id");
                                String user_name = jsonObject.getString("user_name");
                                String content = jsonObject.getString("content");
                                String created_at = jsonObject.getString("created_at");
                                String photo = jsonObject.getString("photo_url");
                                String photo_url = Util.BASE_URL+"/public/uploads/"+photo;

                                Posting posting = new Posting(post_id,user_name,content,created_at,photo_url);
                                postArrayList.add(posting);

                            }
                            adapter_home = new RecyclerViewAdapter_home(getActivity(), postArrayList);
                            recyclerView.setAdapter(adapter_home);

                            offset = offset + response.getInt("count");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> stringStringMap = new HashMap<String, String>();
                stringStringMap.put("Authorization","Bearer "+token);
                return stringStringMap;
            }
        };
        requestQueue.add(request);
    }


}