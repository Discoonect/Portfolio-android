package com.kks.portfolio_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kks.portfolio_android.R;
import com.kks.portfolio_android.model.Posting;

import java.util.ArrayList;

public class RecyclerViewAdapter_favorite extends RecyclerView.Adapter<RecyclerViewAdapter_favorite.ViewHolder> {

    Context context;
    ArrayList<Posting> postArrayList;

    public RecyclerViewAdapter_favorite(Context context, ArrayList<Posting> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }
    @NonNull
    @Override
    public RecyclerViewAdapter_favorite.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_row, parent, false);
        return new RecyclerViewAdapter_favorite.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter_favorite.ViewHolder holder, int position) {


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