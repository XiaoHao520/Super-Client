package com.superschool.fragments;

import android.content.Intent;
import android.graphics.BitmapFactory;
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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.superschool.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.android.volley.toolbox.NetworkImageView;
import com.superschool.activity.CardsActivity;
import com.superschool.tools.FileUpload;
import com.superschool.tools.LruImageCache;

/**
 * Created by xiaohao on 17-10-16.
 */

public class FrameTwo extends Fragment {
    private WebView web;
    private com.android.volley.toolbox.NetworkImageView iv;
    private String url = "http://www.sinbel.top/study/public/index.php/mobile/index/index";
    private static final int MAKE_NOTE = 1;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f2_layout, container, false);
        initView();
        return view;
    }

    private void initView() {
        web = (WebView) view.findViewById(R.id.web);
        WebSettings ws = web.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setJavaScriptEnabled(true);
        web.addJavascriptInterface(new JsCall(),"jsCall");
        web.loadUrl(url);
    }

    class JsCall{

        @JavascriptInterface
        public void makeNote(){
            Intent intent=new Intent(getActivity(), CardsActivity.class);
            startActivityForResult(intent,MAKE_NOTE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
         if(requestCode==1&&resultCode==1){
             //获取返回的数据
             System.out.println(data.getStringExtra("content"));
             ArrayList<String> photos=data.getStringArrayListExtra("photos");
              FileRunnable fileRunnable=new FileRunnable(photos);
             new Thread(fileRunnable).start();

         }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class FileRunnable implements Runnable{
        private ArrayList<String> photos;
        public FileRunnable(ArrayList<String> photos) {
            this.photos = photos;
        }

        @Override
        public void run() {

            try {
                FileUpload.uploadMulti(photos,getContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
