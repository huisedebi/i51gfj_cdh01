package com.i51gfj.www.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.activity.LoginActivity;
import com.i51gfj.www.activity.ShopDetailActivity;
import com.i51gfj.www.adapter.RedDetailAdapter;
import com.i51gfj.www.adapter.RedListAdapter;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.model.RedDetailBean;
import com.i51gfj.www.model.RedListBean;
import com.i51gfj.www.model.UserInfo;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.ShpfUtil;
import com.i51gfj.www.view.FullyLinearLayoutManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/11.红包详情
 */
public class RedDetaileFragment extends BaseFragment {
    private RedDetailBean data;
    private RecyclerView recyclerView;
    private TextView tiele_tv;
    private TextView left_tv;
    private TextView right_tv;
    private RedDetailAdapter adapter;
    private String id;


    @Override
    public int getLayout() {
        return R.layout.fragment_red_detail;
    }

    @Override
    public void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        tiele_tv = (TextView) view.findViewById(R.id.title_tv);
        left_tv = (TextView) view.findViewById(R.id.left_tv);
        left_tv.setOnClickListener(this);
        right_tv = (TextView) view.findViewById(R.id.right_tv);
        tiele_tv.setText("详情");
        right_tv.setText("前往店铺");
        right_tv.setOnClickListener(this);
        adapter = new RedDetailAdapter(mActivity,this);
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if(parent.getChildPosition(view)!= 0){
                    outRect.top = 10;
                }
                outRect.left =10;
                outRect.right = 10;
            }
        });
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void initData() {
        id = getArguments().getString("id","");
        post_main();
    }


  /*  id         *商品id
    uid         *用户id
    lng        *经纬度    极光请求里的经纬度
    lat        *经纬度    极光请求里的经纬度*/
    public void post_main() {
        UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        if (loginData == null) {
            Toast.makeText(mActivity, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("id",id);
            json.put("uid",loginData.getUid());
            json.put("lng",ShpfUtil.getStringValue("lng"));
            json.put("lat",ShpfUtil.getStringValue("lat"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Util.showMyLoadingDialog(mloadingDialog,getActivity());
        OkHttpUtils
                .postString()
                .url(MyURL.URL_DETAIL_RED)
                .content(json.toString())
                .build()
                .execute(new Callback<RedDetailBean>() {
                    @Override
                    public RedDetailBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, RedDetailBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                    }

                    @Override
                    public void onResponse(RedDetailBean response) {
                        if (response.getStatus() == 1) {
                            data = response;
                            adapter.setData(response);
                            if(response.getSid()==null || response.getSid().equals("0")){
                                right_tv.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }
   /* id         *红包id
    uid        *用户id
    lng        *经纬度    本机经纬度
    lat        *经纬度    本机经纬度
    jpushId    *极光id

            返回固定参数
    status      1成功   0提示info内容
    info        为返回的提示信息*/

    public void get_red(String id) {
        if(!is_login()){
            return;
        }
        UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        JSONObject json = new JSONObject();
        try {
            json.put("id",id);
            json.put("uid",loginData.getUid());
            json.put("lng",ShpfUtil.getStringValue("lng"));
            json.put("lat",ShpfUtil.getStringValue("lat"));
            json.put("jpushId", JPushInterface.getRegistrationID(mActivity.getApplicationContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .postString()
                .url(MyURL.URL_GET_RED)
                .content(json.toString())
                .build()
                .execute(new Callback<RedDetailBean>() {
                    @Override
                    public RedDetailBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, RedDetailBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                    }

                    @Override
                    public void onResponse(RedDetailBean response) {
                        if (response.getStatus() == 1) {
                            AppUtil.is_frash=true;
                           post_main();
                        }
                    }
                });
    }
    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.left_tv:
              getActivity().getSupportFragmentManager().popBackStack();
              break;
          case R.id.right_tv:
              Intent intent = new Intent(getActivity(), ShopDetailActivity.class);
              intent.putExtra("passData", data.getSid());
              getActivity().startActivity(intent);
              break;
      }
    }

    @Override
    public void onMyItemClick(Object data) {
        if(data !=null){
            get_red(String.valueOf(data));
        }
    }


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
