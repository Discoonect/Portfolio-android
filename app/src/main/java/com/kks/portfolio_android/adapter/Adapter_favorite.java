package com.kks.portfolio_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.activity.CommentActivity;
import com.kks.portfolio_android.activity.PageActivity;
import com.kks.portfolio_android.activity.PostingActivity;
import com.kks.portfolio_android.model.Alram;
import com.kks.portfolio_android.model.Items;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Adapter_favorite extends RecyclerView.Adapter<Adapter_favorite.ViewHolder> {

    Context context;
    List<Items> itemsList;

    public Adapter_favorite(Context context, List<Items> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }
    @NonNull
    @Override
    public Adapter_favorite.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_row, parent, false);
        return new Adapter_favorite.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_favorite.ViewHolder holder, int position) {
        Items items = itemsList.get(position);

        if(items.getPhoto_url()!=null) {
            Glide.with(context).load(Util.IMAGE_PATH+items.getPhoto_url()).into(holder.ff_img_postImg);
        }else{
            holder.ff_img_postImg.setVisibility(View.GONE);
        }

        if(items.getUser_profilephoto()!=null){
            Glide.with(context).load(Util.IMAGE_PATH+items.getUser_profilephoto()).into(holder.ff_profile);
        }else{
            holder.ff_profile.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }

        holder.ff_txt_content.setText(items.getCreated_at());
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ff_profile;
        ImageView ff_img_postImg;
        TextView ff_txt_content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ff_profile = itemView.findViewById(R.id.ff_profile);
            ff_img_postImg = itemView.findViewById(R.id.ff_img_postImg);
            ff_txt_content = itemView.findViewById(R.id.ff_txt_content);

            ff_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Items items = itemsList.get(getBindingAdapterPosition());
                    Intent i = new Intent(context,PageActivity.class);
                    i.putExtra("user_id",items.getUser_id());
                    context.startActivity(i);
                }
            });

            ff_txt_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Items items = itemsList.get(getBindingAdapterPosition());
                    if(items.getComment()==null && items.getPost_id()==null){
                        Intent i = new Intent(context, PageActivity.class);
                        i.putExtra("user_id",items.getUser_id());
                        context.startActivity(i);
                    }else if(items.getPost_id()!=null && items.getComment()==null){
                        Intent i = new Intent(context, PostingActivity.class);
                        i.putExtra("post_id",items.getPost_id());
                        context.startActivity(i);
                    }else{
                        Intent i = new Intent(context, CommentActivity.class);
                        i.putExtra("post_id",items.getPost_id());
                        context.startActivity(i);
                    }
                }
            });
            
        }
    }
}