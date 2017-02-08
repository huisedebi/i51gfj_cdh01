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
import com.i51gfj.www.adapter.MyConcernAdapter;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.model.FeedBackBean;
import com.i51gfj.www.model.ShopBean;
import com.i51gfj.www.model.UserInfo;
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
public class MineFeedFragment extends BaseFragment {

    TextView title_tv;
    TextView ic_back;
    EditText name;
    EditText phone;
    EditText content;
    Button btn_updata;


    @Override
    public int getLayout() {
        return R.layout.mine_fragment_opinion;
    }

    @Override
    public void initView(View view) {
        title_tv = (TextView) view.findViewById(R.id.title_tv);
        title_tv.setText("反馈意见");
        ic_back = (TextView) view.findViewById(R.id.left_tv);
        ic_back.setOnClickListener(this);
        name = (EditText) view.findViewById(R.id.et_name);
        phone = (EditText) view.findViewById(R.id.et_phone);
        content = (EditText) view.findViewById(R.id.et_opinion);
        btn_updata = (Button) view.findViewById(R.id.btn_updata);
        btn_updata.setOnClickListener(this);
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
            case R.id.btn_updata:
                updata();
                break;
        }
    }


    /**
     * 首页数据
     *uid    *用户id
     */
    public void post_main() {
        JSONObject json = new JSONObject();
        UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        if(loginData==null){
            Toast.makeText(mActivity, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        try {
            json.put("uid",loginData.getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Util.showMyLoadingDialog(mloadingDialog,getActivity());
        OkHttpUtils
                .postString()
                .url(MyURL.URL_FEEDBACK)
                .content(json.toString())
                .build()
                .execute(new Callback<FeedBackBean>() {
                    @Override
                    public FeedBackBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, FeedBackBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                    }

                    @Override
                    public void onResponse(FeedBackBean response) {
                        if (response.getStatus() == 1) {
                            name.setText(response.getData().getRealName());
                            phone.setText(response.getData().getMobile());
                            btn_updata.setOnClickListener(MineFeedFragment.this);
                        }
                    }
                });
    }
    /**
     * uid    *用户id
     mobile     电话
     realName   姓名
     content    内容
     jpushId    *极光id
     */
    public void  updata() {
        JSONObject json = new JSONObject();
        UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        try {
            json.put("uid",loginData.getUid());
            json.put("mobile",phone.getText().toString());
            json.put("realName",name.getText().toString());
            json.put("content",content.getText().toString());
            String registrationID = JPushInterface.getRegistrationID(mActivity.getApplicationContext());
            json.put("jpush_id", registrationID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Util.showMyLoadingDialog(mloadingDialog,getActivity());
        OkHttpUtils
                .postString()
                .url(MyURL.URL_FEEDBACK)
                .content(json.toString())
                .build()
                .execute(new Callback<FeedBackBean>() {
                    @Override
                    public FeedBackBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, FeedBackBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                    }
                    @Override
                    public void onResponse(FeedBackBean response) {
                        if (response.getStatus() == 1) {
                            Toast.makeText(mActivity, response.getInfo(), Toast.LENGTH_SHORT).show();
                            getActivity().finish();

                        }
                        Toast.makeText(mActivity, response.getInfo(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
