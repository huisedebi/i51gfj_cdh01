package com.i51gfj.www.fragment;

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
import com.i51gfj.www.activity.ShopDetailActivity;
import com.i51gfj.www.adapter.MyCommentAdapter;
import com.i51gfj.www.adapter.MyConcernAdapter;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.model.MyCommentBean;
import com.i51gfj.www.model.ShopBean;
import com.i51gfj.www.model.UserInfo;
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
 * Created by Administrator on 2016/8/11.//我的点评
 */
public class MineMyCommentFragment extends BaseFragment {

    TextView title_tv;
    TextView ic_back;
    RecyclerView recyclerView;
    MyCommentAdapter adapter;
    private PullToRefreshScrollView scrollView;
    int pageNum = 1;
    private MyCommentBean data;


    @Override
    public int getLayout() {
        return R.layout.mine_fragment_myconcern;
    }//借用

    @Override
    public void initView(View view) {
        title_tv = (TextView) view.findViewById(R.id.title_tv);
        title_tv.setText("我的点评");
        ic_back = (TextView) view.findViewById(R.id.left_tv);
        ic_back.setOnClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));


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
                    if (pageNum > Integer.valueOf(data.getPage().getPage_total())) {
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
     *uid    *用户id
     page       *分页   *

     */
    public void post_main() {
        Util.showLoading(getActivity());
        JSONObject json = new JSONObject();
        UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        if(loginData==null){
            Toast.makeText(mActivity, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        try {
            json.put("uid",loginData.getUid());
            json.put("page",pageNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .postString()
                .url(MyURL.URL_MY_COMMENT)
                .content(json.toString())
                .build()
                .execute(new Callback<MyCommentBean>() {
                    @Override
                    public MyCommentBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, MyCommentBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                        scrollView.onRefreshComplete();
                        Util.closeLoading();
                    }

                    @Override
                    public void onResponse(MyCommentBean response) {
                        adapter = new MyCommentAdapter(response,MineMyCommentFragment.this,mActivity);
                        recyclerView.setAdapter(adapter);
                        scrollView.onRefreshComplete();
                        Util.closeLoading();
                        if (response.getStatus() == 1) {
                            if(pageNum==1){
                                data = response;
                            }else{
                                data.getData().addAll(response.getData());
                            }
                            adapter.setData(data);
                        }
                    }
                });
    }

    @Override
    public void onMyItemClick(Object data) {
        if(data!=null){
            Intent intent = new Intent(mActivity, ShopDetailActivity.class);
            intent.putExtra("passData", (String) data);
            startActivity(intent);
        }
    }
}
