package com.kks.portfolio_android.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.search.Search_PostingResult;
import com.kks.portfolio_android.search.Search_UserResult;
import com.kks.portfolio_android.adapter.Adapter_search;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Fragment_Search extends Fragment {

    ImageButton fs_img_search;
    TextView fs_edit_search;

    ArrayList<Posting> postingArrayList = new ArrayList<>();

    int offset;

    RequestQueue requestQueue;
    JSONObject jsonObject;

    Adapter_search adapter_search;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment__search,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        postingArrayList.clear();

        recyclerView = getView().findViewById(R.id.fs_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));

        fs_img_search = getView().findViewById(R.id.fs_img_search);
        fs_edit_search = getView().findViewById(R.id.fs_edit_search);

        getFamousPosting();

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
    }

    private void getFamousPosting() {
        requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Util.BASE_URL + "/api/v1/post/bestpost?offset="+offset+"&limit=10",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            boolean success = response.getBoolean("success");
                            if (success == false) {
                                Toast.makeText(getActivity(), "다시 시도 해주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONArray items = response.getJSONArray("items");

                            for(int i=0; i<items.length();i++){
                                jsonObject = items.getJSONObject(i);

                                int like_cnt = jsonObject.getInt("cnt_like");
                                int post_id = jsonObject.getInt("post_id");
                                String photo = jsonObject.getString("photo_url");
                                String photo_url = Util.BASE_URL+"/public/uploads/"+photo;

                                Posting posting = new Posting(post_id,photo_url,like_cnt);
                                postingArrayList.add(posting);
                            }

                            adapter_search = new Adapter_search(getActivity(),postingArrayList);
                            recyclerView.setAdapter(adapter_search);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

}