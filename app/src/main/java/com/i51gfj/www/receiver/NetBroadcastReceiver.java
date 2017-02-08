package com.i51gfj.www.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.i51gfj.www.impl.MyCallBack;


/**
 * 网络变化接收器
 * Created by Administrator on 2016/1/4.
 */
public class NetBroadcastReceiver extends BroadcastReceiver
{
    protected static MyCallBack baseInterface;
    protected final String NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(NET_CHANGE_ACTION))
        {
//            baseInterface.onCallBack(AppUtil.isNetworkAvailable());
        }
    }

    public static void setListener(Activity listener)
    {
        baseInterface = (MyCallBack)listener;
    }
}
