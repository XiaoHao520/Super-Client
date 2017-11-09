package com.superschool.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by xiaohao on 17-11-9.
 */

public class Store extends DataSupport{
    private String storeName;
    private String storeDsc;
    private String storeAddress;
    private String lat;
    private String lon;
    private String type;
    private String storeHolder;

    private String school;


    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getStoreHolder() {
        return storeHolder;
    }

    public void setStoreHolder(String storeHolder) {
        this.storeHolder = storeHolder;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreDsc() {
        return storeDsc;
    }

    public void setStoreDsc(String storeDsc) {
        this.storeDsc = storeDsc;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeName='" + storeName + '\'' +
                ", storeDsc='" + storeDsc + '\'' +
                ", storeAddress='" + storeAddress + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", type='" + type + '\'' +
                ", storeHolder='" + storeHolder + '\'' +
                ", school='" + school + '\'' +
                '}';
    }
}
