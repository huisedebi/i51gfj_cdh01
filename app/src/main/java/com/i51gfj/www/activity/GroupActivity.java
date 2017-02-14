package com.i51gfj.www.activity;

import android.app.Activity;
import android.os.Bundle;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Bob on 15/8/20.
 * 加入群组
 * 群组的概念
 * 群组的维护
 * 创建群组是如何做的？谁来做？
 */
public class GroupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
