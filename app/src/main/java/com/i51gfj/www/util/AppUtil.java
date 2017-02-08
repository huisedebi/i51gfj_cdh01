package com.i51gfj.www.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;


import com.i51gfj.www.R;
import com.i51gfj.www.manager.ModelManager;
import com.i51gfj.www.model.JpushBean;
import com.i51gfj.www.model.MainBean;
import com.i51gfj.www.model.UserInfo;
import com.i51gfj.www.view.LoadingDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 应用工具类.
 */
public class AppUtil {
    public static MainBean indexData;//主页数据
    public static JpushBean city;//城市
    public static Boolean is_frash = false;//领取红包后刷新金额
    public static Boolean info_frash = false ;//修改信息修改后刷新
    public static LoadingDialog loadingDialog;
    public static String  money_text ;//充值后的返回刷新的数字

    public static LoadingDialog getOneLoadingDialog(Context context){
        if(context==null){
            return null;
        }
        if(loadingDialog!=null && loadingDialog.mLoadingDialog!=null){
            return loadingDialog;
        }
        else{
            loadingDialog =new LoadingDialog(context,"加载中...");
            return loadingDialog;
        }
    }
    public static void  showDialog(LoadingDialog dialog){
        if(dialog !=null){
            dialog.show();
        }
    }
    public static void  closeDialog(LoadingDialog dialog){
        if(dialog !=null){
            dialog.close();
            if (loadingDialog !=null){
                loadingDialog =null;
            }

        }
    }
    //判断是否登录状态中
    public static boolean is_logining = false;
    //判断是否登录状态中
    public static boolean is_ry_connet = false;
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(int pxValue) {
        final float scale = ModelManager.getInstance().getContext()
                .getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String getMessageDigest(byte[] buffer) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

  /*  *//**
     * 获取uid
     */
    public static String getUserId() {
        UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        if (loginData != null) {
            return loginData.getUid();
        }else {
            return "";
        }
    }

    /**
     * 获取uid
    /* *//*
    public static boolean getIsLogin(Context context) {
        LoginWrapper loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        if (loginData == null){
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, LoginActivity.class));
            return false;
        }else {
            return true;
        }
    }*/

    /**
     * 判断是不是11位手机号码
     *
     * @param inputText 输入的数字
     * @return
     */
    public static boolean isPhone(String inputText) {
        Pattern p = Pattern
                .compile("^\\d{11}$");
//                .compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(inputText);
        return m.matches();
    }

    /**
     * 获取当前应用程序的版本号
     */
    public static String getAppVersion() {
        String version;
        try {
            version = ModelManager.getInstance().getContext().getPackageManager().getPackageInfo(
                    ModelManager.getInstance().getContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(AppUtil.class.getName()
                    + "the application not found");
        }
        return version;
    }

    /**
     * 获取当前应用程序的版本号
     */
    public static int getAppCode() {
        int version;
        try {
            version = ModelManager.getInstance().getContext().getPackageManager().getPackageInfo(
                    ModelManager.getInstance().getContext().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(AppUtil.class.getName()
                    + "the application not found");
        }
        return version;
    }

    /**
     *
     * @param password
     * 密码
     * @return String
     * 加密后的密码
     */
    public static String getMD5(String password) {
        String re_md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuilder buf = new StringBuilder("");
            for (byte aB : b) {
                i = aB;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }

    /**
     * 回到home，后台运行
     */
    public static void goHome(Context context) {
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(mHomeIntent);
    }

    /**
     * 描述：判断网络是否有效.
     *
     * @return true, if is network available
     */
    public static boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) ModelManager.getInstance().getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static DisplayImageOptions getNormalImageOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.load)
                .showImageForEmptyUri(R.drawable.load)
                .showImageOnFail(R.drawable.load)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(true).cacheOnDisk(true)
                .build();
    }

    /**
     * 描述：dip转换为px.
     *
     * @param dipValue the dip value
     * @return px值
     */
    public static float dip2px(float dipValue) {
        DisplayMetrics mDisplayMetrics = ModelManager
                .getInstance().getContext().getResources().getDisplayMetrics();
        return applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue,
                mDisplayMetrics);
    }

    /**
     * TypedValue官方源码中的算法，任意单位转换为PX单位
     *
     * @param unit    TypedValue.COMPLEX_UNIT_DIP
     * @param value   对应单位的值
     * @param metrics 密度
     * @return px值
     */
    private static float applyDimension(int unit, float value,
                                        DisplayMetrics metrics) {
        switch (unit) {
            case TypedValue.COMPLEX_UNIT_PX:
                return value;
            case TypedValue.COMPLEX_UNIT_DIP:
                return value * metrics.density;
            case TypedValue.COMPLEX_UNIT_SP:
                return value * metrics.scaledDensity;
            case TypedValue.COMPLEX_UNIT_PT:
                return value * metrics.xdpi * (1.0f / 72);
            case TypedValue.COMPLEX_UNIT_IN:
                return value * metrics.xdpi;
            case TypedValue.COMPLEX_UNIT_MM:
                return value * metrics.xdpi * (1.0f / 25.4f);
        }
        return 0;
    }

    /**
     * 获取手机IMEI码
     */
    public static String getPhoneIMEI(Context cxt) {
        TelephonyManager tm = (TelephonyManager) cxt
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获取手机系统SDK版本
     *
     * @return 如API 17 则返回 17
     */
    public static int getSDKVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取系统版本
     *
     * @return 形如2.3.3
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * read file content
     *
     * @param context   the context
     * @param assetPath the asset path
     * @return String string
     */
//    public static String readText(Context context, String assetPath) {
//        LogUtils.debug("read assets file as text: " + assetPath);
//        try {
//            return ConvertUtils.toString(context.getAssets().open(assetPath));
//        } catch (Exception e) {
//            LogUtils.error(e);
//            return "";
//        }
//    }

    public static Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(ModelManager.getInstance().getContext().getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

}
