package com.kks.portfolio_android.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kks.portfolio_android.activity.MainActivity;
import com.kks.portfolio_android.R;
import com.kks.portfolio_android.util.Util;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_Favorite extends Fragment {

    String token;

    public static Fragment_Favorite newInstance(){
        return new Fragment_Favorite();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment__favorite,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sharedPreferences =
                getActivity().getSharedPreferences(Util.PREFERENCE_NAME,MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (token == null) {
            Toast.makeText(getContext(), "로그인을 해주세요", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(), MainActivity.class);
            startActivity(i);
            getActivity().finish();
        }
    }
}