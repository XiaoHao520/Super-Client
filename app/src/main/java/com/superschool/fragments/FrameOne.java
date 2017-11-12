package com.superschool.fragments;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
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
import com.superschool.activity.ApplyStoreActivity;
import com.superschool.activity.StoreDetailActivity;
import com.superschool.entity.Store;
import com.superschool.map.MMap;
import com.superschool.tools.BaseFragment;
import com.superschool.tools.MOkHttp;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaohao on 17-10-16.
 */

public class FrameOne extends Fragment implements AMap.OnInfoWindowClickListener, AMapLocationListener, AMap.OnMapClickListener, AMap.OnMarkerClickListener, AMap.OnMarkerDragListener, GeocodeSearch.OnGeocodeSearchListener {

    private static View view;
    private MapView map;
    private static AMap aMap;
    Bundle instanceState;
    static double lat;
    static double lon;
    private static GeocodeSearch geocodeSearch;
    static Marker pickMarker;
    private static final int APPLY = 256;
    private String address;
    private static String school;
    private static SharedPreferences sharedPreferences;
    private static Map<String, String> storeInfo;
    private static String username;
    private List<Marker> markerList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.instanceState = savedInstanceState;


        System.out.println("----------------------------------------------");

        sharedPreferences = getActivity().getSharedPreferences("localUser", Context.MODE_PRIVATE);
        school = sharedPreferences.getString("school", null);
        username = sharedPreferences.getString("username", null);
        storeInfo = new HashMap<String, String>();
        markerList = new ArrayList<Marker>();

        if (view == null) {

            view = inflater.inflate(R.layout.f1_layout, container, false);
            initView(savedInstanceState);
            loadStore();
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
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        aMap.setMyLocationEnabled(true);
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMarkerDragListener(this);
        aMap.setOnInfoWindowClickListener(this);
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


        if (pickMarker != null) {


            if (pickMarker.isInfoWindowShown()) {
                pickMarker.hideInfoWindow();
            } else {
                pickMarker.showInfoWindow();
            }

        }

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


        LatLng latLng = marker.getPosition();
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        String formatAddress = regeocodeAddress.getFormatAddress();
        pickMarker.setTitle("您选择地址为:");
        pickMarker.setSnippet("当前地址：" + formatAddress + "\n");
        address = formatAddress;


    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onMapClick(LatLng latLng) {


        System.out.println("-----------------------------");
        if (markerList != null) {
            for (Marker marker : markerList) {
                marker.hideInfoWindow();
            }
        }
        if (pickMarker != null) {
            if (pickMarker.isInfoWindowShown()) {
                System.out.println("map click  1");
                pickMarker.hideInfoWindow();
            }
        }
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        if (marker.getTitle().equals("您选择地址为:")) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("是否将该位置标记为店铺常地址？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getActivity(), ApplyStoreActivity.class);
                    intent.putExtra("address", address);

                    LatLng latLng = marker.getPosition();
                    intent.putExtra("lat", String.valueOf(latLng.latitude));
                    intent.putExtra("lon", String.valueOf(latLng.longitude));
                    startActivityForResult(intent, APPLY);
                }
            }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        } else {
            JSONObject json = (JSONObject) marker.getObject();
            String holder = json.getString("store_holder");
            String dsc = json.getString("store_intro");
            String store = json.getString("store_name");
            if (json.get("store_business").toString().equals("打印")) {


            } else {
                Intent intent = new Intent(getActivity(), StoreDetailActivity.class);
                intent.putExtra("store", store);
                intent.putExtra("dsc", dsc);
                intent.putExtra("holder", holder);
                startActivity(intent);
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == APPLY) {
            if (resultCode == 982) {
                System.out.println("开通成功");
            }
        }


    }

    private void loadStore() {

        Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                JSONArray array = (JSONArray) msg.obj;
                Log.i("stores:", "handleMessage: " + array.toString());
                for (int i = 0; i < array.size(); i++) {
                    JSONObject json = JSON.parseObject(array.get(i).toString());
                    if (json != null) {
                        String storeName = json.getString("store_name");
                        System.out.println("store is exist:::" + DataSupport.isExist(Store.class, "storeName=?", storeName));

                        if (!DataSupport.isExist(Store.class, "storeName=?", storeName)) {
                            Store store = new Store();
                            store.setStoreName(json.getString("store_name"));
                            store.setStoreHolder(json.getString("store_holder"));
                            if(store.save()){
                                System.out.println("保存成功");
                            }else {
                                System.out.println("保存失败");
                            }
                        }
                        LatLng latlng = new LatLng(Double.parseDouble(json.get("store_lat").toString()), Double.parseDouble(json.get("store_lon").toString()));
                        MarkerOptions option = new MarkerOptions();
                        option.position(latlng).title(json.get("store_name").toString()).snippet(json.get("store_intro").toString());
                        option.icon(BitmapDescriptorFactory.fromResource(R.drawable.store));
                        Marker marker = aMap.addMarker(option);
                        marker.setObject(json);
                        markerList.add(marker);
                    }

                }


                super.handleMessage(msg);
            }
        };

        if (school != null) {

            new Thread(new LoadFromServerThread(school, mHandler)).start();
        }


    }

    class LoadFromServerThread implements Runnable {
        String school;
        private Handler handler;

        public LoadFromServerThread(String school, Handler handler) {
            this.school = school;
            this.handler = handler;
        }

        @Override
        public void run() {

            String url = "http://www.sinbel.top/study/public/index.php/store/index/loadStore";
            //okhttp

            MOkHttp okHttp = new MOkHttp();
            JSONArray array = okHttp.loadStore(school, url);
            Message message = new Message();
            message.obj = array;
            handler.sendMessage(message);
        }
    }


    @Override
    public void onPause() {
        super.onPause();

        map.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onresume");


    }

    @Override
    public void onStop() {

        System.out.println("f1 onstop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        view = null;
        System.out.println("f1 ondestory");
        aMap = null;
        map.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onStart() {

        map.onResume();
        super.onStart();
    }

 /*   @Override
    public void onDestroyView() {

        System.out.println("f1 ondestoryview");
        map.onDestroy();
        super.onDestroyView();
    }*/

}
