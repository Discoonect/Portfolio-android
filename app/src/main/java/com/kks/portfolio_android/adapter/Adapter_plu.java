package com.kks.portfolio_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.activity.PageActivity;
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

public class Adapter_plu extends RecyclerView.Adapter<Adapter_plu.ViewHolder> {

    Context context;
    List<Items> itemsList;

    public Adapter_plu(Context context, List<Items> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }
    @NonNull
    @Override
    public Adapter_plu.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.postlikeuser_row, parent, false);
        return new Adapter_plu.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_plu.ViewHolder holder, int position) {
        Items items = itemsList.get(position);

        holder.plu_userName.setText(items.getUser_name());

        //시간 맞추기
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        //포스팅 작성시간 표시
        try {
            Date date = df.parse(items.getPostliketime());
            df.setTimeZone(TimeZone.getDefault());
            String strDate = df.format(date);
            holder.plu_time.setText(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(items.getUser_profilephoto()!=null){
            Glide.with(context).load(Util.BASE_URL+Util.IMAGE_PATH+items.getUser_profilephoto()).into(holder.plu_profile);
        }else{
            holder.plu_profile.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }



    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView plu_layout;
        ImageView plu_profile;
        TextView plu_userName;
        TextView plu_time;
        TextView plu_txt_cnt;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            plu_layout = itemView.findViewById(R.id.plu_layout);
            plu_profile = itemView.findViewById(R.id.plu_profile);
            plu_userName = itemView.findViewById(R.id.plu_userName);
            plu_time = itemView.findViewById(R.id.plu_time);
            plu_txt_cnt = itemView.findViewById(R.id.plu_txt_cnt);

            plu_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int user_id = itemsList.get(getBindingAdapterPosition()).getId();
                    Intent i = new Intent(context, PageActivity.class);
                    i.putExtra("user_id",user_id);
                    context.startActivity(i);
                }
            });

            plu_userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int user_id = itemsList.get(getBindingAdapterPosition()).getId();
                    Intent i = new Intent(context, PageActivity.class);
                    i.putExtra("user_id",user_id);
                    context.startActivity(i);
                }
            });
        }
    }
}