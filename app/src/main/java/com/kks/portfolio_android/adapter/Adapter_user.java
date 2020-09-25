package com.kks.portfolio_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kks.portfolio_android.activity.PostingActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.model.Posting;

import java.util.ArrayList;

public class Adapter_user extends RecyclerView.Adapter<Adapter_user.ViewHolder> {

    Context context;
    ArrayList<Posting> postArrayList;

    public Adapter_user(Context context, ArrayList<Posting> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public Adapter_user.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false);
        return new Adapter_user.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_user.ViewHolder holder, int position) {
        Posting posting = postArrayList.get(position);

        Glide.with(context).load(posting.getPhoto_url()).into(holder.fu_img_postimg);
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView fu_img_postimg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fu_img_postimg=itemView.findViewById(R.id.fu_img_postimg);

            fu_img_postimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getBindingAdapterPosition();
                    int post_id = postArrayList.get(position).getId();
                    int user_id = postArrayList.get(position).getUser_id();

                    Intent i = new Intent(context, PostingActivity.class);
                    i.putExtra("post_id",post_id);
                    i.putExtra("user_id",user_id);
                    Log.i("aaa","post_id : "+post_id+"     user_id : "+user_id);
                    context.startActivity(i);
                }
            });
        }
    }
}