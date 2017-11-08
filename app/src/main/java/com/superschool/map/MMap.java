package com.superschool.map;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by xiaohao on 17-11-8.
 */

public class MMap {

    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    public static AMapLocation aMapLocation;
    private Context context;

    public MMap(Context context) {
        this.context = context;
    }

    public  void getMyLocation(AMapLocationListener listener) {

        mlocationClient = new AMapLocationClient(context);

//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(listener);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);


//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);

        mlocationClient.startLocation();



    }


}
