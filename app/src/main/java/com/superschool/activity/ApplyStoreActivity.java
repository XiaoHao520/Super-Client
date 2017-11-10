package com.superschool.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationListener;
import com.superschool.R;
import com.superschool.entity.Store;
import com.superschool.tools.MOkHttp;

import java.util.ArrayList;
import java.util.List;

public class ApplyStoreActivity extends AppCompatActivity implements View.OnClickListener {


    private Spinner spinner;
    static List<String> items;
    private Button ensure;
    private EditText storeName;
    private EditText describe;
    TextView address;
    private static String lon;

    private SharedPreferences sharedPreferences;
    private static String lat;
    private static String storeAddress;
    private String username;
    private static final int OK = 982;
    private static final int ERROR = 576;
    private String school;

    private static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_store);
        initView();
        initData();
    }

    private void initData() {
        context = getApplicationContext();
        Intent intent = getIntent();
        sharedPreferences = getSharedPreferences("localUser", MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);
        school = sharedPreferences.getString("school", null);
        storeAddress = intent.getStringExtra("address");
        address.setText("常住地址:" + intent.getStringExtra("address"));
        lon = intent.getStringExtra("lon");
        lat = intent.getStringExtra("lat");

    }


    private void initView() {
        address = (TextView) findViewById(R.id.address);
        storeName = (EditText) findViewById(R.id.storename);
        describe = (EditText) findViewById(R.id.describe);
        ensure = (Button) findViewById(R.id.ensure);
        ensure.setOnClickListener(this);
        items = new ArrayList<String>();
        items.add("请选择业务类型");
        items.add("打印");
        items.add("跑腿");
        spinner = (Spinner) findViewById(R.id.spninner);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {


        System.out.println(spinner.getSelectedItemPosition());


        gotoApply();
    }

    public void gotoApply() {
        final String url = "http://www.sinbel.top/study/public/index.php/store/index/apply";
        String storename = storeName.getText().toString();
        String storeDsc = describe.getText().toString();
        final Store store = new Store();
        store.setLat(lat);
        store.setLon(lon);
        store.setStoreAddress(storeAddress);
        if (school == null) {
            Toast.makeText(getApplicationContext(), "请完善个人信息或者重新登录试试", Toast.LENGTH_LONG).show();
        }
        store.setSchool(school);
        if (storename == null || storeDsc == null) {
            Toast.makeText(getApplicationContext(), "以上全部为为必填选项", Toast.LENGTH_LONG).show();
            return;
        }
        store.setStoreDsc(storeDsc);
        store.setStoreName(storeName.getText().toString());
        if (spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "请选择业务", Toast.LENGTH_LONG).show();
            return;
        }
        store.setType(spinner.getSelectedItem().toString());
        if (username != null) {
            store.setStoreHolder(username);
        }
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String rs = (String) msg.obj;
                if (rs.equals("ok")) {
                    System.out.println(rs);  //is ok
                    //开通成功
                    AlertDialog.Builder builder = new AlertDialog.Builder(ApplyStoreActivity.this);
                    builder.setMessage("恭喜，您已开通").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (store.save()) {
                                System.out.println("ok");
                            }
                            dialog.dismiss();
                            ApplyStoreActivity.this.setResult(OK);
                            ApplyStoreActivity.this.finish();
                        }
                    }).create().show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ApplyStoreActivity.this);
                    builder.setMessage("抱歉，请稍后重试").setPositiveButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ApplyStoreActivity.this.setResult(ERROR);
                            ApplyStoreActivity.this.finish();
                        }
                    }).create().show();
                }
            }
        };

        new Thread(new MThread(store, url, mHandler)).start();
    }


    class MThread implements Runnable {
        private Store store;
        private String url;
        private Handler handler;

        public MThread(Store store, String url, Handler handler) {
            this.store = store;
            this.url = url;
            this.handler = handler;
        }

        @Override
        public void run() {
            MOkHttp ok = new MOkHttp();
            String rs = ok.applyStore(store, url);
            Message message = new Message();
            message.obj = rs;
            handler.sendMessage(message);
        }
    }
}



