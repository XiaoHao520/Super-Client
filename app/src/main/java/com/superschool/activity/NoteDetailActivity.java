package com.superschool.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.superschool.R;
import com.superschool.data.Comment;

import java.util.List;
import java.util.Map;

import static com.superschool.R.id.header;

public class NoteDetailActivity extends AppCompatActivity {

    private ListView commnetList;
    private List<Map<String, Object>> comments;
    private WebView web;
    private static WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        comments = new Comment().getCommentList();
        commnetList = (ListView) findViewById(R.id.commentList);
        SimpleAdapter adapter = new SimpleAdapter(this, comments, R.layout.comment_item, new String[]{"nickname", "content", "date"}, new int[]{R.id.nickname, R.id.content, R.id.date});
        commnetList.setAdapter(adapter);
        web = (WebView) findViewById(R.id.web);

        webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.loadUrl("http://www.sinbel.top/study/public/index.php/mobile/index/onenote?noteid=3");

    }
}
