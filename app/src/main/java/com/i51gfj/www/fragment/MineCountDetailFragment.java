package com.i51gfj.www.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.adapter.CountDetailAdapter;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.model.CountDetailWrapper;
import com.i51gfj.www.model.UserInfoForMinFragementBean;
import com.i51gfj.www.util.AppUtil;
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
public class MineCountDetailFragment extends BaseFragment {
    private TextView tv_all,tv_out,tv_in;
    private TextView tvlist[];
    private CountDetailWrapper data;
    private PullToRefreshScrollView fresh;

    @Override
    public int getLayout() {
        return R.layout.fragment_mine_count_detail;
    }
    private int type = 0;
    private int page= 1;
    CountDetailAdapter adapter;



    @Override
    public void initView(View view) {
        TextView title = (TextView) view.findViewById(R.id.title_tv);
        title.setText("收支明细");
        view.findViewById(R.id.left_tv).setOnClickListener(this);
        tv_all = (TextView) view.findViewById(R.id.tv_all);
        tv_all.setTextColor(getResources().getColor(R.color.index_main));
        tv_in =(TextView)view.findViewById(R.id.tv_in);
        tv_out =(TextView)view.findViewById(R.id.tv_out);
        tv_all.setOnClickListener(this);
        tv_in.setOnClickListener(this);
        tv_out.setOnClickListener(this);
        tvlist = new TextView[]{tv_all,tv_in,tv_out};

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        adapter = new CountDetailAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        fresh = (PullToRefreshScrollView) view.findViewById(R.id.pull_to_refresh_scrollview);
        fresh.setMode(PullToRefreshBase.Mode.BOTH);
        fresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                page = 1;
                post_main(type,page);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                if (data!=null && data.getData() != null) {
                    page++;
                    if (page > Integer.valueOf(data.getPage().getPageTotal())) {
                        Toast.makeText(mActivity, "加载完成，无更多数据", Toast.LENGTH_SHORT).show();
                        fresh.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fresh.onRefreshComplete();
                            }
                        }, 200);

                    } else {
                        post_main(type,page);
                    }
                } else {
                    Toast.makeText(mActivity, "加载完成，无更多数据", Toast.LENGTH_SHORT).show();
                    fresh.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fresh.onRefreshComplete();
                        }
                    }, 200);
                }
            }
        });


    }

    @Override
    public void initData() {
        post_main(type, page);
    }


  /*  uid    *用户id
    page   *分页
    type   *记录的类型　　　1为收入明细  2为支出明细  0为全部*/
    public void post_main(int type, final int page) {
        Util.showLoading(getActivity());
        JSONObject json = new JSONObject();
        try {
            json.put("uid", AppUtil.getUserId());
            json.put("page",page);
            json.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .postString()
                .url(MyURL.URL_AMOUNT_LOG)
                .content(json.toString())
                .build()
                .execute(new Callback<CountDetailWrapper>() {
                    @Override
                    public CountDetailWrapper parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, CountDetailWrapper.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                        Util.closeLoading();
                    }

                    @Override
                    public void onResponse(CountDetailWrapper response) {
                        fresh.onRefreshComplete();
                        if (response.getStatus() == 1) {
                            if (page == 1) {
                                data = response;
                            } else {
                                data.getData().addAll(response.getData());
                            }
                            adapter.setData(data);
                        }
                        Util.closeLoading();
                    }
                });
    }


    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.left_tv:
               getActivity().finish();
               break;
           case R.id.tv_all:
                tvlist[type].setTextColor(getResources().getColor(R.color.gray_deep));
               type = 0;
               tvlist[type].setTextColor(getResources().getColor(R.color.index_main));
               page =1;
               post_main(type, page);
               break;
           case R.id.tv_in:
                tvlist[type].setTextColor(getResources().getColor(R.color.gray_deep));
               type = 1;
               tvlist[type].setTextColor(getResources().getColor(R.color.index_main));
               page =1;
               post_main(type, page);
               break;
           case R.id.tv_out:
                tvlist[type].setTextColor(getResources().getColor(R.color.gray_deep));
               type = 2;
               tvlist[type].setTextColor(getResources().getColor(R.color.index_main));
               page =1;
               post_main(type,page);
               break;
       }
    }
}
