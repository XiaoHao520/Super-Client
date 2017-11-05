package com.superschool.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.superschool.R;
import com.superschool.adapter.ConversationAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversationActivity extends AppCompatActivity {

    private ListView conversationList;
    private static List<Map<String,String>>data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);


        initView();
    }

    private void initView() {

        conversationList= (ListView) findViewById(R.id.conversationList);

        data=new ArrayList<Map<String, String>>();
        for (int i = 0; i <10 ; i++) {
            Map<String,String>map=new HashMap<String, String>();
            if(i%2==0){
                map.put("status","u");

            }else {
                map.put("status","m");
            }
            map.put("content","说了："+i);
            data.add(map);
        }
        ConversationAdapter adapter=new ConversationAdapter(this,data);
        conversationList.setAdapter(adapter);


    }
}
