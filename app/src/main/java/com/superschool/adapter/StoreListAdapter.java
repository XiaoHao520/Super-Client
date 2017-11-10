package com.superschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSONObject;
import com.superschool.R;
import com.superschool.entity.Store;
import com.superschool.tools.MOkHttp;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by xiaohao on 17-11-9.
 */

public class StoreListAdapter extends BaseAdapter {

    private String url = "";
    private List<JSONObject> data;
    private Context context;


    public StoreListAdapter(List<JSONObject> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.store_item, null);
        TextView storename = (TextView) layout.findViewById(R.id.storename);
        TextView nums = (TextView) layout.findViewById(R.id.nums);
        Button del = (Button) layout.findViewById(R.id.del);
        final JSONObject json = data.get(position);
        storename.setText(json.get("store_name").toString());
        DataSupport.deleteAll(Store.class,"storeName=?",json.get("store_name").toString());

        Store store=new Store();
        store.setStoreName(json.get("store_name").toString());
        store.setStoreHolder(json.get("store_holder").toString());
        store.save();
       del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                StoreListAdapter.this.notifyDataSetChanged();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MOkHttp ok = new MOkHttp();
                        String url = "http://www.sinbel.top/study/public/index.php/store/index/del";
                        ok.delStore(json.get("store_name").toString(), url);
                        DataSupport.deleteAll(Store.class,"storeName=?",json.get("store_name").toString());
                    }
                }).start();
           }
        });
        nums.setText(json.get("store_history_num").toString());
        return layout;
    }
}
