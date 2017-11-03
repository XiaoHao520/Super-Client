package com.superschool.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.superschool.R;
import com.superschool.entity.User;
import com.superschool.tools.JMsg;
import com.superschool.tools.MOkHttp;

import java.io.IOException;

import cn.jpush.im.android.api.JMessageClient;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText username;
    private EditText password;
    private Button login;
    private Button register;

    JMsg jMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        JMessageClient.init(getApplicationContext());
        jMsg = JMsg.instance(getApplicationContext());
        initView();
    }

    private void initView() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {


        if (view == register) {
            startActivity(new Intent(this, RegisterActivity.class));
        }
        if (view == login) {
            User user = new User();
            user.setUsername(username.getText().toString());
            user.setUserPassword(password.getText().toString());
            jMsg.login(user, this);
        }

    }

    public void gotoSuccess(JSONObject json) {

        //取出user的信息=>保存


        if (json.size() == 8) {


            System.out.println(json.toString());
            SharedPreferences sharedPreferences = getSharedPreferences("localUser", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", json.get("username").toString());
            editor.putString("nickname", json.get("user_nickname").toString());
            editor.putString("userid", json.get("user_id").toString());
            editor.putString("userheader", json.get("user_header").toString());
            editor.putString("password", json.get("user_password").toString());
            if (json.get("user_school") == null) {
                editor.putString("userSchool", null);
            } else {
                editor.putString("userSchool", json.get("user_school").toString());
            }
            editor.commit();
            this.finish();
        } else {
            //登录失败
            gotoFail();
        }
    }

    public void gotoFail() {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("--------------onrestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("----------------pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("---------------onresume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("-----------------onstop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("------------ondestory");
    }
}
