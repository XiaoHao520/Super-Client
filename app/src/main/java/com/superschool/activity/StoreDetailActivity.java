package com.superschool.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.model.Text;
import com.superschool.MainActivity;
import com.superschool.R;
import com.superschool.tools.MOkHttp;
import com.superschool.tools.Time;

import java.io.Serializable;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

public class StoreDetailActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private ImageView back;
    private TextView storeName;
    private TextView storeDsc;
    private Button contact;
    private JSONObject json;
    private Intent intent;
    private String store;
    private String storeDiscribe;
    private static Map<String, String> storeInfo;
    private String storeHolder;
    private String url = "http://www.sinbel.top/study/public/index.php/user/user/userinfo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        intent = getIntent();
        store = intent.getStringExtra("store");
        storeDiscribe = intent.getStringExtra("dsc");
        storeHolder = intent.getStringExtra("holder");
        initView();
        initStoreHolderInfo();
        System.out.println("____________________________________mystoredetail  oncreate");


    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        storeName = (TextView) findViewById(R.id.storename);
        storeDsc = (TextView) findViewById(R.id.storedsc);
        contact = (Button) findViewById(R.id.contact);
        contact.setOnClickListener(this);
        contact.setOnTouchListener(this);
        back.setOnClickListener(this);
        storeName.setText(store);
        storeDsc.setText(storeDiscribe);
    }


    private void initStoreHolderInfo() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                StoreDetailActivity.storeInfo = (Map<String, String>) msg.obj;
                super.handleMessage(msg);
            }
        };

        new Thread(new MThread(handler, url, storeHolder)).start();

    }

    class MThread implements Runnable {

        private Handler handler;
        private String url;
        private String username;

        public MThread(Handler handler, String url, String username) {
            this.handler = handler;
            this.url = url;
            this.username = username;
        }

        @Override
        public void run() {
            MOkHttp okHttp = new MOkHttp();
            JSONArray array = okHttp.getUserInfo(username, url);
            JSONObject json = JSON.parseObject(array.get(0).toString());
            Map<String, String> data = new HashMap<String, String>();
            data.put("header", json.get("user_header").toString());
            data.put("nickname", json.get("user_nickname").toString());
            data.put("username", json.get("username").toString());
            data.put("content", "咱谈一笔生意");
            data.put("status", "m");
            data.put("date", Time.getNow());
            Message message = new Message();
            message.obj = data;
            handler.sendMessage(message);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == contact) {
            Intent intent = new Intent(StoreDetailActivity.this, ConversationActivity.class);
            intent.putExtra("data", (Serializable) storeInfo);
            startActivity(intent);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v == contact) {


            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //contact.setBackgroundColor(R.color.bg);
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                // contact.setBackgroundColor(R.color.button);
                System.out.println("抬起");
            }

        }
        return false;
    }


    @Override
    protected void onPause() {
        System.out.println("---------mystore-------onpause");
        super.onPause();
    }
}
