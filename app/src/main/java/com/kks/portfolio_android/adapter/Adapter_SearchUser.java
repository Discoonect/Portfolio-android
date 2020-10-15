package com.kks.portfolio_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kks.portfolio_android.activity.PageActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import java.util.ArrayList;

public class Adapter_SearchUser extends RecyclerView.Adapter<Adapter_SearchUser.ViewHolder> {

    Context context;
    ArrayList<Posting> postArrayList;

    public Adapter_SearchUser(Context context, ArrayList<Posting> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public Adapter_SearchUser.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_user_row, parent, false);
        return new Adapter_SearchUser.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_SearchUser.ViewHolder holder, int position) {
        Posting posting = postArrayList.get(position);

        holder.su_txt_userName.setText(posting.getUser_name());

        if(posting.getUser_profilephoto()=="null"){
            holder.su_img_profile.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }else{
            Glide.with(context).load(Util.IMAGE_PATH+posting.getUser_profilephoto()).into(holder.su_img_profile);
        }
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView su_img_profile;
        TextView su_txt_userName;
        LinearLayout su_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            su_img_profile = itemView.findViewById(R.id.su_img_profile);
            su_txt_userName = itemView.findViewById(R.id.su_txt_userName);
            su_layout = itemView.findViewById(R.id.su_layout);

            su_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int user_id = postArrayList.get(getBindingAdapterPosition()).getUser_id();

                    Intent i = new Intent(context, PageActivity.class);
                    i.putExtra("user_id",user_id);
                    context.startActivity(i);
                }
            });
        }
    }
}