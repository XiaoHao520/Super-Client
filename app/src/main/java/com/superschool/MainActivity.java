package com.superschool;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.superschool.adapter.FragmentAdapter;
import com.superschool.customeview.MyViewpager;
import com.superschool.fragments.FrameOne;
import com.superschool.fragments.FrameThree;
import com.superschool.fragments.FrameTwo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView school;
    private ImageView news;
    private ImageView me;
    private MyViewpager vp;
    List<Fragment> fragmentList;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        vp = (MyViewpager) findViewById(R.id.viewPager);
        school = (ImageView) findViewById(R.id.schoolImg);
        news= (ImageView) findViewById(R.id.newsImg);
        school.setOnClickListener(this);
        news.setOnClickListener(this);
        me= (ImageView) findViewById(R.id.meImg);
        me.setOnClickListener(this);

        FrameOne f1 = new FrameOne();
        FrameTwo f2 = new FrameTwo();
        FrameThree f3 = new FrameThree();

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(f1);
        fragmentList.add(f2);
        fragmentList.add(f3);
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
        if (view == school) {
            vp.setCurrentItem(0);
        }
        if(view==me){
            vp.setCurrentItem(2);
        }
        if(view==news){
            vp.setCurrentItem(1);
        }
    }
}
