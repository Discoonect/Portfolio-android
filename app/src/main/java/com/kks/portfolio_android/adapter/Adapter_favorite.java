package com.kks.portfolio_android.adapter;

import android.content.Context;
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
    ArrayList<Alram> postArrayList;

    public Adapter_favorite(Context context, ArrayList<Alram> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
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
        Alram alram = postArrayList.get(position);

        Glide.with(context).load(alram.getPhoto()).into(holder.ff_img_postImg);
        Log.i("aaa",alram.getPhoto());

        if(alram.getProfile()!="null"){
            Glide.with(context).load(Util.BASE_URL+"/public/uploads/"+alram.getProfile()).into(holder.ff_profile);
        }else{
            holder.ff_profile.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }

        holder.ff_txt_content.setText(alram.getContent());

    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
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
            
        }
    }
}