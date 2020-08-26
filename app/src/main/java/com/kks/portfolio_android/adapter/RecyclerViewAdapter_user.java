package com.kks.portfolio_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.model.Posting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RecyclerViewAdapter_user extends RecyclerView.Adapter<RecyclerViewAdapter_user.ViewHolder> {

        Context context;
        ArrayList<Posting> postArrayList;

public RecyclerViewAdapter_user(Context context, ArrayList<Posting> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
        }
@NonNull
@Override
public RecyclerViewAdapter_user.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.user_row, parent, false);
        return new RecyclerViewAdapter_user.ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull RecyclerViewAdapter_user.ViewHolder holder, int position) {


        }

@Override
public int getItemCount() {
        return postArrayList.size();
        }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}