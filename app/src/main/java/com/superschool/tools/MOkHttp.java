package com.superschool.tools;

import com.superschool.entity.Note;
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
        RequestBody body = new FormBody.Builder().add("username", user.getUsername()).add("password", user.getUserPassword()).build();
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

    public void sendNote(Note note,String url) throws IOException {
        RequestBody body = new FormBody.Builder().add("userid", note.getUserId()).add("date", note.getDate())
                .add("content",note.getContent()).add("images",note.getImages()).build();
        Request request=new Request.Builder().url(url).post(body).build();
        Call call=okHttpClient.newCall(request);
        Response response=call.execute();


    }
}
