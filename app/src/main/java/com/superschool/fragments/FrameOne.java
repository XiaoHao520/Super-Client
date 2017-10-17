package com.superschool.fragments;

import android.app.DownloadManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.superschool.R;

/**
 * Created by xiaohao on 17-10-16.
 */

public class FrameOne extends Fragment {
    private String url = "http://www.sinbel.top/super/";
    private WebView webView;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.f1_layout, container, false);
        initView();
        return view;
    }

    private void initView() {
        webView = (WebView) view.findViewById(R.id.web);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient(){

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                view.loadUrl(request.getUrl().getPath());
                return true;
            }
        });
        webView.loadUrl(url);
    }
}
