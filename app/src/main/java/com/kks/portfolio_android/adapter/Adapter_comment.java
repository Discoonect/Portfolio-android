package com.kks.portfolio_android.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kks.portfolio_android.CommentActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.model.Comments;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.VISIBLE;

public class Adapter_comment extends RecyclerView.Adapter<Adapter_comment.ViewHolder> {

    Context context;
    ArrayList<Comments> commentArrayList;

    public Adapter_comment(Context context, ArrayList<Comments> commentArrayList) {
        this.context = context;
        this.commentArrayList = commentArrayList;
    }

    @NonNull
    @Override
    public Adapter_comment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_comment.ViewHolder holder, int position) {
        Comments comments = commentArrayList.get(position);

        holder.cm_txt_name.setText(comments.getUser_name());
        holder.cm_txt_comment.setText(comments.getComment());
        holder.cm_img_profile.setImageResource(R.drawable.ic_baseline_account_circle_24);

        //시간 맞추기
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        //포스팅 작성시간 표시
        try {
            Date date = df.parse(comments.getCreated_at());
            df.setTimeZone(TimeZone.getDefault());
            String strDate = df.format(date);
            holder.cm_txt_time.setText(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
        int sp_user_id = sharedPreferences.getInt("user_id",1);

        if(sp_user_id!=commentArrayList.get(position).getUser_id()){
            holder.cm_img_delete.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView cm_img_profile;
        TextView cm_txt_name;
        TextView cm_txt_comment;
        TextView cm_txt_time;
        ImageView cm_img_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cm_img_profile = itemView.findViewById(R.id.cm_img_profile);
            cm_txt_name = itemView.findViewById(R.id.cm_txt_name);
            cm_txt_comment = itemView.findViewById(R.id.cm_txt_comment);
            cm_txt_time = itemView.findViewById(R.id.cm_txt_time);
            cm_img_delete = itemView.findViewById(R.id.cm_img_delete);
        }
    }
}