package com.i51gfj.www.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.activity.LoginActivity;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.model.FeedBackBean;
import com.i51gfj.www.model.UserInfo;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.ShpfUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/11.反馈意见
 */
public class MineVersion extends BaseFragment {

    TextView title_tv;
    TextView ic_back,tv_version;


    @Override
    public int getLayout() {
        return R.layout.fragment_version;
    }

    @Override
    public void initView(View view) {
        title_tv = (TextView) view.findViewById(R.id.title_tv);
        title_tv.setText("当前版本号");
        tv_version = (TextView) view.findViewById(R.id.tv_version);
        ic_back = (TextView) view.findViewById(R.id.left_tv);
        ic_back.setOnClickListener(this);
    }

    @Override
    public void initData() {
        post_main();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_tv:
             mActivity.finish();
                break;
        }
    }


    public void post_main() {
        String version = AppUtil.getAppVersion();
        tv_version.setText("逛附近安卓版 V"+version);
    }
}
