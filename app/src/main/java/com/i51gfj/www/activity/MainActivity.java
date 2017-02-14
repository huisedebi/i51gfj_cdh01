package com.i51gfj.www.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.i51gfj.www.R;
import com.i51gfj.www.application.MyApplication;
import com.i51gfj.www.fragment.IndexFragment;
import com.i51gfj.www.fragment.MineFragment;
import com.i51gfj.www.fragment.MineSendRedFragment;
import com.i51gfj.www.fragment.MsgFragment;
import com.i51gfj.www.model.UserInfo;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.ShpfUtil;
import com.i51gfj.www.util.Util;
import com.i51gfj.www.view.widget.DragPointView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class MainActivity extends FragmentActivity {
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private int index;//fragmentList的下标，当前片段的位置
    public TextView tv_tab,tv_main;
    private TextView mine_tab;
    private TextView shop_tab;
    private TextView msg_tab;
    private boolean is_logining = false;
    public List<String > tag_list = new ArrayList<>();

    private IndexFragment indexFragment;//首页
    private MineFragment mineFragment;//我的
    private MsgFragment msgFragment;//信息
    //private ShopFragment shopFragment;//商品
    private MineSendRedFragment sendRedFragment;
    private ConversationListFragment conversationListFragment;//回话界面

    private boolean autoConnect = false;//非自动登录的自动链接
    private DragPointView mUnreadNumView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);
        initView();

    }

    private void initView() {
        tv_tab = (TextView) findViewById(R.id.tv_tab01);
        tv_main =(TextView) findViewById(R.id.tv_tab01);
        shop_tab = (TextView) findViewById(R.id.tv_tab02);
        mine_tab = (TextView) findViewById(R.id.tv_tab03);
        msg_tab = (TextView) findViewById(R.id.tv_tab04);
        mUnreadNumView = (DragPointView) findViewById(R.id.seal_num);
        Util.handler = mHandler;
        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE
        };
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RongIM.getInstance().setOnReceiveUnreadCountChangedListener(mCountListener, conversationTypes);
            }
        }, 500);
        tv_tab.setSelected(true);

        // 首页
        indexFragment = new IndexFragment();
        // 信息
        msgFragment = new MsgFragment();
        //店铺
        sendRedFragment = new MineSendRedFragment();
        // 我的
        mineFragment = new MineFragment();
        //首页mainFragment、专属limitFragment、定制dnaTest1Fragment1、订单orderFragment、我的mineFragment
        //初始化前三个fragment，后面两个先不初始化
        fragmentList.add(indexFragment);
        fragmentList.add(sendRedFragment);
        fragmentList.add(msgFragment);
        fragmentList.add(mineFragment);

        /**
         * 默认显示首页片段
         */
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content, indexFragment, "index");
        fragmentTransaction.commitAllowingStateLoss();
        index = 0;//初始化选中0

        //如果是自动登录的情况下,异步链接
        UserInfo userInfo = ShpfUtil.getObject(ShpfUtil.LOGIN);
        if(userInfo != null && userInfo.is_remenrber()){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.i("new Thread", "执行了new Thread");
                    UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
                    ShpfUtil.setObject("DEMO_TOKEN", loginData.getToken());
                    autoConnect = true;//非自动登录的自动链接
                    connect(loginData.getToken());
                }
            });
            thread.start();
        }
    }
    public RongIM.OnReceiveUnreadCountChangedListener mCountListener = new RongIM.OnReceiveUnreadCountChangedListener() {
        @Override
        public void onMessageIncreased(int count) {
            if (count == 0) {
                mUnreadNumView.setVisibility(View.GONE);
            } else if (count > 0 && count < 100) {
                mUnreadNumView.setVisibility(View.VISIBLE);
                mUnreadNumView.setText(count + "");
            } else {
                mUnreadNumView.setVisibility(View.VISIBLE);
                mUnreadNumView.setText("···");
            }
        }
    };
    public void changeFragment(View v) {
            if (v.getId() == R.id.tv_tab02 || R.id.tv_tab04 == v.getId()|| v.getId() == R.id.tv_tab03) {
                if (Util.need_to_login(this)) {
                    return;
                }
                else if (!AppUtil.is_ry_connet) {
                    UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
                    ShpfUtil.setObject("DEMO_TOKEN", loginData.getToken());
                    connect(loginData.getToken());
                }
            }
            String fragmentTag = "";
            //点击前的tab设置selected为false
            tv_tab.setSelected(false);
            //当前tab
            v.setSelected(true);
            //把当前tab存到tv里面
            tv_tab = (TextView) v;
            switch (v.getId()) {
                case R.id.tv_tab01:
                    if (index == 0) {
                        return;
                    }
                    index = 0;
                    if (indexFragment == null) {
                        indexFragment = new IndexFragment();
                        fragmentList.set(0, indexFragment);
                    }
                    fragmentTag = "index";
                    break;
                case R.id.tv_tab02:
                    if (index == 1) {
                        return;
                    }
                    index = 1;
                    if (sendRedFragment == null) {
                        sendRedFragment = new MineSendRedFragment();
                        fragmentList.set(1, sendRedFragment);
                    }
                    fragmentTag = "send";
                    break;
                case R.id.tv_tab03:
                    if (index == 2) {
                        return;
                    }
                    index = 2;
                    if (msgFragment == null) {
                        msgFragment = new MsgFragment();
                        fragmentList.set(2, msgFragment);
                    }
                    fragmentTag = "msg";
                    break;
                case R.id.tv_tab04:

                    index = 3;
                    if (mineFragment == null) {
                        mineFragment = new MineFragment();
                        fragmentList.set(3, mineFragment);
                    }
                    fragmentTag = "mine";
                    break;
                default:
                    break;
            }
         /*private IndexFragment indexFragment;//首页
        private MineFragment mineFragment;//我的
        private MsgFragment msgFragment;//信息
        private ShopFragment shopFragment;//商品*/
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!fragmentList.get(index).isAdded()) { // 先判断是否被add过
                transaction.add(R.id.content, fragmentList.get(index), fragmentTag); // 隐藏当前的fragment，add下一个到Activity中
            }
            if (index == 0) {//首页
                transaction.hide(msgFragment).hide(mineFragment).hide(sendRedFragment).show(indexFragment);
            } else if (index == 1) {
                transaction.hide(msgFragment).hide(mineFragment).hide(indexFragment).show(sendRedFragment);
            } else if (index == 2) {
                transaction.hide(indexFragment).hide(mineFragment).hide(sendRedFragment).show(msgFragment);
            } else {//我的
                transaction.hide(indexFragment).hide(sendRedFragment).hide(msgFragment).show(mineFragment);
            }
            transaction.commitAllowingStateLoss();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 170 && resultCode==205){
            is_logining = true;
            if(index == 3){
                mineFragment.setUI(mineFragment.data);
            }
        }
        if(data != null){
            if(requestCode == 11 && resultCode==92014){
                indexFragment.chooseCity(data.getStringExtra("city"));
            }
        }
        if(requestCode == 1515 && resultCode==2917){
            MineFragment f = (MineFragment) getSupportFragmentManager().findFragmentByTag("mine");
            f.onActivityResult(requestCode, 2917, data);
        }
        if(requestCode==63 && resultCode==RESULT_OK){
            MineFragment f = (MineFragment) getSupportFragmentManager().findFragmentByTag("mine");
            f.onActivityResult(requestCode, RESULT_OK, data);
        }if(requestCode==3000 && resultCode ==2917){
            if(sendRedFragment!=null){
                ArrayList<String> Imgresults = data.getStringArrayListExtra(PhotoPickerActivity.SELECT_RESULTS_ARRAY);
                sendRedFragment.changeAdapte(Imgresults);
            }
        }if(requestCode ==105 && resultCode == RESULT_OK){
            MineSendRedFragment f = (MineSendRedFragment) getSupportFragmentManager().findFragmentByTag("send");
            f.onActivityResult(requestCode, 102, data);
        }
    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */

    private void connect(String token) {

        if (this.getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(this.getApplicationContext()))) {


/**
 * IMKit SDK调用第二步,建立与服务器的连接
 */

            RongIM.connect(token, new RongIMClient.ConnectCallback() {


                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */

                @Override
                public void onTokenIncorrect() {

                    Log.d("LoginActivity", "--onTokenIncorrect");
                }


                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */

                @Override
                public void onSuccess(String userid) {
                    AppUtil.is_ry_connet = true;
//                    Log.d("LoginActivity", "--onSuccess" + userid);
//                    if (RongIM.getInstance() != null)
//                        RongIM.getInstance().startConversationList(getApplicationContext());
                    if(!autoConnect) {//非自动登录的自动链接
                        startActivity(new Intent(MainActivity.this, ConversationListActivity.class));
                        RongIM.getInstance().setMessageAttachedUserInfo(true);
                    }
                }


                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 *                  http://www.rongcloud.cn/docs/android.html#常见错误码
                 */

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.d("LoginActivity", "--onError" + errorCode);
                }
            });
        }
    }
    /**
     * 投稿回调
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 999:
                    int count = Util.totalUnreadCount;
                    if (count == 0) {
                        mUnreadNumView.setVisibility(View.GONE);
                    } else if (count > 0 && count < 100) {
                        mUnreadNumView.setVisibility(View.VISIBLE);
                        mUnreadNumView.setText(count + "");
                    } else {
                        mUnreadNumView.setVisibility(View.VISIBLE);
                        mUnreadNumView.setText("···");
                    }
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
            if (RongIM.getInstance() != null)
                RongIM.getInstance().disconnect();
                finish();
        }
        return false;
    }

   /* @Override
    protected void onDestroy() {
        if (RongIM.getInstance() != null)
            RongIM.getInstance().disconnect(true);
        super.onDestroy();
    }*/
   @Override
   protected void onResume() {
       super.onResume();
       MobclickAgent.onResume(this);
       JPushInterface.onResume(this);

   }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);

    }
}