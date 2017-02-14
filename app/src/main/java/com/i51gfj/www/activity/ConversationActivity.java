
package com.i51gfj.www.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.i51gfj.www.model.MineIndexWrapper;
import com.i51gfj.www.util.Tool;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.LocationMessage;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Bob on 15/8/18.
 * 会话页面
 */

public class ConversationActivity extends FragmentActivity implements RongIM.LocationProvider, RongIM.ConversationBehaviorListener {

    private TextView mTitle;
    private RelativeLayout mBack;

    private String mTargetId;
    private TextView detail_store;


    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */

    private String mTargetIds;


    /**
     * 会话类型
     */

    private Conversation.ConversationType mConversationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
        RongIM.setLocationProvider(this);
        RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
        Intent intent = getIntent();
        if (intent == null || intent.getData() == null){
            return;
        }
        //intent.getData().getLastPathSegment();//获得当前会话类型
        mConversationType = Conversation.ConversationType.valueOf(intent.getData()
                .getLastPathSegment().toUpperCase(Locale.getDefault()));

        setActionBar();

        getIntentDate(intent);

        isReconnect(intent);
    }


    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */

    private void getIntentDate(Intent intent) {

        mTargetId = intent.getData().getQueryParameter("targetId");
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        String title = intent.getData().getQueryParameter("title");
         getSid(mTargetId);

        //intent.getData().getLastPathSegment();//获得当前会话类型
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        enterFragment(mConversationType, mTargetId);
        setActionBarTitle(title);
    }

    private void getSid(String mTargetId) {
            JSONObject json = new JSONObject();
            try {
                json.put("uid", mTargetId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            OkHttpUtils
                    .postString()
                    .url(MyURL.URL_MINE_INDEX)
                    .content(json.toString())
                    .build()
                    .execute(new Callback<MineIndexWrapper>() {
                        @Override
                        public MineIndexWrapper parseNetworkResponse(Response response) throws IOException {
                            String json = response.body().string();
                            return new Gson().fromJson(json, MineIndexWrapper.class);
                        }

                        @Override
                        public void onError(Call call, Exception e) {
                            e.printStackTrace();
                            Log.e("onError", e.toString(), e);
                        }

                        @Override
                        public void onResponse(final MineIndexWrapper response) {
                            if (response.getStatus() == 1) {
                                if(response.getStoreId()==null||response.getStoreId().equals("0")){
                                    detail_store.setVisibility(View.GONE);
                                }else{
                                    detail_store.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(ConversationActivity.this,ShopDetailActivity.class);
                                            intent.putExtra("passData",response.getStoreId());
                                            ConversationActivity.this.startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            }
                        }
                    });
    }


    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType
     * @param mTargetId
     */

    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();
        fragment.setUri(uri);
    }


    /**
     * 判断消息是否是 push 消息
     */

    private void isReconnect(Intent intent) {


        String token = null;

        if (DemoContext.getInstance() != null) {

            token = DemoContext.getInstance().getSharedPreferences().getString("DEMO_TOKEN", "default");
        }

        //push或通知过来
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
                    enterFragment(mConversationType, mTargetId);
                }
            }
        }
    }


    /**
     * 设置 actionbar 事件
     */

    private void setActionBar() {

        mTitle = (TextView) findViewById(R.id.txt1);
        mBack = (RelativeLayout) findViewById(R.id.back);
        detail_store = (TextView) findViewById(R.id.detail_store);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 设置 actionbar title
     */

    private void setActionBarTitle(String targetid) {

        mTitle.setText(targetid);
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

                    enterFragment(mConversationType, mTargetId);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }

    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        // TODO Auto-generated method stub
        if (message.getContent() instanceof LocationMessage) {
           /* Intent intent = new Intent(ConversationActivity.this, MapLocationActivity.class);
            intent.putExtra("location", message.getContent());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
            Intent intent = new Intent(ConversationActivity.this,BNDemoMainActivity.class);
            intent.putExtra("store_lat",""+((LocationMessage) message.getContent()).getLat());
            intent.putExtra("store_lng",""+((LocationMessage) message.getContent()).getLng());
            intent.putExtra("store_name","");
            intent.putExtra("store_address", "");
            startActivity(intent);
        }
        return false;
    }

    @Override
    public boolean onMessageLinkClick(Context context, String s) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }

    @Override
    public void onStartLocation(Context context, LocationCallback locationCallback) {
        Tool.mLastLocationCallback = locationCallback;
        Intent intent = new Intent(context, MapLocationActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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

