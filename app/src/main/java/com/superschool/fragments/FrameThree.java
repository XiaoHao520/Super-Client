package com.superschool.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.superschool.R;
import com.superschool.activity.LoginActivity;
import com.superschool.activity.ModifyInfoActivity;
import com.superschool.activity.RegisterActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaohao on 17-10-16.
 */

public class FrameThree extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Button login;
    private ListView infoSetting;
    String[] items = new String[]{"店铺", "订单管理", "修改资料"};
    View view;
    SharedPreferences sharedPreferences;
    private TextView nickname;
    private static final int MODIFY = 433;
    private static final int LOGIN = 776;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f3_layout, container, false);
        initView();
        return view;

    }

    private void initView() {
        login = (Button) view.findViewById(R.id.login);
        login.setOnClickListener(this);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("icon", R.drawable.add);
            map.put("value", items[i]);
            list.add(map);
        }
        sharedPreferences = getActivity().getSharedPreferences("localUser", Context.MODE_PRIVATE);

        String userid = sharedPreferences.getString("userid", null);

        if (userid == null) {
            login.setText("请登录");
            login.setBackgroundColor(Color.GREEN);
        } else {
            login.setBackgroundColor(Color.RED);
            login.setText("退出");
            nickname = (TextView) view.findViewById(R.id.nickname);
            nickname.setText(sharedPreferences.getString("nickname", "new user"));
            infoSetting = (ListView) view.findViewById(R.id.infoSetting);
            SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.info_setting_item, new String[]{"icon", "value"}, new int[]{R.id.icon, R.id.item});
            infoSetting.setAdapter(adapter);
            infoSetting.setOnItemClickListener(this);
        }


    }

    @Override
    public void onClick(View view) {
        if(login==view){
            if(login.getText().toString().equals("退出")){

            }else {
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), LOGIN);
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (i == 2) {
            startActivityForResult(new Intent(getActivity(), ModifyInfoActivity.class), MODIFY);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MODIFY) {
            if (resultCode == 1) {
                nickname.setText(data.getStringExtra("nickname"));

            }
            if (requestCode == 0) {
                //nothing is change;
            }

        }
    }
}
