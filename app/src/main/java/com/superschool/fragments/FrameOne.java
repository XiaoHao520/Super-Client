package com.superschool.fragments;

import android.app.DownloadManager;
import android.os.Build;
import android.os.Environment;

import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.superschool.R;
import com.superschool.map.MMap;
import com.superschool.tools.BaseFragment;

/**
 * Created by xiaohao on 17-10-16.
 */

public class FrameOne extends Fragment implements AMapLocationListener, AMap.OnMapClickListener, AMap.OnMarkerClickListener, AMap.OnMarkerDragListener, GeocodeSearch.OnGeocodeSearchListener {
    private static final String TAG = FrameOne.class.getSimpleName();

    private static View view;
    private MapView map;
    private static AMap aMap;
    Bundle instanceState;
    static double lat;
    static double lon;
    private static GeocodeSearch geocodeSearch;
    static Marker pickMarker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.instanceState = savedInstanceState;
        if (view == null) {
            view = inflater.inflate(R.layout.f1_layout, container, false);
            initView(savedInstanceState);
        }
        return view;
    }


    private void initView(Bundle savedInstanceState) {
        map = (MapView) view.findViewById(R.id.map);
        map.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = map.getMap();
        }
        MMap map = new MMap(getActivity());
        map.getMyLocation(this);

        aMap.setMyLocationEnabled(true);
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMarkerDragListener(this);
        geocodeSearch = new GeocodeSearch(getContext());
        geocodeSearch.setOnGeocodeSearchListener(this);

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {

                lat = aMapLocation.getLatitude();
                lon = aMapLocation.getLongitude();


            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();


        map.onPause();
    }

    @Override
    public void onResume() {

        System.out.println("onresume");
        super.onResume();
        map.onResume();

    }

    @Override
    public void onDestroy() {
        map.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    public static void pickPos() {
        System.out.println("选择位置");
        MarkerOptions options = new MarkerOptions();
        options.draggable(true);
        LatLng latLng = new LatLng(lat, lon);
        pickMarker = aMap.addMarker(options.position(latLng));
        pickMarker.setInfoWindowEnable(true);
        Animation animation = new ScaleAnimation(3.0f, 3.0f, 1.0f, 1.0f);
        animation.setDuration(5000);
        animation.setInterpolator(new LinearInterpolator());
        pickMarker.setAnimation(animation);
        pickMarker.startAnimation();
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        System.out.println("marker 点击事件");


        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

        System.out.println("marker 开始拖拽");


    }

    @Override
    public void onMarkerDrag(Marker marker) {

        System.out.println("drag");
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        System.out.println("stop drag");
        System.out.println(marker.getPosition());

        LatLng latLng = marker.getPosition();
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        String formatAddress = regeocodeAddress.getFormatAddress();
        pickMarker.setTitle("hello");
        pickMarker.setSnippet("查询经纬度对应详细地址：" + formatAddress);

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

        System.out.println("map click");
        if (pickMarker.isInfoWindowShown()) {
            System.out.println("map click  1");
            pickMarker.hideInfoWindow();
        }
    }
}
