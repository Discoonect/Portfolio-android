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
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Adapter_favorite extends RecyclerView.Adapter<Adapter_favorite.ViewHolder> {

    Context context;
    ArrayList<Alram> alramArrayList;

    public Adapter_favorite(Context context, ArrayList<Alram> alramArrayList) {
        this.context = context;
        this.alramArrayList = alramArrayList;
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
        Alram alram = alramArrayList.get(position);


        if(alram.getPhoto()!=null) {
            Glide.with(context).load(alram.getPhoto()).into(holder.ff_img_postImg);
        }else{
            holder.ff_img_postImg.setVisibility(View.GONE);
        }

        if(alram.getProfile()!="null"){
            Glide.with(context).load(Util.IMAGE_PATH+alram.getProfile()).into(holder.ff_profile);
        }else{
            holder.ff_profile.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }

        holder.ff_txt_content.setText(alram.getContent());

    }

    @Override
    public int getItemCount() {
        return alramArrayList.size();
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
                    Alram alram = alramArrayList.get(getBindingAdapterPosition());
                    Intent i = new Intent(context,PageActivity.class);
                    i.putExtra("user_id",alram.getUser_id());
                    context.startActivity(i);
                }
            });

            ff_txt_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Alram alram = alramArrayList.get(getBindingAdapterPosition());
                    if(alram.getStatus()==1){
                        Intent i = new Intent(context, PostingActivity.class);
                        i.putExtra("post_id",alram.getId());
                        context.startActivity(i);
                    }else if(alram.getStatus()==2){
                        Intent i = new Intent(context, CommentActivity.class);
                        i.putExtra("post_id",alram.getId());
                        context.startActivity(i);
                    }else if(alram.getStatus()==3){
                        Intent i = new Intent(context, PageActivity.class);
                        i.putExtra("user_id",alram.getId());
                        context.startActivity(i);
                    }
                }
            });
            
        }
    }
}