package com.superschool.init;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superschool.entity.User;
import com.superschool.tools.MOkHttp;
import com.tencent.qcloud.network.assist.ContentRange;

/**
 * Created by xiaohao on 17-11-3.
 */

public class InitUser {

    private String url = "http://www.sinbel.top/study/public/index.php/user/user/login";
    private Context context;


    public InitUser(Context context) {
        this.context = context;
    }

    public boolean isLogined() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("localUser", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        String password = sharedPreferences.getString("password", null);
        if (username != null&&password!=null) {
            User user = new User();
            user.setUsername(username);
            user.setUserPassword(password);
            MOkHttp ok = new MOkHttp();
            JSONArray obj = JSON.parseArray(ok.login(user, url));
            JSONObject json = JSONObject.parseObject(obj.get(0).toString());
            System.out.println(json.toString());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", json.get("username").toString());
            editor.putString("nickname", json.get("user_nickname").toString());
            editor.putString("userid", json.get("user_id").toString());
            editor.putString("userheader",json.get("user_header").toString());
            if (json.get("user_school") == null) {
                editor.putString("userSchool", null);
            } else {
                editor.putString("userSchool", json.get("user_school").toString());
            }
            editor.commit();
            return true;

        }

        return false;
    }

}
