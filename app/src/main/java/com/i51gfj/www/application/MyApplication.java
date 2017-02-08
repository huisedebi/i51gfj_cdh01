package com.i51gfj.www.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.sdk.android.trade.TradeConfigs;
import com.baidu.mapapi.SDKInitializer;
import com.i51gfj.www.DemoContext;
import com.i51gfj.www.manager.ModelManager;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.qyx.android.weight.utils.baidumap.BaiduLoacationUtil;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.LocationInputProvider;
import io.rong.imkit.widget.provider.TextInputProvider;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import io.rong.push.RongPushClient;

/**
 * 初始化
 * Created by Administrator on 2015/12/31.
 */
public class MyApplication extends Application implements RongIM.UserInfoProvider{
    static MyApplication application;
    private BaiduLoacationUtil mBaiduLoacationUtil;
    private static MyApplication mApplication = null;
    /**
     * 保存所有Activity页面
     */
    /**
     * 通知数量
     **/
    private static Context appContext;

    public static MyApplication getAppplication() {
        if (application != null) {
            return application;
        } else {
            application = new MyApplication();
            return application;
        }

    }

    public static MyApplication getInstance() {
        if (mApplication == null) {
            mApplication = new MyApplication();
        }
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        /**
         *
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            //集成使用小米推送
            RongPushClient.registerMiPush(getApplicationContext(),"2882303761517521257","5301752198257");
            RongIM.init(this);
                DemoContext.init(this);
                RongIM.setUserInfoProvider(this, true);//设置用户信息提供者。
        }
        //初始化ModelManager
        ModelManager.getInstance().initialize(this);
        //初始化ImageLoader
        initImageLoader(this);
//        JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(getApplicationContext());            // 初始化 JPush
        // Log.i("getRegistrationID",JPushInterface.getRegistrationID(this)) ;

        //初始化百度地图
        SDKInitializer.initialize(this);
        //初始化Bugly
//        CrashReport.initCrashReport(this, "900017158", false);
        /*TradeConfigs.defaultItemDetailWebViewType = TradeConstants.BAICHUAN_H5_VIEW;
        TradeConfigs.defaultTradeProcessCallback = new TradeProcessCallback() {

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                Log.e("onFailure", "onFailure");
            }

            @Override
            public void onPaySuccess(TradeResult arg0) {
                // TODO Auto-generated method stub
                Log.e("onPaySuccess", "onPaySuccess");
            }

        };*/
        TradeConfigs.defaultTaokePid = "mm_112832948_0_0";
        TextInputProvider textInputProvider = new TextInputProvider(RongContext.getInstance());
        RongIM.setPrimaryInputProvider(textInputProvider);

        //			        扩展功能自定义
        InputProvider.ExtendProvider[] provider = {
                new ImageInputProvider(RongContext.getInstance()),//图片
                new CameraInputProvider(RongContext.getInstance()),//相机
                new LocationInputProvider(RongContext.getInstance()),//地理位置
        };
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.GROUP, provider);
       /* AlibabaSDK.asyncInit(getApplicationContext(), new InitResultCallback() {

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
//				ToastUtils.show("初始化异常");
            }

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
//				ToastUtils.show("初始化成功");
            }
        });*/
    }
    @Override
    public UserInfo getUserInfo(String userId) {

        if (DemoContext.getInstance() == null)
            return null;

        return DemoContext.getInstance().getUserInfoByUserId(userId);
    }
    public BaiduLoacationUtil GetBaiduLoacationUtil() {
        if (mBaiduLoacationUtil == null) {
            mBaiduLoacationUtil = BaiduLoacationUtil
                    .getInstance(getAppContext());
        }
        return mBaiduLoacationUtil;
    }

    public static Context getAppContext() {
        return MyApplication.appContext;
    }

    private void initImageLoader(Context context) {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .threadPoolSize(5).build();
        ImageLoader.getInstance().init(config);
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }
    //下面三个为好友列表四个账号中的三个账号信息，分别为:用户userid，token，登录邮箱，密码，昵称
/**
 * 56146
 * rjlNearK6Qzu3MxMbMrQPLI6ZiT8q7s0UEaMPWY0lMwZFCr6nvpDtW4C3cjQ9tv4cIQMvwbkJb6xzmBRkFjv+Q==
 * 112@rongcloud.com
 * 123456
 * rongcloud112
 */

/**
 * 56147
 * ke0BCyqQnwWiagWoS1ckzrI6ZiT8q7s0UEaMPWY0lMwZFCr6nvpDtX2/KvQEWEDo6r3YdoOyCDqUgN53uY4SEA==
 * 113@rongcloud.com
 * 123456
 * rongcloud113
 */

/**
 * 56148
 * mYLERi6S6fkqdGjEIJrEubI6ZiT8q7s0UEaMPWY0lMwZFCr6nvpDtQIt9svl5Wir1X0Zf3Hy5T1QRLmZJrAbgQ==
 * 114@rongcloud.com
 * 123456
 * rongcloud114
 */



}
