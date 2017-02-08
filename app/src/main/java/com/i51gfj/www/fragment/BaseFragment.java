package com.i51gfj.www.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.i51gfj.www.R;
import com.i51gfj.www.activity.LoginActivity;
import com.i51gfj.www.application.MyApplication;
import com.i51gfj.www.banner.ADInfo;
import com.i51gfj.www.banner.ImageCycleView;
import com.i51gfj.www.dialog.LoadingDialog;
import com.i51gfj.www.impl.MyCallBack;
import com.i51gfj.www.impl.MyItemClickListener;
import com.i51gfj.www.model.UserInfo;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.ShpfUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseFragment
 */
public abstract class BaseFragment extends Fragment implements OnClickListener, MyItemClickListener, MyCallBack {


    protected FragmentActivity mActivity;
    protected LoadingDialog loadingDialog;
    protected DatePickerDialog datePickerDialog;
    protected ImageLoader mImageLoader;
    protected DisplayImageOptions options;
    ImageLoader imageLoader = ImageLoader.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
        View view = inflater.inflate(getLayout(), container, false);
        mImageLoader = ImageLoader.getInstance();
        options = AppUtil.getNormalImageOptions();
        initView(view);
        initData();
        return view;
    }

    /**
     * 设置标题，String为""时不设置，size为0时不设置
 /*    *//*
    public void setTop(String left, String title, String right, int size) {
        top_title.setText(title);
        if (!left.equals("")) {
            top_left.setText(left);
        }
        if (!right.equals("")) {
            top_right.setText(right);
        }
        if(size != 0){
            top_left.setTextSize(size);
            top_right.setTextSize(size);
        }
    }*/

    @Override
    public void onClick(View v) {
    }

    /**
 /*    * 创建并显示加载对话框
     *//*
    public void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog();
        }

        if (!loadingDialog.isAdded()) {
            loadingDialog.show(getActivity().getFragmentManager(), "LoadingDialog");
        }
    }*/

    /**
  /*   * 创建并显示选择时间对话框
     *//*
    public void showDatePickerDialog() {
        if (datePickerDialog == null) {
            datePickerDialog = new DatePickerDialog();
        }

        if (!datePickerDialog.isAdded()) {
            datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
        }
    }
*/
    @Override
    public void onCallBack(Object data) {

    }

    @Override
    public void onCallBack(String jump, Object data) {

    }

    @Override
    public void onMyItemClick() {

    }

    @Override
    public void onMyItemClick(Object data) {

    }

    @Override
    public void onMyItemClick(String jump, int position) {

    }

    @Override
    public void onMyItemClick(String jump, String id) {

    }

    @Override
    public void onMyItemClick(String jump, String store_id, String id) {

    }

    @Override
    public void onMyItemClick(Object data, int position) {

    }

    /**
     * 得到布局
     */
    public abstract int getLayout();

    /**
     * 初始化控件
     *
     * @param view 布局对象
     */
    public abstract void initView(View view);

    /**
     * 初始化数据
     */
    public abstract void initData();



    public ImageCycleView.ImageCycleViewListener bannerCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
        @Override
        public void onImageClick(ADInfo info, int postion, View imageView) {
//             Toast.makeText(getActivity(), "图片" + postion,Toast.LENGTH_SHORT).show();
        }
        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            imageLoader.displayImage(imageURL, imageView);
        }
    };

    public  Boolean is_login(){
        UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        if (loginData == null) {
            Toast.makeText(mActivity, "请先登录", Toast.LENGTH_SHORT).show();
            mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
            return false;
        }else{
            if(AppUtil.is_logining){
                return true;
            }else{
                if(loginData.is_remenrber()){
                    return true;
                }else{
                    Toast.makeText(mActivity, "请先登录", Toast.LENGTH_SHORT).show();
                    mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                    return false;
                }
            }
        }
    }
}
