package com.superschool.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.superschool.R;
import com.superschool.activity.LoginActivity;
import com.superschool.activity.RegisterActivity;

/**
 * Created by xiaohao on 17-10-16.
 */

public class FrameThree extends Fragment implements View.OnClickListener {

    private Button login;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.f3_layout,container,false);
        login= (Button) view.findViewById(R.id.login);
        login.setOnClickListener(this);
           return view;

    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }
}
