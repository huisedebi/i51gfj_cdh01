package com.i51gfj.www.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.activity.LoginActivity;
import com.i51gfj.www.adapter.MyCountAdapter;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.model.MainBean;
import com.i51gfj.www.model.MyCountBean;
import com.i51gfj.www.model.UserInfo;
import com.i51gfj.www.util.ShpfUtil;
import com.i51gfj.www.view.FullyLinearLayoutManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/11.  我的账单
 */
@SuppressLint("ValidFragment")
public class MyCountFragment extends BaseFragment {

    TextView title_tv;
    TextView ic_back;
    String title;
    String type;
    MyCountAdapter adapter;
    RecyclerView recyclerView;
    @SuppressLint("ValidFragment")
    public MyCountFragment() {

    }
    @SuppressLint("ValidFragment")
    public MyCountFragment(String title,String type) {
        this.title = title;
        this.type = type;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_mycount;
    }

    @Override
    public void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
       /* title_tv = (TextView) view.findViewById(R.id.mycount_title);
        title_tv.setText(title);*/
        /* ic_back = (TextView) view.findViewById(R.id.left_tv);
        ic_back.setOnClickListener(this);*/
        adapter = new MyCountAdapter(mActivity);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void initData() {
        post_main(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_tv:
             mActivity.finish();
                break;
        }
    }
    /**
     * 首页数据
     *
     */
    public void post_main(String page) {
        JSONObject json = new JSONObject();
          UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        if(loginData==null){
            Toast.makeText(mActivity, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        try {
            json.put("uid",loginData.getUid());
            json.put("page",page==null?"1":page);
            json.put("type",type==null?"1":type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Util.showMyLoadingDialog(mloadingDialog,getActivity());
        OkHttpUtils
                .postString()
                .url(MyURL.URL_MY_ACCOUNT)
                .content(json.toString())
                .build()
                .execute(new Callback<MyCountBean>() {
                    @Override
                    public MyCountBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, MyCountBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                    }

                    @Override
                    public void onResponse(MyCountBean response) {
                        if (response.getStatus() == 1) {
                            adapter.setData(response);

                        }
                    }
                });
    }
}
