package com.superschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superschool.R;

import java.util.List;
import java.util.Map;

/**
 * Created by XIAOHAO-PC on 2017-11-12.
 */

public class CommentListAdapter extends BaseAdapter {
    private List<Map<String, String>> commentList;
    private Context context;

    public CommentListAdapter(List<Map<String, String>> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return commentList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout item= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.comment_item,null);
        TextView username,content,date;
        username= (TextView) item.findViewById(R.id.username);
        content= (TextView) item.findViewById(R.id.content);

        return null;
    }
}
