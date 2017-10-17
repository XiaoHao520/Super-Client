package com.superschool.webview;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.superschool.R;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {


    String url = "http://39.108.178.40/super/";

    private WebView webView;
     private Button refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initView();
    }

    private void initView() {

        refresh= (Button) findViewById(R.id.refresh);
        webView = (WebView) findViewById(R.id.web);
        webView.loadUrl(url);
        WebSettings ws=webView.getSettings();
        ws.setDomStorageEnabled(true);
        ws.setAppCacheMaxSize(1024*1024*8);

        ws.setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        ws.setAllowFileAccess(true);
        ws.setAppCacheEnabled(true);
        ws.setJavaScriptEnabled(true);


        System.out.println(getApplicationContext().getCacheDir().getAbsolutePath());

        webView.setWebViewClient(new WebViewClient(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().getPath());
                return true;
            }
        });
        webView.loadUrl(url);
        refresh.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==refresh.getId()){
            webView.reload();

        }
    }
}
