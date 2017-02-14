package com.i51gfj.www.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.adapter.ShopDetailCommentAdapter;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.model.ShopDetailCommentWrapper;
import com.i51gfj.www.model.UserInfo;
import com.i51gfj.www.util.ShpfUtil;
import com.i51gfj.www.util.Util;
import com.i51gfj.www.view.FullyLinearLayoutManager;
import com.i51gfj.www.view.pulltorefresh.PullToRefreshBase;
import com.i51gfj.www.view.pulltorefresh.PullToRefreshScrollView;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Response;


/**
 */
public class ShopDetailCommentActivity extends FragmentActivity implements View.OnClickListener{
    RecyclerView recycler;
    ShopDetailCommentAdapter adapter;
    TextView tv_back;
    private ShopDetailCommentWrapper data;
    private int pageNum = 1;
    private PullToRefreshScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail_comment);
        initView();
        initData();
    }

    private void initView() {
        recycler = (RecyclerView) findViewById(R.id.recycler);
        tv_back = (TextView) findViewById(R.id.left_tv);
        TextView textView = (TextView) findViewById(R.id.title_tv);
        textView.setText("点评列表");
        tv_back.setOnClickListener(this);
        TextView right= (TextView) findViewById(R.id.right_tv);
        right.setText("发布");
        right.setOnClickListener(this);


        scrollView= (PullToRefreshScrollView)findViewById(R.id.pull_to_refresh_scrollview);
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
                        Toast.makeText(ShopDetailCommentActivity.this, "加载完成，无更多数据", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ShopDetailCommentActivity.this, "加载完成，无更多数据", Toast.LENGTH_SHORT).show();
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

    private void initData() {
        post_main();
    }
   /* id         *店铺id
    uid        *用户id*/

    public void post_main() {
        UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        if (loginData == null) {
            Toast.makeText(ShopDetailCommentActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ShopDetailCommentActivity.this, LoginActivity.class));
            return;
        }
        Util.showLoading(this);
        JSONObject json = new JSONObject();
        try {
            json.put("id",getIntent().getStringExtra("id"));
            json.put("uid",loginData.getUid());
            json.put("page",pageNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Util.showMyLoadingDialog(mloadingDialog,getActivity());
        OkHttpUtils
                .postString()
                .url(MyURL.URL_SHOP_COMMENT_LIST)
                .content(json.toString())
                .build()
                .execute(new Callback<ShopDetailCommentWrapper>() {
                    @Override
                    public ShopDetailCommentWrapper parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, ShopDetailCommentWrapper.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        scrollView.onRefreshComplete();
                        Log.e("onError", e.toString(), e);
                        Util.closeLoading();
                    }

                    @Override
                    public void onResponse(ShopDetailCommentWrapper response) {
                        scrollView.onRefreshComplete();
                        Util.closeLoading();
                        if (response.getStatus() == 1) {
                            if(pageNum==1){
                                data = response;
                            }else{
                                data.getData().addAll(response.getData());
                            }
                            adapter = new ShopDetailCommentAdapter(data,ShopDetailCommentActivity.this);
                            recycler.setLayoutManager(new FullyLinearLayoutManager(ShopDetailCommentActivity.this, LinearLayoutManager.VERTICAL, false));
                            recycler.setAdapter(adapter);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_tv:
                finish();
                break;
            case R.id.right_tv:
                Intent intent = new Intent(this,SendCommentActivity.class);
                intent.putExtra("id",getIntent().getStringExtra("id"));
                intent.putExtra("name",getIntent().getStringExtra("name"));
                startActivityForResult(intent, 121);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==121 && resultCode==RESULT_OK){
            post_main();
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
