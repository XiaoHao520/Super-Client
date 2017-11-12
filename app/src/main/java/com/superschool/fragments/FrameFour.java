package com.superschool.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import com.superschool.activity.MyStoreActivity;
import com.superschool.entity.Store;
import com.superschool.tools.LruImageCache;

import org.litepal.crud.DataSupport;

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
    String[] items = new String[]{"小生意", "订单管理", "修改资料"};
    View view;
    SharedPreferences sharedPreferences;
    private TextView nickname;
    private static final int MODIFY = 433;
    private static final int LOGIN = 776;
    List<Map<String, Object>> list;
    SharedPreferences.Editor editor;
    SimpleAdapter adapter;
    NetworkImageView imageView;
    private static final int GO_TO_MY_STORE = 73;
    private static final String TAG = "f4";
    private static String localUser;
    private static Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f4_layout, container, false);
        sharedPreferences = getActivity().getSharedPreferences("localUser", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        context=getActivity();
        localUser = sharedPreferences.getString("usernmae", null);
        initView();
        return view;

    }

    private void initView() {
        JMessageClient.init(getActivity());
        imageView = (NetworkImageView) view.findViewById(R.id.userHeader);
        login = (Button) view.findViewById(R.id.login);
        login.setOnClickListener(this);

        String userid = sharedPreferences.getString("userid", null);

        if (userid == null || sharedPreferences.getBoolean("isLogin", false) == false) {
            login.setText("请登录");
            login.setBackground(context.getDrawable(R.drawable.btn_ok));
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
                login.setBackground(context.getDrawable(R.drawable.btn_ok));
                login.setText("请登录");
                JMessageClient.logout();
                list.clear();
                adapter.notifyDataSetChanged();
                FrameThree.dataClear();

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
                    Intent intent = new Intent(getActivity(), MyStoreActivity.class);
                    intent.putExtra("holder", sharedPreferences.getString("username", null));
                    startActivityForResult(intent, GO_TO_MY_STORE);
                } else {
                    //提示申请店铺
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("确认");
                            MainActivity.selectFragement(0, "store");
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
                FrameThree.initData(localUser);
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
        if (requestCode == GO_TO_MY_STORE) {
            if (resultCode == 32) {
                MainActivity.selectFragement(0, "store");
            }
        }
    }

    public void hasLogined() {
        login.setBackground(context.getDrawable(R.drawable.btn_no));
        login.setText("退出");
        imageView.setImageUrl(sharedPreferences.getString("userheader", null), new ImageLoader(Volley.newRequestQueue(getContext()), LruImageCache.instance()));
        list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (i == 0) {
                map.put("icon", R.drawable.store1);
            }
            if (i == 1) {
                map.put("icon", R.drawable.order);
            }
            if (i == 2) {
                map.put("icon", R.drawable.myinfo);
            }

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
        String username = sharedPreferences.getString("username", null);
        List<Store> storeList = DataSupport.where("storeHolder=?", username).find(Store.class);
        if (storeList != null) {
            if (storeList.size() == 0) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        Log.i(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }
}
