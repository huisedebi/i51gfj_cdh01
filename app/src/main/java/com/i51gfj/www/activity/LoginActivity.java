
package com.i51gfj.www.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.i51gfj.www.R;
import com.i51gfj.www.fragment.ForgotFragment;
import com.i51gfj.www.fragment.LoginFragment;
import com.i51gfj.www.fragment.RegisterFragment;
import com.i51gfj.www.fragment.ResetFragment;
import com.i51gfj.www.impl.MyCallBack;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;


/**
 * 登录
 */

public class LoginActivity extends FragmentActivity implements MyCallBack {

    public static final int LOGIN = 0;
    public static final int REGISTER = 1;
    public static final int FORGOT = 2;
    public static final int RESET = 3;

    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    private String phone;

    private String password;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //账号登录Fragment
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setBaseInterface(this);
        //注册账号Fragment
        RegisterFragment registerFragment = new RegisterFragment();
        registerFragment.setBaseInterface(this);
        //忘记密码Fragment
        ForgotFragment forgotFragment = new ForgotFragment();
        forgotFragment.setBaseInterface(this);
        //重置密码Fragment
        ResetFragment resetFragment = new ResetFragment();
        resetFragment.setBaseInterface(this);
        fragmentList.add(loginFragment);
        fragmentList.add(registerFragment);
        fragmentList.add(forgotFragment);
        fragmentList.add(resetFragment);
        String jump =  getIntent().getStringExtra("jump");

        FragmentTransaction fragmentTransaction;
        switch (jump==null?"0":jump){
            case "0":
                 fragmentTransaction = getSupportFragmentManager().beginTransaction();
                //默认显示首页片段
                fragmentTransaction.add(R.id.content_login, fragmentList.get(0), "Login");
                fragmentTransaction.commit();
                break;
            case "3":
                 fragmentTransaction = getSupportFragmentManager().beginTransaction();
                //默认显示首页片段
                fragmentTransaction.add(R.id.content_login, fragmentList.get(3), "RESET");
                fragmentTransaction.commit();
        }


    }


/**
     * 关闭
     *
     * @param v onClick
     *
     */

    public void close(View v) {
        finish();
    }


/**
     * 回退
     *
     * @param v onClick
     */

    public void back(View v) {
        getSupportFragmentManager().popBackStack();
    }


/**
     * 注册账号
     *
     * @param v onClick
     */

    public void register(View v) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.content_login, fragmentList.get(REGISTER), "Register");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


/**
     * 忘记密码
     *
     * @param v onClick
     */

    public void forgot(View v) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.content_login, fragmentList.get(FORGOT), "Forgot");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void onCallBack(Object data) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch ((int) data) {
            //登录成功
            case 0:
                setResult(205);
                finish();
//                ShpfUtil.setValue("showMine", true);
                break;
            //注册成功
            case 1:
                getSupportFragmentManager().popBackStack();
                break;
            //开始重置密码
            case 2:
                getSupportFragmentManager().popBackStack();
                transaction.replace(R.id.content_login, fragmentList.get(RESET), "Reset");
                transaction.addToBackStack(null);
                break;
            //重置密码成功
            case 3:
                getSupportFragmentManager().popBackStack();
                break;
        }
        transaction.commit();

    }

    @Override
    public void onCallBack(String jump, Object data) {

    }



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

