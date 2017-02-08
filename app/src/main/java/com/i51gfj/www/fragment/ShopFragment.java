package com.i51gfj.www.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.activity.MainActivity;
import com.i51gfj.www.activity.ShopDetailActivity;
import com.i51gfj.www.adapter.ShopAdapter;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.model.ShopBean;
import com.i51gfj.www.util.ShpfUtil;
import com.i51gfj.www.util.Util;
import com.i51gfj.www.view.ExpandTabView;
import com.i51gfj.www.view.FullyLinearLayoutManager;
import com.i51gfj.www.view.ViewLeft;
import com.i51gfj.www.view.ViewMiddle;
import com.i51gfj.www.view.ViewRight;
import com.i51gfj.www.view.pulltorefresh.PullToRefreshBase;
import com.i51gfj.www.view.pulltorefresh.PullToRefreshScrollView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/11.
 */
public class ShopFragment extends BaseFragment {



    private TextView left_tv,title_tv,right_tv;
    public RecyclerView recycler;
    private ExpandTabView expandTabView;
    private ArrayList<View> mViewArray = new ArrayList<View>();
    private ViewLeft viewRight;
    private ViewLeft viewMiddle;
    //private ViewRight viewRight;
    private ShopAdapter adapter;
    private TextView adress;
    public ShopBean data;
    private String cid;
    private String tid;
    private String orderType;
    PullToRefreshScrollView scrollView;
    private int pageNum = 1;
    private RelativeLayout layout_nothing;
    private LinearLayout layout_data;

    @Override
    public int getLayout() {
     return R.layout.activity_main;
    }

    @Override
    public void initView(View view) {
        layout_data = (LinearLayout) view.findViewById(R.id.layout_data);
        layout_nothing = (RelativeLayout) view.findViewById(R.id.layout_nothing);
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
        expandTabView = (ExpandTabView) view.findViewById(R.id.expandtab_view);
        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        adress = (TextView) view.findViewById(R.id.adress);
        FullyLinearLayoutManager manager = new FullyLinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        manager.setCanScroll(false);
        recycler.setLayoutManager(manager);
        adapter = new ShopAdapter(this,mActivity);
        recycler.setAdapter(adapter);
        viewRight = new ViewLeft(mActivity);
        viewMiddle = new ViewLeft(mActivity);
//        viewRight = new ViewRight(mActivity);
        view.findViewById(R.id.img_frash).setOnClickListener(this);
        title_tv = (TextView) view.findViewById(R.id.title_tv);
        title_tv.setText("商家");
        left_tv = (TextView) view.findViewById(R.id.left_tv);
        left_tv.setOnClickListener(this);
        if(getActivity() instanceof MainActivity){
            left_tv.setVisibility(View.GONE);
            view.findViewById(R.id.left_tv2).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initData() {
        mViewArray.add(viewMiddle);
        mViewArray.add(viewRight);
//        mViewArray.add(viewRight);
        ArrayList<String> mTextArray = new ArrayList<String>();
        mTextArray.add("全部分类");
        mTextArray.add("默认");
//        mTextArray.add("排序");
        expandTabView.setValue(mTextArray, mViewArray);
        expandTabView.setTitle("全部分类", 0);
        expandTabView.setTitle(viewRight.getShowText(), 1);
//        expandTabView.setTitle(viewRight.getShowText(), 2);
        if(getArguments()!=null){
            cid = getArguments().getString("id","");
        }
        initListener();
        post_main();
    }

    private void initListener() {

        viewRight.setOnSelectListener(new ViewLeft.OnSelectListener() {

            @Override
            public void getValue(String distance, String showText) {
                //onRefresh(viewLeft, showText);
                expandTabView.onPressBack();
                pageNum =1;
                orderType = distance;
                int position = getPositon(viewRight);
                if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
                    expandTabView.setTitle(showText, position);
                }
                post_main();

            }
        });

        viewMiddle.setOnSelectListener(new ViewLeft.OnSelectListener() {

            @Override
            public void getValue(String distance, String showText) {
                //onRefresh(viewLeft, showText);
                expandTabView.onPressBack();
                pageNum =1;
                cid =distance;
                int position = getPositon(viewMiddle);
                if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
                    expandTabView.setTitle(showText, position);
                }
                post_main();

            }
        });
       /* viewMiddle.setOnSelectListener(new ViewMiddle.OnSelectListener() {

            @Override
            public void getValue(String showText, int firstPosizion, int secondPosizion) {
                expandTabView.onPressBack();
                pageNum =1;
                cid =data.getCateList().get(firstPosizion).getList().get(secondPosizion).getCid();
                tid =data.getCateList().get(firstPosizion).getList().get(secondPosizion).getTid();
                int position = getPositon(viewMiddle);
                if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
                    expandTabView.setTitle(showText, position);
                }
                post_main();
            }


        });*/
    }


    private int getPositon(View tView) {
        for (int i = 0; i < mViewArray.size(); i++) {
            if (mViewArray.get(i) == tView) {
                return i;
            }
        }
        return -1;
    }
    /**
     * cityId     *城市id
     lng        *经纬度    极光请求里的经纬度
     lat        *经纬度    极光请求里的经纬度
     cid        商品分类ID
     tid        商品二级分类id
     page       分页
     keyword    搜索的关键词
     qid        商圈id
     aid        区id
     orderType  排序
     *
     */
    public void post_main() {
        Util.showLoading(getActivity());
        JSONObject json = new JSONObject();
        try {
            json.put("cityId", ShpfUtil.getStringValue("city_id"));
            json.put("lng",ShpfUtil.getStringValue("lng"));
            json.put("lat",ShpfUtil.getStringValue("lat"));
            json.put("page",pageNum);
            json.put("cid",cid==null?"":cid);
            json.put("tid",tid==null?"":tid);
            json.put("orderType",orderType==null?"":orderType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpUtils
                .postString()
                .url(MyURL.URL_STORE_LIST)
                .content(json.toString())
                .build()
                .execute(new Callback<ShopBean>() {
                    @Override
                    public ShopBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, ShopBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                        scrollView.onRefreshComplete();
                        Util.closeLoading();
                    }

                    @Override
                    public void onResponse(ShopBean response) {
                        scrollView.onRefreshComplete();
                        Util.closeLoading();
                        if (response.getStatus() == 1) {
                            if(response.getData()==null || response.getData().size()==0){
                                layout_nothing.setVisibility(View.VISIBLE);
                                layout_data.setVisibility(View.GONE);
                            }else{
                                layout_nothing.setVisibility(View.GONE);
                                layout_data.setVisibility(View.VISIBLE);
                            }
                            if (pageNum == 1) {
                                data = response;
                            } else {
                                data.getData().addAll(response.getData());
                            }
                            viewMiddle.setItemsData(data.getCateList(),getActivity());
                            viewRight.setItemsRight(data.getNavs(),getActivity());
                            adress.setText(data.getText1());
                            adapter.setData(data);
                        }
                    }

                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_tv:
                getActivity().finish();
                break;
            case R.id.img_frash:
                post_main();
                break;
        }
    }


    @Override
    public void onMyItemClick(Object data) {
       if(data!=null){
           Intent intent = new Intent(getActivity(), ShopDetailActivity.class);
           intent.putExtra("passData",(String)data);
           getActivity().startActivity(intent);
       }
    }
}
