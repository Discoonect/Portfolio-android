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
import com.kks.portfolio_android.retrofitmodel.Items;
import com.kks.portfolio_android.util.Util;

import java.util.ArrayList;
import java.util.List;

public class Adapter_user extends RecyclerView.Adapter<Adapter_user.ViewHolder> {

    Context context;
    List<Items> itemsList;

    public Adapter_user(Context context, List<Items> itemsArrayList) {
        this.context = context;
        this.itemsList = itemsArrayList;
    }

    @NonNull
    @Override
    public Adapter_user.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false);
        return new Adapter_user.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_user.ViewHolder holder, int position) {
        Items items = itemsList.get(position);

        Glide.with(context).load(Util.IMAGE_PATH+items.getPhoto_url()).into(holder.fu_img_postimg);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
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
                    int post_id = itemsList.get(position).getId();
                    int user_id = itemsList.get(position).getUser_id();

                    Intent i = new Intent(context, PostingActivity.class);
                    i.putExtra("post_id",post_id);
                    i.putExtra("user_id",user_id);
                    context.startActivity(i);
                }
            });
        }
    }
}