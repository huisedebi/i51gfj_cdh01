package com.i51gfj.www.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.activity.LoginActivity;
import com.i51gfj.www.activity.MainActivity;
import com.i51gfj.www.activity.MineMidActivity;
import com.i51gfj.www.activity.MineMyCountActivity;
import com.i51gfj.www.activity.PhotoPickerActivity;
import com.i51gfj.www.application.MyApplication;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.manager.CacheManager;
import com.i51gfj.www.model.MainBean;
import com.i51gfj.www.model.MineIndexWrapper;
import com.i51gfj.www.model.UserInfo;
import com.i51gfj.www.model.UserInfoForMinFragementBean;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.PhoneUtils;
import com.i51gfj.www.util.ShpfUtil;
import com.i51gfj.www.util.Util;
import com.i51gfj.www.view.SwitchView_tip;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.rong.imkit.RongIM;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/11.
 */
public class MineFragment extends BaseFragment {
    private ImageView ic_back;
    private ImageView person_img;
    private ImageView ic_share;

    private TextView name;
    private LinearLayout etid_zl;
    private TextView yu_e;
    private TextView fa_red;
    private TextView fa_record;
    private TextView my_count;
    private TextView count_detail;
    private TextView getred_record;
    private TextView modify_password;
    private TextView my_gz;
    private TextView my_dp;
    private TextView hc_m;
    private TextView my_fkyj;
    private TextView open_hy;
    private TextView store_info;
    private TextView my_version;

    RelativeLayout delete_hc;

    private Button back_login;
    private UserInfo loginData;
    RelativeLayout layout_openstate;

    RelativeLayout yu_e_layout;
    SwitchView_tip switchView_tip;
    TextView store_state;
    private String totalCacheSize;
    public MineIndexWrapper data;//有用户或商户的区别


    @Override
    public int getLayout() {
        return R.layout.fragment_mine_index;
    }

    @Override
    public void initView(View view) {
        options = new DisplayImageOptions.Builder().cacheOnDisc(true).showImageOnLoading(R.drawable.img_loading)
                .showImageForEmptyUri(R.drawable.img_loading)
                .imageScaleType(ImageScaleType.EXACTLY)
                .showImageOnFail(R.drawable.img_loading).imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new RoundedBitmapDisplayer(PhoneUtils.dipToPixels(40), 0))
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        person_img = (ImageView) view.findViewById(R.id.person_img);
        person_img.setOnClickListener(this);
        name = (TextView) view.findViewById(R.id.name);
        etid_zl = (LinearLayout) view.findViewById(R.id.mine_edit);
        etid_zl.setOnClickListener(this);
        yu_e = (TextView) view.findViewById(R.id.yu_e);
        fa_red = (TextView) view.findViewById(R.id.fa_red);
        fa_red.setOnClickListener(this);
        fa_record = (TextView) view.findViewById(R.id.fa_record);
        fa_record.setOnClickListener(this);
        my_count = (TextView) view.findViewById(R.id.my_count);
        my_count.setOnClickListener(this);
        count_detail = (TextView) view.findViewById(R.id.count_detail);
        count_detail.setOnClickListener(this);
        getred_record = (TextView) view.findViewById(R.id.red_record);
        getred_record.setOnClickListener(this);
        modify_password = (TextView) view.findViewById(R.id.modify_password);
        modify_password.setOnClickListener(this);
        my_gz = (TextView) view.findViewById(R.id.my_gz);
        my_gz.setOnClickListener(this);
        my_dp = (TextView) view.findViewById(R.id.my_dp);
        my_dp.setOnClickListener(this);
        hc_m = (TextView) view.findViewById(R.id.hc_m);
        view.findViewById(R.id.delete_hc).setOnClickListener(this);
        my_fkyj = (TextView) view.findViewById(R.id.my_fkyj);
        my_fkyj.setOnClickListener(this);
        back_login = (Button) view.findViewById(R.id.back_login);
        back_login.setOnClickListener(this);
        open_hy = (TextView) view.findViewById(R.id.open_hy);
        open_hy.setOnClickListener(this);
        layout_openstate = (RelativeLayout) view.findViewById(R.id.layout_openstate);
        store_state = (TextView) view.findViewById(R.id.btn_state);
        store_info = (TextView) view.findViewById(R.id.store_info);
        store_info.setOnClickListener(this);
        switchView_tip = (SwitchView_tip) view.findViewById(R.id.switchview);
        switchView_tip.setOpened(true);
        switchView_tip.setOnSwitchListener(new SwitchView_tip.OnSwitchListener() {
            @Override
            public void onSwitched(boolean isSwitchOn) {
                store_state.setText(isSwitchOn ? "开" : "关");
                post_store_statu(isSwitchOn ? "1" : "0");
            }
        });
        yu_e_layout = (RelativeLayout) view.findViewById(R.id.yu_e_layout);
        yu_e_layout.setOnClickListener(this);
        view.findViewById(R.id.my_version).setOnClickListener(this);
    }


    @Override
    public void initData() {
        loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        totalCacheSize = "0KB";
        try {
            totalCacheSize = CacheManager.getTotalCacheSize(mActivity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        hc_m.setText("当前缓存" + totalCacheSize);
        post_main();
    }

    private void post_mine_index() {
        is_login();
        JSONObject json = new JSONObject();
        try {
            json.put("uid", AppUtil.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .postString()
                .url(MyURL.URL_MINE_INDEX)
                .content(json.toString())
                .build()
                .execute(new Callback<MineIndexWrapper>() {
                    @Override
                    public MineIndexWrapper parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, MineIndexWrapper.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                    }

                    @Override
                    public void onResponse(MineIndexWrapper response) {
                        if (response.getStatus() == 1) {
                            data = response;
                            setUI(response);
                        }
                    }
                });
    }

    //设置商家或者普通用户
    public void setUI(MineIndexWrapper response) {
        yu_e.setText("￥" + response.getAmount());
        if (loginData == null) {
            loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        }
        loginData.setUserType(response.getUserType());
        ShpfUtil.setObject(ShpfUtil.LOGIN, loginData);
        if (loginData.getUserType().equals("1")) {
            open_hy.setVisibility(View.GONE);
            layout_openstate.setVisibility(View.VISIBLE);
        } else {
            open_hy.setVisibility(View.VISIBLE);
            layout_openstate.setVisibility(View.GONE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
            if (name != null && name.getText().toString().equals("无") && loginData != null && AppUtil.is_logining) {
                setValue(null);
                post_mine_index();
            }
            if (loginData == null) {
                post_main();
            }
            if (AppUtil.is_frash) {//刷新金额
                post_mine_index();
                AppUtil.is_frash = false;
            }
        }
    }


    public void post_main() {
        if (!is_login()) {
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("uid", AppUtil.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .postString()
                .url(MyURL.URL_INFORMATION)
                .content(json.toString())
                .build()
                .execute(new Callback<UserInfoForMinFragementBean>() {
                    @Override
                    public UserInfoForMinFragementBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, UserInfoForMinFragementBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                    }

                    @Override
                    public void onResponse(UserInfoForMinFragementBean response) {
                        if (response.getStatus() == 1) {
                            setValue(response);
                            post_mine_index();
                        }
                    }
                });
    }

    //设置头像和名称
    private void setValue(UserInfoForMinFragementBean response) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        Log.i("loginData", "setValue: " + loginData);
        Log.i("loginData", "setValue: " + response);
        if (response != null && response.getData() != null && response.getData().getRealName() != null) {
            loginData.setUserName(response.getData().getRealName());
        }
        ShpfUtil.setObject(ShpfUtil.LOGIN, loginData);
        imageLoader.displayImage(loginData.getHeadImg(), person_img, options);
        name.setText(loginData.getUserName());
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.back_login:
                final android.app.AlertDialog.Builder alterDialog = new android.app.AlertDialog.Builder(getActivity());
                alterDialog.setMessage("确定退出当前用户？");
                alterDialog.setCancelable(true);
                alterDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShpfUtil.remove(ShpfUtil.LOGIN);
                        loginData = null;
                        data = null;
                        name.setText("无");
                        MainActivity activity = (MainActivity) mActivity;
                        activity.changeFragment(activity.tv_main);
                    }
                });
                alterDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alterDialog.show();
                return;
            case R.id.modify_password://修改密码s
                intent = new Intent(mActivity, LoginActivity.class);
                intent.putExtra("jump", "3");
                startActivity(intent);
                return;
            case R.id.mine_edit:
                intent = new Intent(mActivity, MineMidActivity.class);
                intent.putExtra("jump", "mine_edit");
                startActivityForResult(intent, 63);
                break;
            case R.id.yu_e_layout:
                intent = new Intent(mActivity, MineMidActivity.class);
                intent.putExtra("money", data.getAmount());
                intent.putExtra("jump", "yu_e");
                startActivity(intent);
                break;
            case R.id.fa_record:
                intent = new Intent(mActivity, MineMidActivity.class);
                intent.putExtra("jump", "fa_record");//发布记录
                intent.putExtra("type", "2");
                startActivity(intent);
                break;
            case R.id.my_gz:
                intent = new Intent(mActivity, MineMidActivity.class);
                intent.putExtra("jump", "my_gz");//我的关注
                startActivity(intent);
                break;
            case R.id.red_record:
                intent = new Intent(mActivity, MineMidActivity.class);
                intent.putExtra("jump", "fa_record");//领取记录
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
            case R.id.my_count:
                intent = new Intent(mActivity, MineMyCountActivity.class);
                startActivity(intent);
                break;
            case R.id.my_dp:
                intent = new Intent(mActivity, MineMidActivity.class);
                intent.putExtra("jump", "my_dp");//我的点评
                startActivity(intent);
                break;
            case R.id.fa_red:
                intent = new Intent(mActivity, MineMidActivity.class);
                intent.putExtra("jump", "fa_red");//推广红包
                startActivity(intent);
                break;
            case R.id.my_fkyj:
                intent = new Intent(mActivity, MineMidActivity.class);
                intent.putExtra("jump", "my_fkyj");//反馈意见
                startActivity(intent);
                break;
            case R.id.open_hy:
                intent = new Intent(mActivity, MineMidActivity.class);
                intent.putExtra("jump", "open_hy");//开通会员
                startActivity(intent);
                break;
            case R.id.store_info:
                if (loginData.getUserType().equals("1")) {
                    intent = new Intent(mActivity, MineMidActivity.class);
                    intent.putExtra("jump", "store_info");//商户信息
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("您暂未开通年费会员，请先开通年费会员！");
                    builder.setTitle("提示");
                    builder.setPositiveButton("开通", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Intent intent2 = new Intent(mActivity, MineMidActivity.class);
                            intent2.putExtra("jump", "open_hy");//开通会员
                            startActivity(intent2);
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });
                    builder.create().show();
                }
                break;
            case R.id.count_detail:
                intent = new Intent(mActivity, MineMidActivity.class);
                intent.putExtra("jump", "count_detail");//收支明细
                startActivity(intent);
                break;
            case R.id.delete_hc:

                UserInfo loginWrapper = ShpfUtil.getObject(ShpfUtil.LOGIN);
                CacheManager.clearAllCache(mActivity);
                ShpfUtil.setObject(ShpfUtil.LOGIN, loginWrapper);
                hc_m.setText("当前缓存 0kb ");
                Toast.makeText(mActivity, "成功清除缓存！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.person_img:
                Intent intent1 = new Intent(getActivity(), PhotoPickerActivity.class);
                getActivity().startActivityForResult(intent1, 1515);
                break;
            case R.id.my_version:
                intent = new Intent(mActivity, MineMidActivity.class);
                intent.putExtra("jump", "my_version");//版本
                startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1515 && resultCode == 2917) {
            if (data != null) {
                String Img = Util.bitmapToBase64(Util.getBitmap(data.getStringExtra(PhotoPickerActivity.SELECT_RESULTS)));
                post_headimg_upload(Img);
            }
        } else if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == 63) {
                post_main();
            }
        }
    }

  /*  uid    *用户id
    img    *base64位传上来*/

    public void post_headimg_upload(String img) {
        Util.showLoading(getActivity());
        JSONObject json = new JSONObject();
        try {
            json.put("uid", AppUtil.getUserId());
            json.put("img", img);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .postString()
                .url(MyURL.URL_HEADIMG_UPLOAD)
                .content(json.toString())
                .build()
                .execute(new Callback<UserInfoForMinFragementBean>() {
                    @Override
                    public UserInfoForMinFragementBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, UserInfoForMinFragementBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                        Util.closeLoading();
                    }

                    @Override
                    public void onResponse(UserInfoForMinFragementBean response) {
                        Util.closeLoading();
                        if (response.getStatus() == 1) {
                            if (response.getImg() != null) {
                                loginData.setHeadImg(response.getImg());
                                ShpfUtil.setValue(ShpfUtil.LOGIN, loginData);
                                ImageLoader imageLoader = ImageLoader.getInstance();
                                imageLoader.displayImage(loginData.getHeadImg(), person_img, options);
                            }
                        }
                    }
                });
    }


    /*  uid          *用户id
      storeStatus  *营业状态值*/
    public void post_store_statu(String status) {
        JSONObject json = new JSONObject();
        try {
            json.put("uid", AppUtil.getUserId());
            json.put("storeStatus", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .postString()
                .url(MyURL.URL_STORE_STATUS)
                .content(json.toString())
                .build()
                .execute(new Callback<UserInfoForMinFragementBean>() {
                    @Override
                    public UserInfoForMinFragementBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, UserInfoForMinFragementBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                    }

                    @Override
                    public void onResponse(UserInfoForMinFragementBean response) {
                        if (response.getStatus() == 1) {
                        }
                    }
                });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (AppUtil.is_frash) {//刷新金额
            post_mine_index();
            AppUtil.is_frash = false;
        }
        if (AppUtil.info_frash) {
            post_main();
            AppUtil.info_frash = false;
        }
    }
}
