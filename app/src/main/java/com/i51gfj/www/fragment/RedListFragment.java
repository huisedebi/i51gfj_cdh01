package com.i51gfj.www.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.adapter.RedListAdapter;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.model.RedListBean;
import com.i51gfj.www.model.UserInfo;
import com.i51gfj.www.model.UserInfoForMinFragementBean;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.ShpfUtil;
import com.i51gfj.www.util.Util;
import com.i51gfj.www.view.FullyLinearLayoutManager;
import com.i51gfj.www.view.pulltorefresh.PullToRefreshBase;
import com.i51gfj.www.view.pulltorefresh.PullToRefreshScrollView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/11.
 */
public class RedListFragment extends BaseFragment {
    private RedListBean data;
    private RecyclerView recyclerView;
    private TextView tiele_tv;
    private TextView left_tv;
    private TextView right_tv;
    private RedListAdapter adapter;
    private int pageNum = 1;
    private PullToRefreshScrollView scrollView;
    private String typeId;


    @Override
    public int getLayout() {
        return R.layout.fragment_red;
    }

    @Override
    public void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        tiele_tv = (TextView) view.findViewById(R.id.title_tv);
        left_tv = (TextView) view.findViewById(R.id.left_tv);
        left_tv.setOnClickListener(this);
       /* right_tv = (TextView) view.findViewById(R.id.right_tv);
        right_tv.setText("搜索");
        right_tv.setOnClickListener(this);*/
        adapter = new RedListAdapter(mActivity,this);
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        scrollView= (PullToRefreshScrollView) view.findViewById(R.id.pull_to_refresh_scrollview);
        scrollView.setMode(PullToRefreshBase.Mode.BOTH);

        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                pageNum = 1;
                post_main();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                if (data.getData() != null) {
                    pageNum++;
                    if (pageNum > Integer.valueOf(data.getPage().getPageTotal())) {
                        Toast.makeText(mActivity, "加载完成，无更多数据", Toast.LENGTH_SHORT).show();
                        scrollView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.onRefreshComplete();
                            }
                        }, 200);

                    } else {
                        post_main();
                    }
                } else {
                    Toast.makeText(mActivity, "加载完成，无更多数据", Toast.LENGTH_SHORT).show();
                    scrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.onRefreshComplete();
                        }
                    }, 200);
                }
            }
        });
    }


    @Override
    public void initData() {
        post_main();
    }


 /*   lng        *经纬度    极光请求里的经纬度
    lat        *经纬度    极光请求里的经纬度
    typeId     类型id     当该id有传时不显示广告模块　
    page       分页
    uid        用户id　*/
    public void post_main() {
        Util.showLoading(getActivity());
        JSONObject json = new JSONObject();
        try {
            json.put("lng",ShpfUtil.getStringValue("lng"));
            json.put("lat",ShpfUtil.getStringValue("lat"));
            json.put("page",pageNum);
            json.put("uid", AppUtil.getUserId());
            json.put("typeId", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Util.showMyLoadingDialog(mloadingDialog,getActivity());
        OkHttpUtils
                .postString()
                .url(MyURL.URL_RED_LIST)
                .content(json.toString())
                .build()
                .execute(new Callback<RedListBean>() {
                    @Override
                    public RedListBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, RedListBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Util.closeLoading();
                        scrollView.onRefreshComplete();
                        Log.e("onError", e.toString(), e);
                    }

                    @Override
                    public void onResponse(RedListBean response) {
                        Util.closeLoading();
                        scrollView.onRefreshComplete();
                        if (response.getStatus() == 1) {
                            if (pageNum == 1) {
                                data = response;
                            } else {
                                data.getData().addAll(response.getData());
                            }
                            adapter.setData(data);
                            tiele_tv.setText("红包列表");
                        }
                    }
                });
    }


    @Override
    public void onMyItemClick(Object data) {
        RedDetaileFragment fragment;
        Bundle bundle = new Bundle();
        bundle.putString("id", String.valueOf(data));
        fragment = new RedDetaileFragment();
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(RedListFragment.this);
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onMyItemClick(String jump, String id) {
        RedAdvaFragment fragment;
        Bundle bundle = new Bundle();
        bundle.putString("typeId",id );
        fragment = new RedAdvaFragment();
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(RedListFragment.this);
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.left_tv:
              if(getActivity()!=null){
                  getActivity().finish();
              }
              break;
          case R.id.right_tv:

              break;
      }
    }
}
