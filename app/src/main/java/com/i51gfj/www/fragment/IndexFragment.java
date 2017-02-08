package com.i51gfj.www.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.activity.CityActivity;
import com.i51gfj.www.activity.IndexMidActivity;
import com.i51gfj.www.activity.SearchStoreActivity;
import com.i51gfj.www.activity.ShopDetailActivity;
import com.i51gfj.www.activity.WebViewActivity2;
import com.i51gfj.www.adapter.IndexAdapter;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.impl.LocationListener;
import com.i51gfj.www.impl.MyItemClickListener;
import com.i51gfj.www.model.JpushBean;
import com.i51gfj.www.model.MainBean;
import com.i51gfj.www.model.UpdaterWrapper;
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
import java.text.DecimalFormat;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/11.
 */
public class IndexFragment extends BaseFragment implements MyItemClickListener {

    private String cityId;  /*   城市id*/
    private String lng;      /*经纬度    极光请求里的经纬度*/
    private String lat;    /*  经纬度    极光请求里的经纬度*/

    private JpushBean city;
    private MainBean data;
    private IndexAdapter adapter;
    private RecyclerView recycler;
    private TextView tv_city;
    private PullToRefreshScrollView scrollView;
    private int  pageNum =1;

    @Override
    public int getLayout() {
        return R.layout.fragment_index;
    }

    @Override
    public void initView(View view) {

        view.findViewById(R.id.img_search).setOnClickListener(this);
        tv_city = (TextView) view.findViewById(R.id.tv_city);
        tv_city.setOnClickListener(this);
        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        FullyLinearLayoutManager manager = new FullyLinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        manager.setCanScroll(false);
        recycler.setLayoutManager(manager);
        adapter = new IndexAdapter(this, mActivity);
        recycler.setAdapter(adapter);

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
                if (data!=null && data.getStore()!=null) {
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
        city = AppUtil.city;
        post_check_update();
        if(AppUtil.indexData ==null){
            getLocation();
        }
        else{
            tv_city.setText(AppUtil.indexData.getCity_name());
            data = AppUtil.indexData;
            adapter.setData(data);
            AppUtil.indexData=null;
        }
    }


    /**
     * 定位
     */
    public void getLocation() {
        Util.showLoading(getActivity());
        new LocationListener(getActivity()).setMyLocationFinishListener(new LocationListener.MyLocationFinishListener() {
            @Override
            public void finish(BDLocation location) {

                if (location.getLocType() == 62) {
                    if (AppUtil.isNetworkAvailable()) {
                        Toast.makeText(mActivity, "无法获取有效定位依据导致定位失败，请确认手机是否允许APP获取定位的信息", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mActivity, "无法获取有效定位依据导致定位失败，" +
                                "一般是由于手机的原因，处于飞行模式下一般会造成这种结果，" +
                                "可以试着重启手机", Toast.LENGTH_LONG).show();
                        Util.closeLoading();
                    }
                } else {
                    //经度
                    double d_lng = location.getLongitude();
                    //维度
                    double d_lat = location.getLatitude();
                    lng = new DecimalFormat("##.000000").format(location.getLongitude());
                    lat = new DecimalFormat("##.000000").format(location.getLatitude());
                    if (d_lng < 10) {
                        lng = "0";
                    }
                    if (d_lat < 10) {
                        lat = "0";
                    }
                    post_jpush("");
                    ShpfUtil.setValue("location_city", location.getCity());
                    Util.closeLoading();
                }
            }
        });
    }

    /**
     * jpushId    极光id
     * version    版本号
     * model      机子类型　ios示例:　6 plus　　　　　android示例:　HM 1SC
     * sdk　　　　环境版本  示例:4.3
     * lng        经纬度    示例  128.235689
     * lat        经纬度    示例  21.235689
     * type       ios或android
     * cityName     城市名称
     */
    public void post_jpush(String cityName) {
        Util.showLoading(getActivity());
        JSONObject json = new JSONObject();
        try {
            //经度(百度的)
            json.put("lng", lng==null?ShpfUtil.getStringValue("lng"):lng);
            //纬度(百度的)
            json.put("lat", lat==null?ShpfUtil.getStringValue("lat"):lat);
            //版本号，传固定参数4.0
            json.put("version", AppUtil.getAppVersion());
            json.put("type", "android");
            json.put("cityName", cityName);
            //设备的信息
            json.put("model", android.os.Build.MODEL);
            json.put("sdk", android.os.Build.VERSION.RELEASE);
            //极光ID
            String registrationID = JPushInterface.getRegistrationID(mActivity.getApplicationContext());
            json.put("jpushId", registrationID);
        } catch (JSONException e1) {
            e1.printStackTrace();
            return;
        }
        OkHttpUtils
                .postString()
                .url(MyURL.URL_JPUSH)
                .content(json.toString())
                .build()
                .execute(new Callback<JpushBean>() {
                    @Override
                    public JpushBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, JpushBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Log.i("indexfragment", "onError: e." + e.toString());
                        Util.closeLoading();
                    }

                    @Override
                    public void onResponse(JpushBean response) {
                        Util.closeLoading();
                        if (response.getStatus() == 1) {
                            city = response;
                            ShpfUtil.setValue("lng", city.getLng());
                            ShpfUtil.setValue("lat", city.getLat());
                            ShpfUtil.setValue("city_id", city.getCityId());
                            ShpfUtil.setValue("city_neme", city.getCityName());
                            tv_city.setText(response.getCityName());
                            post_main();
                        }
                    }
                });
    }

    /**
     * 首页数据
     */
    public void post_main() {
        Util.showLoading(getActivity());
        JSONObject json = new JSONObject();
        try {
            json.put("cityId", city.getCityId());
            json.put("lng", city.getLng());
            json.put("page", pageNum);
            json.put("lat", city.getLat());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .postString()
                .url(MyURL.URL_INDEX)
                .content(json.toString())
                .build()
                .execute(new Callback<MainBean>() {
                    @Override
                    public MainBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, MainBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        scrollView.onRefreshComplete();
                        Util.closeLoading();
                        Log.e("onError", e.toString(), e);
                    }

                    @Override
                    public void onResponse(MainBean response) {
                        scrollView.onRefreshComplete();
                        if (response.getStatus() == 1) {
                            if(pageNum==1){
                                data = response;
                            }else{
                                data.getStore().addAll(response.getStore());
                            }
                            adapter.setData(data);

                        }
                        Util.closeLoading();
                    }
                });
    }
    /**
     * 检查版本
     */
    public void post_check_update() {
        JSONObject json = new JSONObject();
        try {
            json.put("version", AppUtil.getAppVersion());
            json.put("type","android");
        } catch (JSONException e1) {
            e1.printStackTrace();
            return;
        }
        OkHttpUtils
                .postString()
                .url(MyURL.URL_UPDATA_VERSION)
                .content(json.toString())
                .build()
                .execute(new Callback<UpdaterWrapper>() {

                    @Override
                    public UpdaterWrapper parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, UpdaterWrapper.class);

                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                        AppUtil.closeDialog(null);
                    }

                    @Override
                    public void onResponse(final UpdaterWrapper response) {
                        if (response.getIs_down().equals("1")) {
                            new AlertDialog.Builder(mActivity).setTitle("更新提示")//设置对话框标题
                                    .setMessage("有新的版本，是否前往更新？")//设置显示的内容
                                    .setPositiveButton("前往", new DialogInterface.OnClickListener() {//添加确定按钮
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                            if (response.getUrl() != null) {
                                                Uri uri = Uri.parse(response.getUrl());
                                                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                                                startActivity(it);
                                            }
                                        }
                                    }).setNegativeButton("放弃", new DialogInterface.OnClickListener() {//添加返回按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//响应事件
                                }
                            }).show();//在按键响应事件中显示此对话框


                        }
                    }
                });
    }

    @Override
    public void onMyItemClick(Object data) {
        super.onMyItemClick(data);
        if (data == null) {
            return;
        }
        Intent intent = new Intent(mActivity, ShopDetailActivity.class);
        intent.putExtra("passData", (String) data);
        startActivity(intent);
    }

    @Override
    public void onMyItemClick(String passid, String url,String title) {
        super.onMyItemClick(passid, url);
        Log.i("url", "onMyItemClick: "+url);
        if(url!=null && !url.equals("")){
            Intent intent = new Intent(getActivity(), WebViewActivity2.class);
            intent.putExtra("url",url);
            intent.putExtra("title",title);
            getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);
            getActivity().startActivity(intent);
            return;
        }
        if (passid != null && passid.equals("0")) {
            Intent intent = new Intent(mActivity, IndexMidActivity.class);
            intent.putExtra("jump", "redList");
            startActivity(intent);
        } else {
                Intent intent = new Intent(mActivity, IndexMidActivity.class);
                intent.putExtra("jump", "shopList");
                intent.putExtra("id", passid);
                startActivity(intent);
        }

    }

    @Override
    public void onMyItemClick() {
        super.onMyItemClick();
        getLocation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_search:
                Intent intent = new Intent(mActivity, SearchStoreActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.tv_city:
                Intent intent2 = new Intent(mActivity, CityActivity.class);
                mActivity.startActivityForResult(intent2, 11);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void chooseCity(String city) {
        post_jpush(city);
    }
}
