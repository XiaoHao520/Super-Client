package com.superschool;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

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
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Message;

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
    private ImageView school, me, chat, note;
    private TextView schoolText, meText, chatText, noteText;
    private static List<Map<String, String>> data;
    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
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
        setButton();

        super.onStart();

    }


    private void setButton() {
        int i = vp.getCurrentItem();

        switch (i) {
            case 0: {
                school.setImageResource(R.drawable.mapactived);
                me.setImageResource(R.drawable.me);
                note.setImageResource(R.drawable.note);
                chat.setImageResource(R.drawable.chat);
                schoolText.setTextColor(context.getColor(R.color.navactived));
                noteText.setTextColor(context.getColor(R.color.nav));
                meText.setTextColor(context.getColor(R.color.nav));
                chatText.setTextColor(context.getColor(R.color.nav));
                break;
            }
            case 1: {
                school.setImageResource(R.drawable.map);
                me.setImageResource(R.drawable.me);
                note.setImageResource(R.drawable.noteactived);
                chat.setImageResource(R.drawable.chat);
                schoolText.setTextColor(context.getColor(R.color.nav));
                noteText.setTextColor(context.getColor(R.color.navactived));
                meText.setTextColor(context.getColor(R.color.nav));
                chatText.setTextColor(context.getColor(R.color.nav));
                break;
            }
            case 2: {
                school.setImageResource(R.drawable.map);

                note.setImageResource(R.drawable.note);
                chat.setImageResource(R.drawable.chatactivied);
                me.setImageResource(R.drawable.me);
                schoolText.setTextColor(context.getColor(R.color.nav));
                noteText.setTextColor(context.getColor(R.color.nav));
                meText.setTextColor(context.getColor(R.color.nav));
                chatText.setTextColor(context.getColor(R.color.navactived));
                break;
            }
            case 3: {
                school.setImageResource(R.drawable.map);
                note.setImageResource(R.drawable.note);
                chat.setImageResource(R.drawable.chat);
                me.setImageResource(R.drawable.meactived);
                schoolText.setTextColor(context.getColor(R.color.nav));
                noteText.setTextColor(context.getColor(R.color.nav));
                meText.setTextColor(context.getColor(R.color.navactived));
                chatText.setTextColor(context.getColor(R.color.nav));
                break;
            }
        }
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
        me = (ImageView) findViewById(R.id.me);
        note = (ImageView) findViewById(R.id.note);
        school = (ImageView) findViewById(R.id.school);
        chat = (ImageView) findViewById(R.id.chat);

        meText = (TextView) findViewById(R.id.meText);
        noteText = (TextView) findViewById(R.id.noteText);
        schoolText = (TextView) findViewById(R.id.schoolText);
        chatText = (TextView) findViewById(R.id.chatText);
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
        setButton();

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
            FrameOne.pickPos();
        }
    }


    @Override
    public void finish() {
        moveTaskToBack(true);
        super.finish();
    }
}
