/*
package com.i51gfj.www.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;


import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.social.UMPlatformData;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class UmengUtils {
    private Handler handlerForMoreInfo;
    private Handler handlerForSign;

    public static String QQ_APPID = "1104406384";
    public static String QQ_APPSECRET = "M8vrF299G0twI2iW";
    public static String WEIXIN_APPID = "wx1ad65cfb17900283";
    public static String WEIXIN_APPSECRET = "4ba0a53373e9643ea26dcc386da90585";

    private Context mContext;
    private UMSocialService mController;
    private boolean clickeabled = false;
    private int position;
    private int signtype = 1;
    private int forgettype = 1;

    public UmengUtils(Context mContext) {
        this.mContext = mContext;
        initData();
    }

    private void initData() {
        mController = UMServiceFactory.getUMSocialService("com.umeng.login");
        // UMSocialService.doOauthVerify(Context mContext , SHARE_MEDIA platform
        // , UMAuthListener listener)
        // 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) mContext,
                QQ_APPID, QQ_APPSECRET);
        qqSsoHandler.addToSocialSDK();
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mContext, WEIXIN_APPID,
                WEIXIN_APPSECRET);
        wxHandler.setRefreshTokenAvailable(false);
        wxHandler.addToSocialSDK();
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(mContext, WEIXIN_APPID,
                WEIXIN_APPSECRET);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        signtype = 1;
        forgettype = 1;
    }

    // 显示分享对话框
    public void showShareDialog(final String title,
                                final String url, final String desc, final String img) {
        final AlertDialog shareAlertDialog = AlertDialogUtils.getInstance(
                (Activity) mContext, R.layout.activity_share);
        shareAlertDialog.setCanceledOnTouchOutside(true);
        Window window = shareAlertDialog.getWindow();
        WindowManager m = ((Activity) mContext).getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams p = shareAlertDialog
                .getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth());
        window.setAttributes(p); // 设置生效
        window.setGravity(Gravity.BOTTOM);
        window.findViewById(R.id.btn_cancel).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        shareAlertDialog.dismiss();
                    }
                });
        window.findViewById(R.id.ll_wenxinhaoyou).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dealShareWeiXin(title, url, desc, img);
                        shareAlertDialog.dismiss();
                    }
                });
        window.findViewById(R.id.ll_pengyouquan)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dealShareWeiXinCircle(title, url, desc, img);
                        shareAlertDialog.dismiss();
                    }
                });
        window.findViewById(R.id.ll_qqhaoyou).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dealShareQQ(title, url, desc, img);
                        shareAlertDialog.dismiss();
                    }
                });
        window.findViewById(R.id.ll_qqkongjian).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dealShareQQZone(title, url, desc, img);
                        shareAlertDialog.dismiss();
                    }
                });

    }

    protected void dealShareQQZone(String title, String url, String desc,
                                   String img) {
        UMPlatformData platform = new UMPlatformData(UMPlatformData.UMedia.TENCENT_QZONE, "");
        platform.setGender(UMPlatformData.GENDER.MALE); //optional
        platform.setWeiboId(QQ_APPID);  //optional
        MobclickAgent.onSocialEvent(mContext, platform);
        // 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        // QQ空间不能使用setTargetUrl，要分享的放在setShareImage，或者拼在setShareContent
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
                (Activity) mContext, QQ_APPID, QQ_APPSECRET);
        qZoneSsoHandler.addToSocialSDK();
        QZoneShareContent qzone = new QZoneShareContent();
        // 设置分享文字
        if (StringUtils.isEmpty(desc)) {
            desc = "来自定制精灵客户端";
        }
        qzone.setShareContent(desc);
        // 设置点击消息的跳转URL
        if (!url.equals(""))
            qzone.setTargetUrl(url);
        // 设置分享内容的标题
        qzone.setTitle(title);
        // 设置分享图片
        qzone.setShareImage(new UMImage(mContext, img));
        mController.setShareMedia(qzone);
        mController.postShare(mContext, SHARE_MEDIA.QZONE,
                new SnsPostListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA platform, int eCode,
                                           SocializeEntity entity) {
                        if (eCode == 200) {
                            shareSuccess();
                        } else {
                        }
                    }
                });
    }

    protected void dealShareQQ(String title, String url, String desc, String img) {
        UMPlatformData platform = new UMPlatformData(UMPlatformData.UMedia.TENCENT_QQ, "");
        platform.setGender(UMPlatformData.GENDER.MALE); //optional
        platform.setWeiboId(QQ_APPID);  //optional
        MobclickAgent.onSocialEvent(mContext, platform);
        // 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) mContext,
                QQ_APPID, QQ_APPSECRET);
        qqSsoHandler.addToSocialSDK();
        QQShareContent qqShareContent = new QQShareContent();
        // 设置分享文字
        if (StringUtils.isEmpty(desc)) {
            desc = "来自定制精灵客户端";
        }
        qqShareContent.setShareContent(desc);
        // 设置分享title
        qqShareContent.setTitle(title);
        // 设置分享图片
        qqShareContent.setShareImage(new UMImage(mContext, img));
        // 设置点击分享内容的跳转链接
        if (!url.equals(""))
            qqShareContent.setTargetUrl(url);
        mController.setShareMedia(qqShareContent);
        mController.postShare(mContext, SHARE_MEDIA.QQ, new SnsPostListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode,
                                   SocializeEntity entity) {
                if (eCode == 200) {
                    shareSuccess();
                } else {
                }
            }
        });
    }

    public void dealShareWeiXinCircle(String title, String url, String desc,
                                      String img) {
        UMPlatformData platform = new UMPlatformData(UMPlatformData.UMedia.WEIXIN_CIRCLE, "");
        platform.setGender(UMPlatformData.GENDER.MALE); //optional
        platform.setWeiboId(WEIXIN_APPID);  //optional
        MobclickAgent.onSocialEvent(mContext, platform);
        // 设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(title);
        // 设置朋友圈title 就是内容
        circleMedia.setTitle(title);
        circleMedia.setShareImage(new UMImage(mContext, img));
        if (!url.equals(""))
            circleMedia.setTargetUrl(url);
        mController.setShareMedia(circleMedia);
        mController.postShare(mContext, SHARE_MEDIA.WEIXIN_CIRCLE,
                new SnsPostListener() {
                    @Override
                    public void onStart() {
                    }

                    private void OnFinished() {
                        shareSuccess();
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA platform, int eCode,
                                           SocializeEntity entity) {
                        if (eCode == 200) {
                            shareSuccess();
                        } else {
//                            ToastUtil.show("错误码：" + eCode);
                        }
                    }
                });
    }

    protected void dealShareWeiXin(String title, String url, String desc,
                                   String img) {
        UMPlatformData platform = new UMPlatformData(UMPlatformData.UMedia.WEIXIN_FRIENDS, "");
        platform.setGender(UMPlatformData.GENDER.MALE); //optional
        platform.setWeiboId(WEIXIN_APPID);  //optional
        MobclickAgent.onSocialEvent(mContext, platform);
        // 设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        // 设置分享文字
        weixinContent.setShareContent(title);
        // 设置title
        weixinContent.setTitle(title);
        // 设置分享内容跳转URL
        if (!url.equals(""))
            weixinContent.setTargetUrl(url);
        // 设置分享图片
        weixinContent.setShareImage(new UMImage(mContext, img));
        mController.setShareMedia(weixinContent);
        mController.postShare(mContext, SHARE_MEDIA.WEIXIN,
                new SnsPostListener() {
                    @Override
                    public void onStart() {
                    }

                    private void OnFinished() {
                        shareSuccess();
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA platform, int eCode,
                                           SocializeEntity entity) {
                        if (eCode == 200) {
                            shareSuccess();
                        } else {
                            // ToastUtil.show("分享失败");
                        }
                    }
                });
    }

    protected void shareSuccess() {
//        Activity activity = (Activity) mContext;
//        if (activity instanceof MainActivity) {
//            MainActivity.FragmentParent.shareSuccess(position);
//        } else if (activity instanceof NewsDetailActivity) {
//            ((NewsDetailActivity) mContext).shareSuccess();
//        } else if (activity instanceof ZuopinActivity) {
//            ((ZuopinActivity) mContext).shareSuccess();
//        }
    }
}
*/
