package com.superschool.activity;


import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.appcompat.BuildConfig;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.superschool.MainActivity;
import com.superschool.R;
import com.superschool.entity.User;
import com.superschool.tools.FileUpload;
import com.superschool.tools.JMsg;
import com.superschool.tools.MImageLoader;

import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.GFImageView;
import cn.jpush.im.android.api.JMessageClient;


/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {
    private EditText email;
    private EditText username;
    private EditText password1;
    private EditText password2;
    private Button register;
    private GFImageView addHeader;
    private MHandler mHandler;
    JMsg jMsg;
    private static String headerPath=null;
    MImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //先注册到极光返回id存入服务器

        JMessageClient.init(getApplicationContext());
        initView();
        jMsg = JMsg.instance(getApplicationContext());
    }

    private void initView() {
        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        password1 = (EditText) findViewById(R.id.password1);
        password2 = (EditText) findViewById(R.id.password2);
        register = (Button) findViewById(R.id.register);
        addHeader = (GFImageView) findViewById(R.id.addHeader);
        imageLoader = new MImageLoader();
        addHeader.setOnClickListener(this);
        register.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view == register) {
            User user = new User();
            if (headerPath!=null) {
                String headUrl = null;
                try {
                    headUrl = FileUpload.uploadSingle(headerPath, getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                user.setUserHeader(headUrl);
            }else {
                user.setUserHeader("http://super-1252119503.cosgz.myqcloud.com/b2140269ab78105353269047a8d60473IMG_20171031_194321.jpg");
            }
            user.setUsername(username.getText().toString());
            user.setUserPassword(password1.getText().toString());
            user.setUserEmail(email.getText().toString());
            jMsg.register(user, this);
        }
        if (addHeader == view) {
            mHandler = new MHandler();
            pickImages(1);
        }
    }

    public void gotoSuccess() {
        System.out.println("注册成功");
//保存数据


        this.finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void pickImages(int sum) {
        imageLoader.clearMemoryCache();
        ThemeConfig theme = new ThemeConfig.Builder().build();
        FunctionConfig function = new FunctionConfig.Builder().setEnableCamera(true).setEnableEdit(false).setEnablePreview(true).build();
        MImageLoader imageLoader = new MImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(getApplicationContext(), imageLoader, theme).setDebug(BuildConfig.DEBUG).setFunctionConfig(function).build();
        GalleryFinal.init(coreConfig);
        GalleryFinal.openGalleryMuti(1, 10 - sum, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (reqeustCode == 1) {
                    // photoInfoList = resultList;
                    Message msg = new Message();
                    msg.obj = resultList.get(0).getPhotoPath();
                    mHandler.sendMessage(msg);
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
            }
        });
    }

    class MHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            headerPath = (String) msg.obj;
            System.out.println(headerPath);
            imageLoader.displayImage(RegisterActivity.this, headerPath, addHeader, null, 200, 200);
            super.handleMessage(msg);
        }
    }
}

