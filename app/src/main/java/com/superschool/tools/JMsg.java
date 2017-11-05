package com.superschool.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superschool.activity.LoginActivity;
import com.superschool.activity.RegisterActivity;
import com.superschool.entity.User;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by xiaohao on 17-11-2.
 */

public class JMsg {
    private static final int LOGINED = 820;
    private Context context;
    static JMsg jMsg = null;

    public JMsg(Context context) {
        this.context = context;
    }

    public static void init(Context context) {
        JMessageClient.init(context);
    }

    public static JMsg instance(Context context) {
        if (jMsg == null) {
            jMsg = new JMsg(context);
        }
        return jMsg;
    }


    class JBasicCallback extends BasicCallback {
        private boolean isSuccess = false;

        public boolean isSuccess() {
            return this.isSuccess;
        }


        @Override
        public void gotResult(int i, String s) {
            if (i == 0) {
                this.isSuccess = true;
                System.out.println("注册成功");


            }

        }
    }


    public void register(final User user, final RegisterActivity registerActivity) {


        System.out.println("正在注册");

        JMessageClient.register(user.getUsername(), user.getUserPassword(), new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {


                if (i == 0) {
                    JMessageClient.login(user.getUsername(), user.getUserPassword(), new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                UserInfo userInfo = JMessageClient.getMyInfo();

                                user.setUserId(String.valueOf(userInfo.getUserID()));
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String url = "http://www.sinbel.top/study/public/index.php/user/user/register";
                                        MOkHttp mOkHttp = new MOkHttp();
                                        String rs = mOkHttp.register(user, url);
                                        if (!rs.equals("")) {
                                            System.out.println(rs);
                                            registerActivity.gotoSuccess();
                                        }

                                    }
                                }).start();

                            }
                        }
                    });
                }

               /* */
            }
        });


    }

    public void login(final User user, final LoginActivity loginActivity) {
        System.out.println("正在登录");
        JMessageClient.login(user.getUsername(), user.getUserPassword(), new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    //登录成功从服务器拉回信息=>通过接口的形式获取json
                    //  loginActivity.gotoSuccess(user);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String url = "http://www.sinbel.top/study/public/index.php/user/user/login";
                            MOkHttp mOk = new MOkHttp();
                            String rs = mOk.login(user, url);
                            System.out.println(rs);
                            JSONArray obj = JSON.parseArray(rs);
                            JSONObject json = JSON.parseObject(obj.get(0).toString());
                            loginActivity.gotoSuccess(json);
                        }
                    }).start();

                }
            }
        });
    }



}
