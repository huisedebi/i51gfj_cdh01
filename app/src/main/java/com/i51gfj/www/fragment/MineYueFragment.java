package com.i51gfj.www.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.activity.LoginActivity;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.model.MineIndexWrapper;
import com.i51gfj.www.model.UserInfo;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.ShpfUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/11.账户余额  钱包
 */
public class MineYueFragment extends BaseFragment {

    TextView title_tv;
    TextView ic_back;
    TextView tv_money;


    @Override
    public int getLayout() {
        return R.layout.mine_fragment_yu_e;
    }

    @Override
    public void initView(View view) {
        title_tv = (TextView) view.findViewById(R.id.title_tv);
        title_tv.setText("账户余额");
        ic_back = (TextView) view.findViewById(R.id.left_tv);
        ic_back.setOnClickListener(this);
        tv_money= (TextView) view.findViewById(R.id.tv_money);
        view.findViewById(R.id.bt_cz).setOnClickListener(this);
        view.findViewById(R.id.bt_tx).setOnClickListener(this);
        String money =getArguments().getString("money", "0");
        tv_money.setText(money);
    }

    @Override
    public void initData() {

    }




    @Override
    public void onClick(View v) {
        BaseFragment fragment;
        FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
        //Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.left_tv:
             mActivity.finish();
                break;
            case R.id.bt_cz:
                fragment = new MineChongzhiFragment();
                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.hide(MineYueFragment.this);
                fragmentTransaction.add(R.id.content, fragment);
                fragmentTransaction.addToBackStack(null);
                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case R.id.bt_tx:
                fragment = new MineTiXianFragment();
                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.hide(MineYueFragment.this);
                fragmentTransaction.add(R.id.content, fragment);
                fragmentTransaction.addToBackStack(null);
                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.commitAllowingStateLoss();
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden && AppUtil.is_frash){
            tv_money.setText(AppUtil.money_text);
        }
    }
}
