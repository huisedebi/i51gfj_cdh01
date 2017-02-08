
package com.i51gfj.www.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.i51gfj.www.DemoContext;
import com.i51gfj.www.R;
import com.i51gfj.www.application.MyApplication;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.model.UserInfo;
import com.i51gfj.www.util.ShpfUtil;
import com.i51gfj.www.util.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Bob on 15/8/18.
 * 会话列表
 */

public class ConversationListActivity extends FragmentActivity implements RongIM.UserInfoProvider {

    private TextView mTitle;
    private RelativeLayout mBack;
    private List<UserInfo> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversationlist);
        setActionBarTitle();
        isReconnect();
        RongIM.setUserInfoProvider(this,true);
        getUnreadNum();
        enterFragment();
    }

    private void getUnreadNum() {
        RongIMClient.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                int totalUnreadCount = integer;
                Util.totalUnreadCount = integer;
                Handler mmhandler = Util.handler;
                if (mmhandler != null) {
                    Message message = new Message();
                    message.what = 999;
                    mmhandler.sendMessage(message);
                }
                Log.i("UnreadNum", "onSuccess: "+integer);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }


    /**
     * 加载 会话列表 ConversationListFragment
     */

    private void enterFragment() {

        ConversationListFragment fragment = (ConversationListFragment) getSupportFragmentManager().findFragmentById(R.id.conversationlist);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();

        fragment.setUri(uri);
    }


/**
     * 设置 actionbar 事件
     */

    private void setActionBarTitle() {

        mTitle = (TextView) findViewById(R.id.txt1);
        mBack = (RelativeLayout) findViewById(R.id.back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


/**
     * 判断消息是否是 push 消息
     */

    private void isReconnect() {

        Intent intent = getIntent();
        String token = null;

        if (DemoContext.getInstance() != null) {
//            token = DemoContext.getInstance().getSharedPreferences().getString("DEMO_TOKEN", "default");
            token = ShpfUtil.getObject("DEMO_TOKEN");
            if(token == null){
                token = "default";
            }
        }

        //push，通知或新消息过来
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {
            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("push") != null
                    && intent.getData().getQueryParameter("push").equals("true")) {

                reconnect(token);
            } else {
                //程序切到后台，收到消息后点击进入,会执行这里
                if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {

                    reconnect(token);
                } else {
                    enterFragment();
                }
            }
        }
    }


/**
     * 重连
     *
     * @param token
     */

    private void reconnect(String token) {

        if (getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {

                }

                @Override
                public void onSuccess(String s) {

                    enterFragment();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }
    private void post_user_info(String uid) {
        JSONObject json = new JSONObject();
        try {
            json.put("uid",uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .postString()
                .url(MyURL.URL_USER_INFO)
                .content(json.toString())
                .build()
                .execute(new Callback<UserInfo>() {
                    @Override
                    public UserInfo parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, UserInfo.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                    }

                    @Override
                    public void onResponse(final UserInfo response) {
                        if(response!=null){
                            userList.add(response);
                        }
                    }


                });
    }

    @Override
    public io.rong.imlib.model.UserInfo getUserInfo(String s) {
        if(userList.size()==0){
            post_user_info(s);
            return null;
        }
        for(int i =0,n=userList.size();i<n;i++){
            if(s.equals(userList.get(i).getUid())){
                return new io.rong.imlib.model.UserInfo(s,userList.get(i).getUserName(),Uri.parse(userList.get(i).getHeadImg()));
            }else{
                post_user_info(s);
            }
        }
        return null;
    }
}

