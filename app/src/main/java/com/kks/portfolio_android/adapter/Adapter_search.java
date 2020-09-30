package com.kks.portfolio_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kks.portfolio_android.activity.PostingActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.retrofitmodel.Items;

import java.util.ArrayList;
import java.util.List;

public class Adapter_search extends RecyclerView.Adapter<Adapter_search.ViewHolder> {

    Context context;
    List<Items> itemsList;

    public Adapter_search(Context context, List<Items> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }
    @NonNull
    @Override
    public Adapter_search.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_row, parent, false);
        return new Adapter_search.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_search.ViewHolder holder, int position) {
        Items items = itemsList.get(position);

        Glide.with(context).load(items.getPhoto_url()).into(holder.fs_img_posting);

        holder.fs_img_likeCnt.setText(""+items.getCnt_like());
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView fs_img_posting;
        TextView fs_img_likeCnt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fs_img_posting = itemView.findViewById(R.id.fs_img_posting);
            fs_img_likeCnt = itemView.findViewById(R.id.fs_img_likeCnt);

            fs_img_posting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PostingActivity.class);
                    i.putExtra("post_id",itemsList.get(getBindingAdapterPosition()).getPost_id());
                    i.putExtra("user_id",itemsList.get(getBindingAdapterPosition()).getUser_id());

                    context.startActivity(i);
                }
            });
        }
    }
}