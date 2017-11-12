package com.superschool.data;

import com.superschool.R;
import com.superschool.tools.Time;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by XIAOHAO-PC on 2017-11-12.
 */

public class Comment {

    private List<Map<String, Object>> comments;

    public List<Map<String, Object>> getCommentList() {

        comments = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("nickname", "nickname");
            map.put("content", "bfmdshfjhdskjhfkdshfdjshkjfdshjfhdskfhdskjjhf");
            map.put("date", Time.getNow());

            comments.add(map);
        }
        return comments;


    }


}
