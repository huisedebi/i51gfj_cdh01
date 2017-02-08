package com.i51gfj.www.receiver;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.i51gfj.www.activity.MainActivity;
import com.i51gfj.www.activity.WelcomeActivity;
import com.i51gfj.www.util.Util;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Administrator on 2016/10/28.
 */

public class NotificationReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
//        Toast.makeText(context, "消息来了", Toast.LENGTH_SHORT).show();
        Log.i("MessageArrived", "onNotificationMessageArrived: "+pushNotificationMessage.getPushTitle());
        Handler mmhandler = Util.handler;
        if (mmhandler != null) {
            Message message = new Message();
            message.what = 999;
            mmhandler.sendMessage(message);
        }
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {
        Log.i("MessageClicked", "onNotificationMessageClicked: "+pushNotificationMessage.getPushContent());
//        Toast.makeText(context, "点击通知栏了", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, WelcomeActivity.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }
}
