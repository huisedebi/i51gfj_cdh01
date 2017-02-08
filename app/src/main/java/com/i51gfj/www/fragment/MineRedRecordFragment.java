package com.i51gfj.www.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.activity.LoginActivity;
import com.i51gfj.www.adapter.RedRecordAdapter;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.model.MainBean;
import com.i51gfj.www.model.RedListBean;
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
 * Created by Administrator on 2016/8/11.
 */
public class MineRedRecordFragment extends BaseFragment {

    TextView title_tv;
    TextView ic_back;
    public String type;
    RedRecordAdapter adapter;
    RecyclerView recyclerView;
    private PullToRefreshScrollView scrollView;
    int pageNum = 1;RedListBean data;
    private String keyword;
    LinearLayout layout_nothing;


    @Override
    public int getLayout() {
        return R.layout.mine_fragment_myconcern;
    }

    @Override
    public void initView(View view) {
        type = getArguments().getString("type",null);
        title_tv = (TextView) view.findViewById(R.id.title_tv);
        title_tv.setText(type.equals("1")?"领取红包记录":"发放红包记录");
        TextView right = (TextView) view.findViewById(R.id.right_tv);
        right.setText("搜索标题");
        right.setOnClickListener(this);
        ic_back = (TextView) view.findViewById(R.id.left_tv);
        ic_back.setOnClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        adapter = new RedRecordAdapter(mActivity,this);
        recyclerView.setAdapter(adapter);
        layout_nothing = (LinearLayout) view.findViewById(R.id.layout_nothing);


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
                if (data.getData() != null && data.getPage()!=null && data.getPage().getPage_total()!=null) {
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
            case R.id.right_tv:
                input();
                break;
        }
    }
    /**
     * 首页数据
     *
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
            json.put("lng",ShpfUtil.getStringValue("lng"));
            json.put("lat",ShpfUtil.getStringValue("lat"));
            json.put("page",pageNum);
            json.put("type",type==null?"1":type);
            json.put("keyword",keyword==null?"":keyword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .postString()
                .url(MyURL.URL_RED_RECORD_LIST)
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
                        Log.e("onError", e.toString(), e);
                        scrollView.onRefreshComplete();
                        Util.closeLoading();
                    }

                    @Override
                    public void onResponse(RedListBean response) {
                        scrollView.onRefreshComplete();
                        Util.closeLoading();
                        if (response.getStatus() == 1) {
                            if(pageNum==1){
                                data = response;
                            }else{
                                data.getData().addAll(response.getData());
                            }
                            if(data.getData()==null||data.getData().size()==0){
                                layout_nothing.setVisibility(View.VISIBLE);
                                scrollView.setVisibility(View.GONE);
                            }else{
                                layout_nothing.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);
                            }
                            adapter.setData(data);

                        }
                    }
                });
    }

    @Override
    public void onMyItemClick(Object data) {
        if(data!=null){
            BaseFragment fragment;
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("id",String.valueOf(data));
            fragment = new RedDetaileFragment();
            fragment.setArguments(bundle);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            fragmentTransaction.add(R.id.content, fragment);
            fragmentTransaction.hide(MineRedRecordFragment.this);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public void input(){
        final EditText inputServer = new EditText(this.getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("搜索").setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                keyword = inputServer.getText().toString().trim();
                pageNum=1;
                post_main();

            }
        });
        builder.show();
    }
}
