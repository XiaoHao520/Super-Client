package com.superschool.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superschool.R;
import com.superschool.adapter.StoreListAdapter;
import com.superschool.tools.MOkHttp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyStoreActivity extends AppCompatActivity {
    private static List<JSONObject> data;
    private static String url = "http://www.sinbel.top/study/public/index.php/store/index/getHistory";
    private static String holder;
    static JSONObject json;
    private static Context context;
    private ListView storeList;


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
    }

    private void initData() {


        Intent intent = getIntent();
        holder = intent.getStringExtra("holder");
        data = new ArrayList<JSONObject>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                MOkHttp ok = new MOkHttp();
                JSONArray array = ok.getStoreHistory(holder, url);

                if (array != null) {

                    setData(array);
                }


            }
        }).start();
    }

    public void setData(JSONArray array) {


        for (int i = 0; i < array.size(); i++) {
            JSONObject json = JSONArray.parseObject(array.get(i).toString());
            data.add(json);
        }
        tempData = data;
        adapter = new StoreListAdapter(data, context);
        storeList.setAdapter(adapter);
    }


   /* @Override
    public void onBackPressed() {

        final String url = "http://www.sinbel.top/study/public/index.php/store/index/del";
        for (int i = 0; i < tempData.size(); i++) {

            final JSONObject json = tempData.get(i);


            for (int j = 0; j < data.size(); j++) {
                JSONObject json2 = data.get(j);
                if (json.get("store_name").equals(json2.get("store_name"))) {

                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MOkHttp ok = new MOkHttp();
                            ok.delStore(json.get("store_name").toString(), url);

                        }
                    }).start();
                }
            }


        }


    }*/
}
