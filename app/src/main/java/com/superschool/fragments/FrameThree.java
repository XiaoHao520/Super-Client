package com.superschool.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.superschool.R;
import com.superschool.activity.ConversationActivity;
import com.superschool.adapter.ConversationListAdapter;
import com.superschool.entity.ConversationRecording;
import com.superschool.tools.Time;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XIAOHAO-PC on 2017-11-04.
 */

public class FrameThree extends Fragment implements AdapterView.OnItemClickListener {
    View view;
    private static ListView conversationList;
    private static List<Map<String, String>> data;
    private static final int CONVERSATION = 404;
    private static ConversationListAdapter adapter;
    private static int dataSize=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("------------------oncreate");
        view = inflater.inflate(R.layout.f3_layout, container, false);

        initView();
        return view;
    }

    public static void initData(List<Map<String,String>>list) {

         data=list;

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initView() {
        conversationList = (ListView) view.findViewById(R.id.conversationList);

       if(data==null)
       {
           data = new ArrayList<Map<String, String>>();
       }

        adapter = new ConversationListAdapter(data, getActivity());
        conversationList.setAdapter(adapter);
        conversationList.setOnItemClickListener(this);


    }


    public static void show() {
        System.out.println("广播发送");
    }

    public static void updateConversationList(Map<String, String> map) {
        System.out.println("消息进来");


        String username = map.get("username");
        for (int i = 0; i < data.size(); i++) {
            Map<String, String> temp = data.get(i);
            if (username.equals(temp.get("username").toString())) {
                data.remove(i);
                data.add(0, map);
                adapter.notifyDataSetChanged();
                return;
            }
        }
        data.add(0, map);
        adapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getActivity(), ConversationActivity.class);
        intent.putExtra("data", (Serializable) data.get(position));
        startActivityForResult(intent, CONVERSATION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CONVERSATION) {

            System.out.println(requestCode);

            System.out.println(resultCode);
            if (resultCode == 929) {
                Map<String, String> map = (Map<String, String>) data.getSerializableExtra("data");
                this.data.set(0, map);
                adapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void itemTotop(Map<String, String> map) {


    }

    @Override
    public void onResume() {

        adapter.notifyDataSetChanged();

        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();





/*

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


        if (recording.save()) {
            Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_LONG).show();
            System.out.println("保存成功");
        } else {
            Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_LONG).show();
            System.out.println("保存失败");
        }
*/

    }
}
