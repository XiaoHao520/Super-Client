package com.superschool.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.superschool.R;
import com.superschool.data.Comment;
import com.superschool.tools.MOkHttp;

import java.util.List;
import java.util.Map;

import static com.superschool.R.id.header;

public class NoteDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView commnetList;
    private List<Map<String, Object>> comments;
    private WebView web;
    private static WebSettings webSettings;
    private ImageView newComment;
    private Intent intent;
    private static final int TO_SEND_COMMENT = 564;
    private String userId;
    private SharedPreferences shared;
    private String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        intent = getIntent();
        init();
    }

    private void init() {
        noteId = intent.getStringExtra("noteId");
        shared = this.getSharedPreferences("localUser", MODE_PRIVATE);
        userId = shared.getString("userid", null);
        newComment = (ImageView) findViewById(R.id.newComment);

        newComment.setOnClickListener(this);
        comments = new Comment().getCommentList();
        commnetList = (ListView) findViewById(R.id.commentList);
        SimpleAdapter adapter = new SimpleAdapter(this, comments, R.layout.comment_item, new String[]{"nickname", "content", "date"}, new int[]{R.id.nickname, R.id.content, R.id.date});
        commnetList.setAdapter(adapter);
        web = (WebView) findViewById(R.id.web);

        webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.loadUrl("http://www.sinbel.top/study/public/index.php/mobile/index/onenote?noteid=3");

    }

    @Override
    public void onClick(View v) {
        if (newComment == v) {


            Intent toCommentIntent = new Intent(this, MakeCommentActivity.class);

            startActivityForResult(toCommentIntent, TO_SEND_COMMENT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TO_SEND_COMMENT) {
            if (resultCode == 162) {
                final String content = data.getStringExtra("content");
             final String url="http://www.sinbel.top/study/public/index.php/mobile/index/makeComment";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MOkHttp ok=new MOkHttp();
                        ok.sendComment(userId,noteId,content,url);
                    }
                }).start();
            }
        }
    }
}
