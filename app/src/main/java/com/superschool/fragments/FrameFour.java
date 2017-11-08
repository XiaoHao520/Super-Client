package com.superschool.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.superschool.MainActivity;
import com.superschool.R;
import com.superschool.activity.LoginActivity;
import com.superschool.activity.ModifyInfoActivity;
import com.superschool.activity.RegisterActivity;
import com.superschool.tools.LruImageCache;
import com.superschool.tools.MImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by xiaohao on 17-10-16.
 */

public class FrameFour extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Button login;
    private ListView infoSetting;
    String[] items = new String[]{"店铺", "订单管理", "修改资料"};
    View view;
    SharedPreferences sharedPreferences;
    private TextView nickname;
    private static final int MODIFY = 433;
    private static final int LOGIN = 776;
    List<Map<String, Object>> list;
    SharedPreferences.Editor editor;
    SimpleAdapter adapter;
    NetworkImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f4_layout, container, false);
        initView();
        return view;

    }

    private void initView() {
        JMessageClient.init(getActivity());
        imageView = (NetworkImageView) view.findViewById(R.id.userHeader);
        login = (Button) view.findViewById(R.id.login);
        login.setOnClickListener(this);
        sharedPreferences = getActivity().getSharedPreferences("localUser", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String userid = sharedPreferences.getString("userid", null);

        if (userid == null || sharedPreferences.getBoolean("isLogin", false) == false) {
            login.setText("请登录");
            login.setBackgroundColor(Color.GREEN);
        } else {
            hasLogined();
        }


    }

    @Override
    public void onClick(View view) {
        if (login == view) {
            if (login.getText().toString().equals("退出")) {
                editor.putBoolean("isLogin", false);
                editor.commit();
                login.setBackgroundColor(Color.GREEN);
                login.setText("请登录");
                list.clear();
                adapter.notifyDataSetChanged();


            } else {
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), LOGIN);
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        switch (i) {

            case 0: {


                if (hasStore()) {
                    //进入我的额店铺//

                } else {
                    //提示申请店铺
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("确认");
                            MainActivity.selectFragement(0,"store");
                        }
                    }).setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("以后再说");
                        }
                    }).setMessage("暂时没有店铺，现在去申请？").create().show();

                }

                break;
            }
            case 1: {
                break;
            }
            case 2: {
                startActivityForResult(new Intent(getActivity(), ModifyInfoActivity.class), MODIFY);
                break;
            }
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


        if (requestCode == LOGIN) {
            if (resultCode == 820) {
                hasLogined();
            }
        }
    }

    public void hasLogined() {
        login.setBackgroundColor(Color.RED);
        login.setText("退出");
        imageView.setImageUrl(sharedPreferences.getString("userheader", null), new ImageLoader(Volley.newRequestQueue(getContext()), LruImageCache.instance()));
        list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("icon", R.drawable.add);
            map.put("value", items[i]);
            list.add(map);

            nickname = (TextView) view.findViewById(R.id.nickname);
            nickname.setText(sharedPreferences.getString("nickname", "new user"));
            infoSetting = (ListView) view.findViewById(R.id.infoSetting);
            adapter = new SimpleAdapter(getActivity(), list, R.layout.info_setting_item, new String[]{"icon", "value"}, new int[]{R.id.icon, R.id.item});
            infoSetting.setAdapter(adapter);
            infoSetting.setOnItemClickListener(this);
        }
    }

    public boolean hasStore() {


        String storeNum = sharedPreferences.getString("store", null);


        if (storeNum.equals("0")) {
            System.out.println("没有商店");
            return false;
        }
        return true;

    }



}
