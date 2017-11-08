package com.superschool.activity;

import android.content.Intent;
import android.content.IntentFilter;
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

import com.superschool.R;

import java.util.ArrayList;
import java.util.List;

public class ApplyStoreActivity extends AppCompatActivity implements View.OnClickListener {


    private Spinner spinner;
    static List<String> items;
    private Button ensure;

    private EditText storeName;
    private EditText describe;

     TextView address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_store);
        initView();
        initData();
    }

    private void initData() {
        Intent intent=getIntent();

        address.setText("常住地址:"+intent.getStringExtra("address"));

    }

    private void initView() {
        address= (TextView) findViewById(R.id.address);
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

    }
}



