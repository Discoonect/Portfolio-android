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
import com.kks.portfolio_android.PageActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import java.util.ArrayList;

public class Adapter_follow extends RecyclerView.Adapter<Adapter_follow.ViewHolder> {

    Context context;
    ArrayList<Posting> postArrayList;

    public Adapter_follow(Context context, ArrayList<Posting> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }
    @NonNull
    @Override
    public Adapter_follow.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.follow_row, parent, false);
        return new Adapter_follow.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_follow.ViewHolder holder, int position) {
        Posting posting = postArrayList.get(position);

        if(posting.getUser_profilephoto()!="null"){
            Glide.with(context).load(Util.BASE_URL+"/public/uploads/"+posting.getUser_profilephoto()).into(holder.follow_img_profile);
        }else{
            holder.follow_img_profile.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }

        holder.follow_txt_userName.setText(posting.getUser_name());

    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView follow_img_profile;
        TextView follow_txt_userName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            follow_img_profile = itemView.findViewById(R.id.follow_img_profile);
            follow_txt_userName = itemView.findViewById(R.id.follow_txt_userName);

            follow_img_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int user_id = postArrayList.get(getAdapterPosition()).getUser_id();

                    Intent i = new Intent(context, PageActivity.class);
                    i.putExtra("user_id",user_id);
                    Log.i("aaa","user_id : "+user_id);
                    context.startActivity(i);
                }
            });

            follow_txt_userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int user_id = postArrayList.get(getAdapterPosition()).getUser_id();

                    Intent i = new Intent(context, PageActivity.class);
                    i.putExtra("user_id",user_id);
                    Log.i("aaa","user_id : "+user_id);
                    context.startActivity(i);
                }
            });
        }
    }
}