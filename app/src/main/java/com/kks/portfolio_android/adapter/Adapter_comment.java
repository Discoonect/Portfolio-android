package com.kks.portfolio_android.adapter;

import android.content.Context;
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
import com.kks.portfolio_android.CommentActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.model.Comments;
import com.kks.portfolio_android.model.Posting;
import com.kks.portfolio_android.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
        TextView cm_txt_cnt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cm_img_profile = itemView.findViewById(R.id.cm_img_profile);
            cm_txt_name = itemView.findViewById(R.id.cm_txt_name);
            cm_txt_comment = itemView.findViewById(R.id.cm_txt_comment);
            cm_txt_time = itemView.findViewById(R.id.cm_txt_time);
            cm_img_delete = itemView.findViewById(R.id.cm_img_delete);
            cm_txt_cnt = itemView.findViewById(R.id.cm_txt_cnt);

            cm_img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Comments comments = commentArrayList.get(getAdapterPosition());
                    int comment_id = comments.getComment_id();

                    SharedPreferences sharedPreferences =
                            context.getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
                    String token = sharedPreferences.getString("token",null);

                    deleteComment(comment_id,token);
                    commentArrayList.remove(comments);
                    notifyDataSetChanged();
                }
            });
        }

        private void deleteComment(int comment_id,String token) {
            JSONObject body = new JSONObject();
            try {
                body.put("comment_id", comment_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Util.BASE_URL + "/api/v1/comment/deletecomment",
                    body,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                boolean success = response.getBoolean("success");

                                if (success == false) {
                                    Toast.makeText(context, "떙", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                Toast.makeText(context, "댓글 삭제 성공", Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }
            )  {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", "Bearer " + token);
                    return params;
                }
            } ;
            Volley.newRequestQueue(context).add(request);


        }
    }
}