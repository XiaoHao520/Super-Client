package com.superschool;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.superschool.adapter.FragmentAdapter;
import com.superschool.customeview.MyViewpager;
import com.superschool.fragments.FrameOne;
import com.superschool.fragments.FrameFour;
import com.superschool.fragments.FrameThree;
import com.superschool.fragments.FrameTwo;
import com.superschool.init.InitUser;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout f1;
    private LinearLayout f2;
    private LinearLayout f4;
    private LinearLayout f3;
    private MyViewpager vp;
    List<Fragment> fragmentList;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        InitUser initUser = new InitUser(this);
        initJM();
        initUser.login();//初始登录
        initView();
    }

    private void initJM() {
        JMessageClient.init(getApplicationContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        vp = (MyViewpager) findViewById(R.id.viewPager);
        f1 = (LinearLayout) findViewById(R.id.f1);
        f2 = (LinearLayout) findViewById(R.id.f2);
        f1.setOnClickListener(this);
        f2.setOnClickListener(this);
        f3 = (LinearLayout) findViewById(R.id.f3);

        f3.setOnClickListener(this);
        f4 = (LinearLayout) findViewById(R.id.f4);
        f4.setOnClickListener(this);

        FrameOne f1 = new FrameOne();
        FrameTwo f2 = new FrameTwo();
        FrameThree f3 = new FrameThree();
        FrameFour f4 = new FrameFour();


        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(f1);
        fragmentList.add(f2);
        fragmentList.add(f3);
        fragmentList.add(f4);
        FragmentPagerAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList);
        vp.setAdapter(adapter);
        vp.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {

            }
        });


    }

    @Override
    public void onClick(View view) {
        if (view == f1) {
            vp.setCurrentItem(0);
        }
        if (view == f4) {
            vp.setCurrentItem(3);
        }
        if (view == f2) {
            vp.setCurrentItem(1);
        }
        if (view == f3) {
            vp.setCurrentItem(2);
        }
    }


}
