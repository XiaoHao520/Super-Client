package com.superschool.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.superschool.R;
import com.superschool.entity.User;
import com.superschool.tools.ImagePicker;
import com.superschool.tools.MOkHttp;

import java.io.IOException;

import cn.finalteam.galleryfinal.widget.GFImageView;

public class ModifyInfoActivity extends AppCompatActivity implements View.OnClickListener {


    private GFImageView userHeader;
    private EditText school;
    private EditText nickname;
    private Button save;
    private SharedPreferences shared;
    AlertDialog.Builder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        initView();
    }

    private void initView() {
        shared = getSharedPreferences("localUser", MODE_PRIVATE);
        school = (EditText) findViewById(R.id.school);
        nickname = (EditText) findViewById(R.id.nickname);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        userHeader = (GFImageView) findViewById(R.id.userHeader);
        userHeader.setOnClickListener(this);
        nickname.setText(shared.getString("nickname", null));


    }

    @Override
    public void onClick(View view) {
        if (view == save) {

            String schoolName = school.getText().toString();


            System.out.println("================"+schoolName);
            if(schoolName.equals("")){
                schoolName=school.getHint().toString();
            }
            String nicknameContent = nickname.getText().toString();
            String userHeaderPath = shared.getString("userheader",null);

            String userid = shared.getString("userid", null);


            final User user = new User();
            user.setUserSchool(schoolName);
            user.setUserNickName(nicknameContent);


            user.setUserId(userid);
            user.setUserHeader(userHeaderPath);

            String url = "http://www.sinbel.top/study/public/index.php/user/user/modify";
            new Thread(new MRunable(user,url,this)).start();

            dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setMessage("等待");
            dialogBuilder.create().show();
        }
        if (userHeader == view) {
            ImagePicker picker = new ImagePicker();
            picker.init(this);
            picker.pick(1);
        }
    }

    public void getHeaderPath() {


    }

    class MRunable implements Runnable {


        User user;
        String url;

        boolean flag;
        ModifyInfoActivity activity;

        public MRunable(User user, String url, ModifyInfoActivity activity) {
            this.user = user;
            this.url = url;
            this.activity = activity;
        }

        @Override
        public void run() {
            MOkHttp ok = new MOkHttp();
            try {
                flag = ok.modifyUser(user, url);


                if(flag){

                    System.out.println("返回了");
                    activity.stopAlter();
                }else {

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopAlter(){
      dialogBuilder.show().dismiss();
        Intent intent=this.getIntent();
        this.setResult(1,intent.putExtra("nickname",nickname.getText().toString()));
        this.finish();
    }
}
