package com.superschool.fragments;

import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.superschool.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.android.volley.toolbox.NetworkImageView;
import com.superschool.tools.LruImageCache;

/**
 * Created by xiaohao on 17-10-16.
 */

public class FrameTwo extends Fragment {

    private com.android.volley.toolbox.NetworkImageView iv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f2_layout, container, false);
        iv = (NetworkImageView) view.findViewById(R.id.iv);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        LruImageCache lruImageCache = LruImageCache.instance();
        ImageLoader imageLoader = new ImageLoader(requestQueue, lruImageCache);
        iv.setImageUrl("http://upload.chinaz.com/2016/0506/1462500414807.jpg", imageLoader);
        return view;
    }
}
