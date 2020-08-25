package com.kks.portfolio_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kks.portfolio_android.MainActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.fragment.Fragment_Home;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RecyclerViewAdapter_home extends RecyclerView.Adapter<RecyclerViewAdapter_home.ViewHolder> {

    Context context;
    ArrayList<Posting> postArrayList;

    public RecyclerViewAdapter_home(Context context, ArrayList<Posting> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Posting posting = postArrayList.get(position);

        holder.fh_txt_userId.setText(posting.getUser_name());
        holder.fh_txt_content.setText(posting.getContent());
        holder.fh_txt_created.setText(posting.getCreatedAt());

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fh_img_profilePhoto = itemView.findViewById(R.id.fh_img_profilePhoto);
            fh_txt_userId = itemView.findViewById(R.id.fh_txt_userId);
            fh_img_postPhoto = itemView.findViewById(R.id.fh_img_postPhoto);
            fh_img_like = itemView.findViewById(R.id.fh_img_like);
            fh_img_comment = itemView.findViewById(R.id.fh_img_comment);
            fh_txt_content = itemView.findViewById(R.id.fh_txt_content);
            fh_txt_created = itemView.findViewById(R.id.fh_txt_created);

            fh_img_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    int is_like = postArrayList.get(position).getPostlike();

                    if(is_like==1){

                    }else{

                    }
                }
            });

        }
    }
}