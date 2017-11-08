package com.superschool.activity;

import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.ProviderInfo;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.superschool.R;
import com.superschool.adapter.ConversationAdapter;
import com.superschool.entity.ConversationRecording;
import com.superschool.fragments.FrameThree;
import com.superschool.tools.Time;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

public class ConversationActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView conversationList;
    private static List<Map<String, String>> data;
    static ConversationAdapter adapter;
    private TextView back;
    private TextView nickname;
    private ImageView header;
    private ImageView send;
    private EditText input;
    Map<String, String> from;
    Intent intent;
    private SharedPreferences sharedPreferences;
    private static final int CONVERSATION = 929;
    private static String returnNickname;
    private static String returnUsername;
    private static String userHeader;
    List<Message> msgList;
    private static String localUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        JMessageClient.registerEventReceiver(this);
        sharedPreferences = getSharedPreferences("localUser", MODE_PRIVATE);
        localUser = sharedPreferences.getString("username", null);
        initView();
        initData();
        enterConversation();
    }

    private void initView() {
        conversationList = (ListView) findViewById(R.id.conversationList);
        header = (ImageView) findViewById(R.id.userHeader);
        back = (TextView) findViewById(R.id.back);
        nickname = (TextView) findViewById(R.id.nickname);
        send = (ImageView) findViewById(R.id.send);
        input = (EditText) findViewById(R.id.input);
        send.setOnClickListener(this);
    }

    private void initData() {

        data = new ArrayList<Map<String, String>>();
        intent = getIntent();
        from = (Map<String, String>) intent.getSerializableExtra("data");
        adapter = new ConversationAdapter(this, data);
        conversationList.setAdapter(adapter);
        userHeader = from.get("header");
        returnNickname = from.get("nickname");
        returnUsername = from.get("username");
        nickname.setText(from.get("nickname"));
        Glide.with(this).load(from.get("header")).placeholder(R.drawable.user).error(R.drawable.user).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new ImageViewTarget<GlideDrawable>(header) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        header.setImageDrawable(resource);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == send) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "m");
            String content = input.getText().toString();
            if (!content.equals("")) {
                map.put("content", content);
            }
            data.add(map);
            map.put("type", "text");
            map.put("content", input.getText().toString());
            map.put("date", Time.getNow());
            map.put("header", sharedPreferences.getString("userheader", null));
            map.put("nickname", sharedPreferences.getString("nickname", null));
            Message message = JMessageClient.createSingleCustomMessage(from.get("username"), map);
            JMessageClient.sendMessage(message);
            input.setText("");
            adapter.notifyDataSetChanged();
        }
    }


    private void enterConversation() {


        String username = from.get("username");
        if (username == null) {
            System.out.println("user is null");
        } else {
            JMessageClient.enterSingleConversation(username);

            Conversation conversation = JMessageClient.getSingleConversation(username);
            msgList = conversation.getAllMessage();
            int start = 0;

            if (msgList.size() == 0) {
                data.add(from);


                adapter.notifyDataSetChanged();

                return;
            }
            if (msgList.size() > 10) {
                start = msgList.size() - 10;

            }
            msgList = conversation.getMessagesFromOldest(start, 10);
            for (Message message : msgList) {


                ContentType type = message.getContentType();
                switch (type) {//现只有custom；
                    case custom: {

                        CustomContent content = (CustomContent) message.getContent();
                        if ("text".equals(content.getStringValue("type"))) {

                            Map<String, String> map = new HashMap<String, String>();
                            if (message.getFromUser().getUserName().equals(localUser)) {
                                map.put("status", "m");
                            } else {
                                map.put("status", "u");
                            }

                            map.put("from", message.getFromUser().getUserName());
                            map.put("content", content.getStringValue("content"));
                            map.put("date", content.getStringValue("date"));
                            map.put("header", content.getStringValue("header"));
                            map.put("nickname", content.getStringValue("nickname"));
                            map.put("username", content.getStringValue("username"));
                            data.add(map);
                        }
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        back();

    }


    @Override
    public void onBackPressed() {
        back();
        super.onBackPressed();
    }

    private void back() {
        JMessageClient.exitConversation();
        JMessageClient.unRegisterEventReceiver(this);
        Map<String, String> map = data.get(data.size() - 1);
        map.put("username", returnUsername);
        map.put("nickname", returnNickname);
        map.put("date", Time.getNow());
        intent.putExtra("data", (Serializable) map);
        ConversationRecording recording = new ConversationRecording();
        localUser = sharedPreferences.getString("username", null).toString();
        List<ConversationRecording> current = recording.where("localUser=? and chatUser=?",
                localUser, returnUsername).find(ConversationRecording.class);
        if (current != null) {
            recording.deleteAll(ConversationRecording.class, "localUser=? and chatUser=?", localUser, returnUsername);
        }
        recording.setLocalUser(localUser);
        recording.setChatUser(returnUsername);
        recording.setNickname(returnNickname);
        recording.setDate(Time.getNow());
        recording.setHeader(userHeader);
        recording.setStatus(map.get("status"));
        recording.setLast(map.get("content"));
        if (recording.save()) {
            Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_LONG).show();
            System.out.println("保存成功");
        } else {
            Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_LONG).show();
            System.out.println("保存失败");
        }
        this.setResult(CONVERSATION, intent);
        this.finish();
    }

    public void onEvent(MessageEvent event) {
        Message message = event.getMessage();
        ContentType type = message.getContentType();
        switch (type) {
            case text: {
                TextContent content = (TextContent) message.getContent();
                Map<String, String> map = new HashMap<String, String>();
                map.put("status", "u");
                map.put("content", content.getText());
                data.add(map);
                adapter.notifyDataSetChanged();
                break;
            }
            case custom: {
                CustomContent content = (CustomContent) message.getContent();
                if ("text".equals(content.getStringValue("type"))) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("status", "u");
                    map.put("from", message.getFromUser().getUserName());
                    map.put("content", content.getStringValue("content"));
                    map.put("date", content.getStringValue("date"));
                    map.put("header", content.getStringValue("header"));
                    map.put("nickname", content.getStringValue("nickname"));
                    map.put("username", content.getStringValue("username"));
                    System.out.println("收到text消息");
                    data.add(map);
                    adapter.notifyDataSetChanged();

                }
            }
            case image: {
                break;
            }
        }
    }
}
