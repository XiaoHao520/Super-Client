package com.superschool;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.superschool.activity.ConversationActivity;
import com.superschool.adapter.FragmentAdapter;
import com.superschool.customeview.MyViewpager;
import com.superschool.entity.ConversationRecording;
import com.superschool.fragments.FrameOne;
import com.superschool.fragments.FrameFour;
import com.superschool.fragments.FrameThree;
import com.superschool.fragments.FrameTwo;
import com.superschool.init.InitUser;
import com.superschool.tools.Time;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

import static com.superschool.R.id.map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout f1;
    private LinearLayout f2;
    private LinearLayout f4;
    private LinearLayout f3;
    private static MyViewpager vp;
    List<Fragment> fragmentList;
    SharedPreferences shared;
    private static String localUser;
    ConversationRecording recording;


    private static List<Map<String, String>> data;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        System.out.println("main ---------------onstart");
        shared = getSharedPreferences("localUser", MODE_PRIVATE);
        localUser = shared.getString("username", null);
        data = new ArrayList<Map<String, String>>();

        initJM();
        InitUser initUser = new InitUser(this);
        initUser.login();//初始登录
        initView();
        initData();
    }


    @Override
    protected void onStart() {


        // insertTest();
        super.onStart();

    }


    private void insertTest() {
        ConversationRecording recording = new ConversationRecording();
        recording.setChatUser("xiaohao");
        recording.setLocalUser("admin");
        recording.setDate(Time.getNow());

        if (recording.save()) {
            Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_LONG).show();
            System.out.println("保存成功");
        } else {
            Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_LONG).show();
            System.out.println("保存失败");
        }
    }

    private void initJM() {
        System.out.println("初始化");
        JMessageClient.init(getApplicationContext());
        JMessageClient.registerEventReceiver(this);
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


    }

    @Override
    public void onClick(View view) {
        vp.setOffscreenPageLimit(3);
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


    public void onEvent(MessageEvent event) {
        Message message = event.getMessage();
        if (message != null) {
            switch (message.getContentType()) {

                case custom: {

                    CustomContent content = (CustomContent) message.getContent();

                    if ("text".equals(content.getStringValue("type"))) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("from", message.getFromUser().getUserName());
                        map.put("content", content.getStringValue("content"));
                        map.put("date", content.getStringValue("date"));
                        map.put("header", content.getStringValue("header"));
                        map.put("nickname", content.getStringValue("nickname"));
                        map.put("username", message.getFromUser().getUserName());
                        map.put("status", "u");
                        FrameThree.updateConversationList(map);
                    }

                    if ("conversation".equals(content.getStringValue("type"))) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("from", content.getStringValue("from"));
                        map.put("content", content.getStringValue("content"));
                        map.put("date", content.getStringValue("date"));
                        map.put("header", content.getStringValue("header"));
                        map.put("nickname", content.getStringValue("nickname"));
                        map.put("username", content.getStringValue("username"));

                        FrameThree.updateConversationList(map);


                    }
                    break;
                }

            }


        }


    }


    private void initData() {
        recording = new ConversationRecording();
        if (localUser != null) {
            List<ConversationRecording> conversationList = recording.where("localUser=?", localUser).find(ConversationRecording.class);
            if (conversationList != null) {
                for (ConversationRecording recording : conversationList) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("from", recording.getChatUser());
                    map.put("content", recording.getLast());
                    map.put("date", recording.getDate());
                    map.put("header", recording.getHeader());
                    map.put("nickname", recording.getNickname());
                    map.put("username", recording.getChatUser());
                    map.put("status", recording.getStatus());
                    data.add(map);
                }
                FrameThree.initData(data);
            }


        }

    }

    public static void selectFragement(int i, String what) {
        vp.setCurrentItem(i);


        if (what.equals("store")) {

            System.out.println("执行了没");

            FrameOne.pickPos();
        }
    }

}
