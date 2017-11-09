package com.superschool.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_store);
        initView();
        initData();
    }

    private void initData() {
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

        final String url = "http://www.sinbel.top/study/public/index.php/store/index/apply";


        System.out.println(store.toString());

        new Thread(new Runnable() {
            @Override
            public void run() {
                final MOkHttp ok = new MOkHttp();
                String rs = ok.applyStore(store, url);

                if (rs.equals("ok")) {
                    //开通成功
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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
        }).start();
    }
}



