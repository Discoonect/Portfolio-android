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

import java.util.ArrayList;

public class Adapter_searchPosting extends RecyclerView.Adapter<Adapter_searchPosting.ViewHolder> {

    Context context;
    ArrayList<Posting> postArrayList;

    public Adapter_searchPosting(Context context, ArrayList<Posting> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }
    @NonNull
    @Override
    public Adapter_searchPosting.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_posting_row, parent, false);
        return new Adapter_searchPosting.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_searchPosting.ViewHolder holder, int position) {
        Posting posting = postArrayList.get(position);

        Glide.with(context).load(posting.getUser_name()).into(holder.sp_img_photo);

        holder.sp_txt_content.setText(posting.getUser_profilephoto());

    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView sp_img_photo;
        TextView sp_txt_content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sp_img_photo = itemView.findViewById(R.id.sp_img_photo);
            sp_txt_content = itemView.findViewById(R.id.sp_txt_content);
        }
    }
}