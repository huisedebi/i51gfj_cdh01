package com.i51gfj.www.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;

import com.i51gfj.www.R;
import com.i51gfj.www.fragment.BaseFragment;
import com.i51gfj.www.fragment.RedListFragment;
import com.i51gfj.www.fragment.ShopDetailFragment;
import com.i51gfj.www.fragment.ShopFragment;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/8/17.中间层跳转
 */
public class IndexMidActivity extends FragmentActivity {

    private final String SHOPDETAIL = "shopdetail";
    public LinearLayout layout_content;
    public String jump;
    public String passData;




    public IndexMidActivity() {
        super();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_midactivity);
        initView();
        initData();
    }

    private void initView() {

    }
    private void initData() {
        jump = getIntent().getStringExtra("jump");
        passData = getIntent().getStringExtra("passData");
        jump();
    }

    private void jump() {
        BaseFragment fragment;
        Bundle bundle = new Bundle();
        if(jump!=null && jump.equals(SHOPDETAIL)){
            bundle.putString("passData",passData);
            fragment = new ShopDetailFragment();
            fragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            fragmentTransaction.add(R.id.content, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }else if(jump !=null && jump.equals("redList")){
            fragment = new RedListFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            fragmentTransaction.add(R.id.content, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }else if(jump !=null && jump.equals("shopList")){
            bundle.putString("id",getIntent().getStringExtra("id"));
            fragment = new ShopFragment();
            fragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            fragmentTransaction.add(R.id.content, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }

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

