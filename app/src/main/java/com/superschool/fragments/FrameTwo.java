package com.superschool.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.superschool.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.toolbox.NetworkImageView;
import com.superschool.activity.CardsActivity;
import com.superschool.entity.Note;
import com.superschool.tools.FileUpload;
import com.superschool.tools.LruImageCache;
import com.superschool.tools.MOkHttp;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.model.Message;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

/**
 * Created by xiaohao on 17-10-16.
 */

public class FrameTwo extends Fragment {
    private WebView web;
    private com.android.volley.toolbox.NetworkImageView iv;
    private String url = "http://www.sinbel.top/study/public/index.php/mobile/index/index";
    //  private String url="file:///android_asset/card.html";
    private static final int MAKE_NOTE = 1;
    View view;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f2_layout, container, false);
        initView();
        return view;
    }

    private void initView() {
        sharedPreferences = getActivity().getSharedPreferences("localUser", Context.MODE_PRIVATE);
        web = (WebView) view.findViewById(R.id.web);
        WebSettings ws = web.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setCacheMode(MIXED_CONTENT_ALWAYS_ALLOW);
        ws.setJavaScriptEnabled(true);
        web.addJavascriptInterface(new JsCall(), "jsCall");
        web.loadUrl(url);
    }

    class JsCall {

        @JavascriptInterface
        public void makeNote() {
            String username = sharedPreferences.getString("username", null);
            String school = sharedPreferences.getString("userSchool", null);
            if (username == null) {
                Toast.makeText(getContext(), "请先登录", Toast.LENGTH_LONG).show();

            } else if (school == null) {
                Toast.makeText(getContext(), "请完善个人信息", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(getActivity(), CardsActivity.class);
                startActivityForResult(intent, MAKE_NOTE);
            }


        }

        @JavascriptInterface
        public void likeClick(final String noteId, final String noteUser) {
            final String likeUrl = "http://www.sinbel.top/study/public/index.php/mobile/index/like";


            new Thread(new Runnable() {
                @Override
                public void run() {
                    Date date = new Date();
                    MOkHttp mOkHttp = new MOkHttp();
                    String username = sharedPreferences.getString("username", null);
                    mOkHttp.like(likeUrl, username, noteUser, noteId);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("from", username);
                    map.put("type", "like");
                    map.put("username",sharedPreferences.getString("username",null));
                    map.put("content", "我赞了你咯");
                    map.put("date", String.valueOf(date.getHours()));
                    map.put("header",sharedPreferences.getString("userheader",null));
                    map.put("nickname",sharedPreferences.getString("nickname",null));
                    Message message = JMessageClient.createSingleCustomMessage(noteUser, map);
                    JMessageClient.sendMessage(message);
                }
            }).start();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 1) {
            //获取返回的数据
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("localUser", Context.MODE_PRIVATE);
            String userId = sharedPreferences.getString("userid", null);
            Note note = new Note();
            note.setUserId(userId);
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

            Date date = new Date();
            note.setDate(format.format(date));

            System.out.println("userid:============" + userId);
            note.setContent(data.getStringExtra("content"));
            ArrayList<String> photos = data.getStringArrayListExtra("photos");
            FileRunnable fileRunnable = new FileRunnable(photos, note);
            new Thread(fileRunnable).start();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class FileRunnable implements Runnable {
        private ArrayList<String> photos;
        private Note note;

        public FileRunnable(ArrayList<String> photos, Note note) {
            this.photos = photos;
            this.note = note;
        }

        public FileRunnable(ArrayList<String> photos) {
            this.photos = photos;
        }

        @Override
        public void run() {

            try {

                List<String> cosUrls = FileUpload.uploadMulti(photos, getContext());
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < cosUrls.size(); i++) {
                    String url = cosUrls.get(i);
                    if (url != null) {
                        stringBuilder.append(url + "%");
                    }
                }

                System.out.println(stringBuilder.toString());

                note.setImages(stringBuilder.toString());
                MOkHttp mOkHttp = new MOkHttp();
                String url = "http://www.sinbel.top/study/public/index.php/mobile/index/savenote";
                mOkHttp.sendNote(note, url);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
