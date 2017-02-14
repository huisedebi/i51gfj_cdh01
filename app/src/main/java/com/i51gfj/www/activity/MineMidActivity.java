package com.i51gfj.www.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;

import com.i51gfj.www.R;
import com.i51gfj.www.fragment.BaseFragment;
import com.i51gfj.www.fragment.MineCountDetailFragment;
import com.i51gfj.www.fragment.MineFeedFragment;
import com.i51gfj.www.fragment.MineMyCommentFragment;
import com.i51gfj.www.fragment.MineMyConcernFragment;
import com.i51gfj.www.fragment.MineRedRecordFragment;
import com.i51gfj.www.fragment.MineSendRedFragment;
import com.i51gfj.www.fragment.MineVersion;
import com.i51gfj.www.fragment.MineYueFragment;
import com.i51gfj.www.fragment.OpenHYFragment;
import com.i51gfj.www.fragment.ShopInfoFragment;
import com.i51gfj.www.fragment.UpdataFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/8/17.中间层跳转
 */
public class MineMidActivity extends FragmentActivity {

    private final String SHOPDETAIL = "shopdetail";
    public LinearLayout layout_content;
    public String jump;
    public String passData;
    public boolean isMultiSelect;
    public ArrayList<String> Imgresults;
    public String Imgresult;
    public ShopInfoFragment shopinfofragment;
    public  MineSendRedFragment mineSendRedFragment;


    public MineMidActivity() {
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
        jump();
    }

    private void jump() {
        BaseFragment fragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        switch (jump){
            case "mine_edit":
                fragment = new UpdataFragment();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.add(R.id.content, fragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case "count_detail":
                fragment = new MineCountDetailFragment();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.add(R.id.content, fragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case "yu_e":
                fragment = new MineYueFragment();
                bundle.putString("money", getIntent().getStringExtra("money"));
                fragment.setArguments(bundle);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.add(R.id.content, fragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case "fa_record":
                fragment = new MineRedRecordFragment();
                bundle.putString("type",getIntent().getStringExtra("type"));
                fragment.setArguments(bundle);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.add(R.id.content, fragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case "fa_red"://推广红包
                mineSendRedFragment = new MineSendRedFragment();
                bundle.putString("type",getIntent().getStringExtra("type"));
                mineSendRedFragment.setArguments(bundle);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.add(R.id.content, mineSendRedFragment,"MineSendRedFragment");
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case "my_gz":
                fragment = new MineMyConcernFragment();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.add(R.id.content, fragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case "my_dp":
                fragment = new MineMyCommentFragment();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.add(R.id.content, fragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case "my_fkyj":
                fragment = new MineFeedFragment();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.add(R.id.content, fragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case "open_hy":
                fragment = new OpenHYFragment();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.add(R.id.content, fragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case "store_info":
                shopinfofragment = new ShopInfoFragment();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.add(R.id.content, shopinfofragment,"shopinfofragment");
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case "my_version":
                MineVersion fragment1 = new MineVersion();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.add(R.id.content, fragment1,"MineVersion");
                fragmentTransaction.commitAllowingStateLoss();
                break;
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2916 && resultCode == 2917) {
            if (isMultiSelect) {
                //多选
                 Imgresults = data.getStringArrayListExtra(PhotoPickerActivity.SELECT_RESULTS_ARRAY);
            }else{
                //单选
                 Imgresult = data.getStringExtra(PhotoPickerActivity.SELECT_RESULTS);
                setImgOne("one");
            }
        }else if(requestCode==3000 && resultCode ==2917){
            if(mineSendRedFragment!=null){
                Imgresults = data.getStringArrayListExtra(PhotoPickerActivity.SELECT_RESULTS_ARRAY);
                mineSendRedFragment.changeAdapte(Imgresults);
            }
        }else if(requestCode==3001 && resultCode ==2917){
            if(shopinfofragment!=null){
                Imgresults = data.getStringArrayListExtra(PhotoPickerActivity.SELECT_RESULTS_ARRAY);
                shopinfofragment.changeAdapte(Imgresults);
            }
        }else  if(requestCode ==101 && resultCode == RESULT_OK){
            ShopInfoFragment f = (ShopInfoFragment) getSupportFragmentManager().findFragmentByTag("shopinfofragment");
            f.onActivityResult(requestCode, 102, data);
        }else if(requestCode ==105 && resultCode == RESULT_OK){
            MineSendRedFragment f = (MineSendRedFragment) getSupportFragmentManager().findFragmentByTag("MineSendRedFragment");
            f.onActivityResult(requestCode, 102, data);
        }
    }

    private void setImgOne(String type) {
        if(shopinfofragment!=null){
            if(type.equals("one")){
                shopinfofragment.changeImg(Imgresult);
            }
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
