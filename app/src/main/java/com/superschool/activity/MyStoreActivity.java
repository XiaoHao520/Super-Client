package com.superschool.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superschool.R;
import com.superschool.adapter.StoreListAdapter;
import com.superschool.tools.MOkHttp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MyStoreActivity extends AppCompatActivity implements View.OnClickListener {
    private static List<JSONObject> data;
    private static String url = "http://www.sinbel.top/study/public/index.php/store/index/getHistory";
    private static String holder;
    static JSONObject json;
    private static Context context;
    private ListView storeList;
    private TextView newStore;
    private static final int APPLY = 549;

    private static final int APLLY_NEW_STORE = 32;
    private static List<JSONObject> tempData;
    StoreListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_store);
        context = getApplicationContext();
        initView();
        initData();


    }

    private void initView() {
        storeList = (ListView) findViewById(R.id.storelist);
        newStore = (TextView) findViewById(R.id.newStore);
        newStore.setOnClickListener(this);

    }

    private void initData() {

        Intent intent = getIntent();
        holder = intent.getStringExtra("holder");
        data = new ArrayList<JSONObject>();
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                JSONArray array = (JSONArray) msg.obj;


                if(array.size()!=0){




                for (int i = 0; i < array.size(); i++) {

                    data.add(JSON.parseObject(array.get(i).toString()));
                }

                adapter = new StoreListAdapter(data, context);
                storeList.setAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };

        new Thread(new MThread(holder, handler)).start();
    }

    @Override
    public void onClick(View v) {
        if(newStore==v){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("是否开通新的小生意").setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   MyStoreActivity.this.setResult(APLLY_NEW_STORE);

                    MyStoreActivity.this.finish();


                }
            }).setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                     dialog.dismiss();
                }
            }).create().show();
        }
    }

    class MThread implements Runnable {
        private String url = "http://www.sinbel.top/study/public/index.php/store/index/getHistory";

        private String holder;
        private Handler handler;

        public MThread(String holder, Handler handler) {
            this.holder = holder;
            this.handler = handler;
        }

        @Override
        public void run() {
            MOkHttp ok = new MOkHttp();
            JSONArray array = ok.getStoreHistory(holder, url);

            if (array != null) {
                Message message = new Message();
                message.obj = array;
                handler.sendMessage(message);
            }

        }
    }



    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
