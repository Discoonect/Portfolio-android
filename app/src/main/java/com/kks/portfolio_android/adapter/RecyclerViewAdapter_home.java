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

import com.kks.portfolio_android.MainActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        holder.txt_content.setText(posting.getContent());
        holder.txt_created.setText(posting.getCreatedAt());

        //시간 맞추기
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        //포스팅 작성시간 표시
        try {
            Date date = df.parse(posting.getCreatedAt());
            df.setTimeZone(TimeZone.getDefault());
            String strDate = df.format(date);
            holder.txt_created.setText(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //하트 누르면 하트이미지 바꾸기
        if(posting.getPostlike() == 1){
            holder.img_like.setImageResource(R.drawable.ic_baseline_favorite_24);
        }else {
            holder.img_like.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

    }
    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img_user;
        TextView txt_user_id;
        ImageView img_post;
        ImageView img_like;
        ImageView img_comment;
        TextView txt_content;
        TextView txt_comment;
        TextView txt_created;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_user = itemView.findViewById(R.id.img_user);
            txt_user_id = itemView.findViewById(R.id.txt_user_id);
            img_post = itemView.findViewById(R.id.img_post);
            img_like = itemView.findViewById(R.id.img_like);
            img_comment = itemView.findViewById(R.id.img_comment);
            txt_content = itemView.findViewById(R.id.txt_content);
            txt_comment = itemView.findViewById(R.id.txt_comment);
            txt_created = itemView.findViewById(R.id.txt_created);

        }
    }

}
















