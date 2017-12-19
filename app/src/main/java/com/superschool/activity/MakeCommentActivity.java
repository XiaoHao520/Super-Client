package com.superschool.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.superschool.R;
import com.superschool.tools.MOkHttp;

public class MakeCommentActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView send;
    private EditText content;
    private ImageView back;

    private String contentText;



    private Intent intent;
    private static final int SEND_COMMENT = 162;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_comment);
        initView();
    }

    private void initView() {
        send = (TextView) findViewById(R.id.send);
        content = (EditText)findViewById(R.id.content);
        back = (ImageView) findViewById(R.id.back);
        send.setOnClickListener(this);
        back.setOnClickListener(this);

         intent=getIntent();
    }

    @Override
    public void onClick(View v) {
        if (v == send) {
            //发送
            contentText = content.getText().toString();
            intent.putExtra("content",contentText);
            this.setResult(SEND_COMMENT,intent);
            this.finish();
        }
        if (back == v) {
            //返回//
        }
    }


}
