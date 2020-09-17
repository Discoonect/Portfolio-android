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
import com.kks.portfolio_android.CommentActivity;
import com.kks.portfolio_android.PageActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

public class Adapter_home extends RecyclerView.Adapter<Adapter_home.ViewHolder> {

    Context context;
    ArrayList<Posting> postArrayList;

    public Adapter_home(Context context, ArrayList<Posting> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Posting posting = postArrayList.get(position);

        holder.fh_txt_userId.setText(posting.getUser_name());
        holder.fh_txt_content.setText(posting.getContent());
        holder.fh_txt_created.setText(posting.getCreatedAt());
        holder.fh_txt_cntFavorite.setText(""+posting.getCnt_favorite()+"명이 좋아합니다");
        holder.fh_txt_cntComment.setText("댓글 "+posting.getCnt_comment()+"개");

        //시간 맞추기
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        //포스팅 작성시간 표시
        try {
            Date date = df.parse(posting.getCreatedAt());
            df.setTimeZone(TimeZone.getDefault());
            String strDate = df.format(date);
            holder.fh_txt_created.setText(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //하트 누르면 하트이미지 바꾸기
        if(posting.getPostlike() == 1){
            holder.fh_img_like.setImageResource(R.drawable.ic_baseline_favorite_24);
        }else {
            holder.fh_img_like.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

        Glide.with(context).load(posting.getPhoto_url()).into(holder.fh_img_postPhoto);

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
        int sp_user_id = sharedPreferences.getInt("user_id",0);
        String token = sharedPreferences.getString("token",null);

        if(sp_user_id==posting.getUser_id()){
            holder.fh_img_menu.setVisibility(View.VISIBLE);
        }else{
            holder.fh_img_menu.setVisibility(View.INVISIBLE);
        }

        holder.fh_img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(context,holder.fh_img_menu);
                popupMenu.inflate(R.menu.fh_post_menu);

                int post_id = postArrayList.get(position).getId();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.fh_menu_edit:
                                Toast.makeText(context, "수정", Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.fh_menu_delete:
                                deletePosting(post_id,token);
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }

            private void deletePosting(int post_id,String token) {
                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST, Util.BASE_URL + "/api/v1/post/deletepost/"+post_id,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(context, "포스팅 삭제 성공", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
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
                Volley.newRequestQueue(context).add(request);
            }

        });

        if(posting.getUser_profilephoto()!="null"){
            Glide.with(context).load(Util.BASE_URL+"/public/uploads/"+posting.getUser_profilephoto()).into(holder.fh_img_profilePhoto);
        }else{
            holder.fh_img_profilePhoto.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }

    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
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

                    int position = getAdapterPosition();
                    int is_like = postArrayList.get(position).getPostlike();
                    String token;

                    SharedPreferences sp = context.getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
                    token = sp.getString("token", null);

                    if(is_like==1){
                        clickDislike(position,token);
                    }else{
                        clickLike(position,token);
                    }
                }
            });

            fh_img_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Posting posting = postArrayList.get(position);
                    int post_id = posting.getId();

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
                    int position = getAdapterPosition();
                    Posting posting = postArrayList.get(position);
                    int post_id = posting.getId();

                    Intent i = new Intent(context,CommentActivity.class);
                    i.putExtra("post_id",post_id);

                    context.startActivity(i);
                }
            });

            fh_img_profilePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PageActivity.class);
                    i.putExtra("user_id",postArrayList.get(getAdapterPosition()).getUser_id());
                    context.startActivity(i);
                }
            });
        }

        private void clickLike(int position,String token) {
            Posting posting = postArrayList.get(position);
            int cnt_like_postid = postArrayList.get(position).getId();
            int posting_id = posting.getId();
            JSONObject body = new JSONObject();
            try {
                body.put("post_id", posting_id);

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
                            Posting post = postArrayList.get(position);
                            post.setPostlike(1);
                            notifyDataSetChanged();
                            getLikeCntData(post,cnt_like_postid);
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

        private void clickDislike(int position,String token) {
            int cnt_like_postid = postArrayList.get(position).getId();
            Posting posting = postArrayList.get(position);
            int posting_id = posting.getId();

            JSONObject body = new JSONObject();
            try {
                body.put("post_id", posting_id);
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
                            Posting post = postArrayList.get(position);
                            post.setPostlike(0);
                            notifyDataSetChanged();
                            getLikeCntData(post,cnt_like_postid);
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

        private void getLikeCntData(Posting post,int cnt_like_postid) {
            int position = getAdapterPosition();

            JsonObjectRequest request1 = new JsonObjectRequest(
                    Request.Method.GET, Util.BASE_URL + "/api/v1/like/countlikepost/" + cnt_like_postid,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int likecnt = response.getInt("cnt");

                                post.setCnt_favorite(likecnt);
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


