package com.i51gfj.www;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.ArrayList;

import io.rong.imlib.model.UserInfo;

/**
 * Created by Bob on 15/8/21.
 */
public class DemoContext {

    private static DemoContext mDemoContext;
    public Context mContext;
    private SharedPreferences mPreferences;

    public static DemoContext getInstance() {

        if (mDemoContext == null) {
            mDemoContext = new DemoContext();
        }
        return mDemoContext;
    }

    private DemoContext() {
    }

    private DemoContext(Context context) {
        mContext = context;
        mDemoContext = this;
        //http初始化 用于登录、注册使用
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private ArrayList<UserInfo> mUserInfos;

    public ArrayList<UserInfo> getmUserInfos() {
        return mUserInfos;
    }

    public void setmUserInfos(ArrayList<UserInfo> mUserInfos) {
        this.mUserInfos = mUserInfos;
    }

    public UserInfo getUserInfoByUserId(String userId) {

        UserInfo userInfoReturn = null;

        if (!TextUtils.isEmpty(userId) && mUserInfos != null) {
            for (UserInfo userInfo : mUserInfos) {

                if (userId.equals(userInfo.getUserId())) {
                    userInfoReturn = userInfo;
                    break;
                }
            }
        }
        return userInfoReturn;
    }

    public static void init(Context context) {
        mDemoContext = new DemoContext(context);
    }

    public SharedPreferences getSharedPreferences() {
        return mPreferences;
    }

}
