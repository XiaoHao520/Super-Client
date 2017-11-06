package com.superschool.activity;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.superschool.R;
import com.superschool.adapter.ConversationAdapter;
import com.superschool.fragments.FrameThree;

import java.io.Serializable;
import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        JMessageClient.registerEventReceiver(this);
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


        sharedPreferences = getSharedPreferences("localUser", MODE_PRIVATE);
        data = new ArrayList<Map<String, String>>();

        intent = getIntent();
        from = (Map<String, String>) intent.getSerializableExtra("data");


        data.add(from);
        adapter = new ConversationAdapter(this, data);
        conversationList.setAdapter(adapter);

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
            Date date = new Date();
            map.put("type", "text");
            map.put("content", input.getText().toString());
            map.put("date", String.valueOf(date.getHours()));
            map.put("header", sharedPreferences.getString("userheader", null));
            map.put("nickname", sharedPreferences.getString("nickname", null));
            Message message = JMessageClient.createSingleCustomMessage(from.get("username"), map);
            JMessageClient.sendMessage(message);
            input.setText("");
            adapter.notifyDataSetChanged();
        }
    }


    private void enterConversation() {

        if (from.get("username") == null) {
            System.out.println("user is null");
        } else {
            JMessageClient.enterSingleConversation(from.get("username"));
            System.out.println("进入会话列表");
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


       // back();

    }


    @Override
    public void onBackPressed() {


        System.out.println("返回键");
        back();

        super.onBackPressed();
    }

    private void back() {
        JMessageClient.exitConversation();
        JMessageClient.unRegisterEventReceiver(this);
        System.out.println(data.get(data.size()-1));
        intent.putExtra("data", (Serializable) data.get(data.size()-1));
        this.setResult(CONVERSATION,intent);


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
                System.out.println("收到消息");
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
