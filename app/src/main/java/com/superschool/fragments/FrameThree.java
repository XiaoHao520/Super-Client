package com.superschool.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.superschool.R;
import com.superschool.adapter.ConversationListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XIAOHAO-PC on 2017-11-04.
 */

public class FrameThree extends Fragment {


    View view;
    ListView conversationList;
   private static List<Map<String, String>> data;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.f3_layout, container, false);
        initView();

        return view;
    }

    private void initView() {
        conversationList = (ListView) view.findViewById(R.id.conversationList);
        data = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 10; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("header", "http://img3.imgtn.bdimg.com/it/u=2770691011,100164542&fm=27&gp=0.jpg");
            map.put("username", "用户名");
            map.put("nickname","nickname");
            map.put("last", "最后一条记录");
            map.put("date", "2014/14/14");
            data.add(map);
        }


        ConversationListAdapter adapter = new ConversationListAdapter(data, getActivity());
        conversationList.setAdapter(adapter);

    }


    public static void show() {

        System.out.println("广播发送");
    }
    public static void updateConversationList(Map<String,String> map){

        data.set(0,map);

    }


}
