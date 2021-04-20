package com.kks.portfolio_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.kks.portfolio_android.activity.CommentActivity;
import com.kks.portfolio_android.activity.PageActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.activity.PostLikeUserActivity;
import com.kks.portfolio_android.activity.RevisePostingActivity;
import com.kks.portfolio_android.api.RetrofitApi;
import com.kks.portfolio_android.model.Items;
import com.kks.portfolio_android.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

public class Adapter_home extends RecyclerView.Adapter<Adapter_home.ViewHolder> {

    Context context;
    List<Items> itemsList;

    public Adapter_home(Context context, List<Items> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Items items = itemsList.get(position);

        holder.fh_txt_userId.setText(items.getUser_name());
        holder.fh_txt_content.setText(items.getContent());
        holder.fh_txt_cntFavorite.setText(""+items.getLike_cnt()+"명이 좋아합니다");
        holder.fh_txt_cntComment.setText("댓글 "+items.getComment_cnt()+"개");

        //시간 맞추기
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        //포스팅 작성시간 표시
        try {
            Date date = df.parse(items.getCreated_at());
            df.setTimeZone(TimeZone.getDefault());
            String strDate = df.format(date);
            holder.fh_txt_created.setText(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //하트 누르면 하트이미지 바꾸기
        if(items.getMylike() == 1){
            holder.fh_img_like.setImageResource(R.drawable.ic_baseline_favorite_24);
        }else {
            holder.fh_img_like.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

        Glide.with(context).load(Util.IMAGE_PATH+items.getPhoto_url()).into(holder.fh_img_postPhoto);

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
        int sp_user_id = sharedPreferences.getInt("user_id",0);
        String token = sharedPreferences.getString("token",null);

        if(sp_user_id==items.getUser_id()){
            holder.fh_img_menu.setVisibility(View.VISIBLE);
        }else{
            holder.fh_img_menu.setVisibility(View.INVISIBLE);
        }

        holder.fh_img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(context,holder.fh_img_menu);
                popupMenu.inflate(R.menu.fh_post_menu);

                int post_id = itemsList.get(position).getPost_id();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.fh_menu_edit:
                                Intent i = new Intent(context, RevisePostingActivity.class);
                                i.putExtra("post_id",post_id);
                                context.startActivity(i);
                                return true;

                            case R.id.fh_menu_delete:
                                RetrofitApi retrofitApi = new RetrofitApi();
                                retrofitApi.deletePost(context,token,post_id);
                                itemsList.remove(position);
                                notifyDataSetChanged();
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }

        });

        if(items.getUser_profilephoto()!=null){
            Glide.with(context).load(Util.IMAGE_PATH+items.getUser_profilephoto()).into(holder.fh_img_profilePhoto);
        }else{
            holder.fh_img_profilePhoto.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView fh_img_profilePhoto;
        TextView fh_txt_userId;
        ImageView fh_img_postPhoto;
        ImageView fh_img_like;
        ImageView fh_img_comment;
        TextView fh_txt_content;
        TextView fh_txt_created;
        TextView fh_txt_cntComment;
        TextView fh_txt_cntFavorite;
        ImageView fh_img_menu;

        RetrofitApi retrofitApi = new RetrofitApi();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fh_img_profilePhoto = itemView.findViewById(R.id.fh_img_profilePhoto);
            fh_txt_userId = itemView.findViewById(R.id.fh_txt_userId);
            fh_img_postPhoto = itemView.findViewById(R.id.fh_img_postPhoto);
            fh_img_like = itemView.findViewById(R.id.fh_img_like);
            fh_img_comment = itemView.findViewById(R.id.fh_img_comment);
            fh_txt_content = itemView.findViewById(R.id.fh_txt_content);
            fh_txt_created = itemView.findViewById(R.id.fh_txt_created);
            fh_txt_cntComment = itemView.findViewById(R.id.fh_txt_cntComment);
            fh_txt_cntFavorite = itemView.findViewById(R.id.fh_txt_cntFavorite);
            fh_img_menu = itemView.findViewById(R.id.fh_img_menu);

            fh_img_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getBindingAdapterPosition();
                    int is_like = itemsList.get(position).getMylike();

                    SharedPreferences sp = context.getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
                    String token = sp.getString("token", null);

                    Items items = itemsList.get(position);

                    if(is_like==1){
                        int post_id = items.getPost_id();
                        retrofitApi.clickDislike(context,post_id,token,fh_txt_cntFavorite);
                        items.setMylike(0);
                        notifyDataSetChanged();
                    }else{
                        int post_id = items.getPost_id();
                        retrofitApi.clickLike(context,post_id,token,fh_txt_cntFavorite);
                        items.setMylike(1);
                        notifyDataSetChanged();
                    }
                }
            });

            fh_img_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getBindingAdapterPosition();
                    Items items = itemsList.get(position);
                    int post_id = items.getPost_id();

                    Intent i = new Intent(context, CommentActivity.class);
                    i.putExtra("post_id",post_id);

                    Intent j = new Intent(context,Adapter_comment.class);
                    j.putExtra("post_id",post_id);

                    context.startActivity(i);
                }
            });

            fh_txt_cntComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getBindingAdapterPosition();
                    Items items = itemsList.get(position);
                    int post_id = items.getPost_id();

                    Intent i = new Intent(context,CommentActivity.class);
                    i.putExtra("post_id",post_id);

                    context.startActivity(i);
                }
            });

            fh_img_profilePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PageActivity.class);
                    i.putExtra("user_id",itemsList.get(getBindingAdapterPosition()).getUser_id());
                    context.startActivity(i);
                }
            });

            fh_txt_cntFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int post_id = itemsList.get(getBindingAdapterPosition()).getPost_id();

                    Intent i = new Intent(context, PostLikeUserActivity.class);
                    i.putExtra("post_id",post_id);
                    context.startActivity(i);
                }
            });
        }

        private void clickLike(int post_id,int position,String token) {
            JSONObject body = new JSONObject();
            try {
                body.put("post_id", post_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Util.BASE_URL + "/api/v1/like/likepost",
                    body,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // 어레이리스트의 값을 변경시켜줘야 한다.
                            Items items = itemsList.get(position);
                            items.setMylike(1);
                            notifyDataSetChanged();
                            getLikeCntData(items,post_id);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }
            )  {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", "Bearer " + token);
                    return params;
                }
            } ;
            Volley.newRequestQueue(context).add(request);

        }

        private void clickDislike(int post_id, int position,String token) {
            JSONObject body = new JSONObject();
            try {
                body.put("post_id", post_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Util.BASE_URL + "/api/v1/like/deletelikepost",
                    body,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // 어레이리스트의 값을 변경시켜줘야 한다.
                            Items items = itemsList.get(position);
                            items.setMylike(0);
                            notifyDataSetChanged();
                            getLikeCntData(items,post_id);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("aaa",error.toString());
                        }
                    }
            )  {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", "Bearer " + token);
                    return params;
                }
            } ;
            Volley.newRequestQueue(context).add(request);

        }

        private void getLikeCntData(Items items,int cnt_like_postid) {
            int position = getBindingAdapterPosition();

            JsonObjectRequest request1 = new JsonObjectRequest(
                    Request.Method.GET, Util.BASE_URL + "/api/v1/like/countlikepost/" + cnt_like_postid,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int likecnt = response.getInt("cnt");

                                items.setLike_cnt(likecnt);
                                notifyDataSetChanged();

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
            Volley.newRequestQueue(context).add(request1);
        }
    }
}


