package com.kks.portfolio_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.kks.portfolio_android.activity.PageActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.api.VolleyApi;
import com.kks.portfolio_android.model.Comments;
import com.kks.portfolio_android.model.Items;
import com.kks.portfolio_android.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.VISIBLE;

public class Adapter_comment extends RecyclerView.Adapter<Adapter_comment.ViewHolder> {

    Context context;
    List<Items> itemsList;

    public Adapter_comment(Context context, List<Items> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
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
        Items items = itemsList.get(position);
        Log.i("aaa","어댑터: 1");
        holder.cm_txt_name.setText(items.getUser_name());
        holder.cm_txt_comment.setText(items.getComment());

        //시간 맞추기
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Log.i("aaa","어댑터: 2");
        //포스팅 작성시간 표시
        try {
            Date date = df.parse(items.getCreated_at());
            df.setTimeZone(TimeZone.getDefault());
            String strDate = df.format(date);
            holder.cm_txt_time.setText(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("aaa","어댑터: 3");
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
        int sp_user_id = sharedPreferences.getInt("user_id",0);

        if(sp_user_id == itemsList.get(position).getUser_id() || sp_user_id == itemsList.get(position).getPost_user_id()){
            holder.cm_img_delete.setVisibility(VISIBLE);
        }else{
            holder.cm_img_delete.setVisibility(View.GONE);
        }
        Log.i("aaa","어댑터: 4");
        if(items.getUser_profilephoto()!=null){
            Glide.with(context).load(Util.IMAGE_PATH+items.getUser_profilephoto()).into(holder.cm_img_profile);
        }else{
            holder.cm_img_profile.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }
        Log.i("aaa","어댑터: 6");
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
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

            cm_img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    VolleyApi volleyApi = new VolleyApi();

                    Items items = itemsList.get(getBindingAdapterPosition());
                    int comment_id = items.getComment_id();

                    SharedPreferences sharedPreferences =
                            context.getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
                    String token = sharedPreferences.getString("token",null);

                    volleyApi.deleteComment(context,comment_id,token);
                    itemsList.remove(items);
                    notifyDataSetChanged();
                }
            });

            cm_img_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int user_id = itemsList.get(getBindingAdapterPosition()).getUser_id();

                    Intent i = new Intent(context, PageActivity.class);
                    i.putExtra("user_id",user_id);
                    context.startActivity(i);
                }
            });

            cm_txt_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int user_id = itemsList.get(getBindingAdapterPosition()).getUser_id();

                    Intent i = new Intent(context, PageActivity.class);
                    i.putExtra("user_id",user_id);
                    context.startActivity(i);
                }
            });
        }
    }
}