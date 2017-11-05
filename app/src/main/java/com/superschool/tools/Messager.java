package com.superschool.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.superschool.fragments.FrameThree;

/**
 * Created by XIAOHAO-PC on 2017-11-05.
 */

public class Messager extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(intent.getStringExtra("key"));
        FrameThree.show();


    }
}
