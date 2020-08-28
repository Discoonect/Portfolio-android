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

public class Adapter_user extends RecyclerView.Adapter<Adapter_user.ViewHolder> {

        Context context;
        ArrayList<Posting> postArrayList;

public Adapter_user(Context context, ArrayList<Posting> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
        }
@NonNull
@Override
public Adapter_user.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.user_row, parent, false);
        return new Adapter_user.ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull Adapter_user.ViewHolder holder, int position) {


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