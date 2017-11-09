package com.superschool.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superschool.entity.Note;
import com.superschool.entity.Store;
import com.superschool.entity.User;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by XIAOHAO-PC on 2017-10-29.
 */

public class MOkHttp {
    private static OkHttpClient okHttpClient;

    public MOkHttp() {
        this.okHttpClient = new OkHttpClient();
    }

    public String register(User user, String url) {

        System.out.println(" eddddddddddddddddddddddddddddddddddd" + user.getUserHeader());
        RequestBody body = new FormBody.Builder()
                .add("username", user.getUsername())
                .add("email", user.getUserEmail())
                .add("password", user.getUserPassword())
                .add("userid", user.getUserId())
                .add("userHeader", user.getUserHeader()).build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String rs = response.body().string();
            return rs;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String login(User user, String url) {


        RequestBody body = new FormBody.Builder()
                .add("username", user.getUsername())
                .add("password", user.getUserPassword())
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = okHttpClient.newCall(request);

        Response response = null;
        try {
            response = call.execute();

            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    public void sendNote(Note note, String url) throws IOException {
        RequestBody body = new FormBody.Builder().add("userid", note.getUserId()).add("date", note.getDate())
                .add("content", note.getContent()).add("images", note.getImages()).build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        System.out.println("返回" + response.body().string());

    }

    public String modifyUser(User user, String url) throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("userid", user.getUserId())
                .add("userheader", user.getUserHeader().toString())
                .add("nickname", user.getUserNickName())
                .add("school", user.getUserSchool())
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        return response.body().string();

    }

    public void like(String url, String myUsername, String noteUser, String noteId) {
        RequestBody body = new FormBody.Builder().add("from", myUsername)
                .add("to", noteUser).add("noteid", noteId).build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public String applyStore(Store store, String url) {
        RequestBody body = new FormBody.Builder()
                .add("username", store.getStoreHolder())
                .add("storename", store.getStoreName())
                .add("storeDsc", store.getStoreDsc())
                .add("storeholder", store.getStoreHolder())
                .add("storeaddress", store.getStoreAddress())
                .add("lat", store.getLat())
                .add("lon", store.getLon())
                .add("type", store.getType())
                .add("school", store.getSchool()).build();


        Request request = new Request.Builder().url(url).post(body).build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            return (response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void delStore(String store, String url) {

        System.out.println(store);
        RequestBody body = new FormBody.Builder().add("store", store).build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = okHttpClient.newCall(request);
        try {
            Response rs = call.execute();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void addStoreHistory(String store,String url){

        RequestBody body=new FormBody.Builder().add("store",store).build();
        Request request=new Request.Builder().url(url).post(body).build();
        Call call=okHttpClient.newCall(request);
        try {
            Response rs=call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONArray getStoreHistory(String holder,String url){
        RequestBody body=new FormBody.Builder().add("holder",holder).build();
        Request request=new Request.Builder().url(url).post(body).build();
        Call call=okHttpClient.newCall(request);
        try {
            Response response=call.execute();
            String rs=response.body().string();
            JSONArray array= JSON.parseArray(rs);
         return array;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }



}
