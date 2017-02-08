package com.i51gfj.www.activity;

import android.app.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.i51gfj.www.R;
import com.i51gfj.www.util.ShpfUtil;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.i51gfj.www.util.Util;

public class BNDemoMainActivity extends FragmentActivity implements View.OnClickListener{

    FrameLayout frameLayout;
    MapView mMapView;
    BaiduMap mBaiduMap;
    private double lng;
    private double lat;
    private double store_lat;
    private double store_lng;
    LatLng latLng ;
    LatLng storelatLng ;
    String store_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Util.showLoading(this);
        initData();
        initView();
        Util.closeLoading();
    }

    private void initData() {
        lng = Double.parseDouble(ShpfUtil.getStringValue("lng"));
        lat = Double.parseDouble(ShpfUtil.getStringValue("lat"));
        store_lat = Double.parseDouble(getIntent().getStringExtra("store_lat"));
        store_lng = Double.parseDouble(getIntent().getStringExtra("store_lng"));

    }

    private void initView() {
        findViewById(R.id.tv_gomap).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.title_tv);
        title.setText("地图");
        TextView name = (TextView) findViewById(R.id.tv_name);
        store_name = getIntent().getStringExtra("store_name");
        name.setText(getIntent().getStringExtra("store_name"));
        TextView address = (TextView) findViewById(R.id.tv_address);
        address.setText(getIntent().getStringExtra("store_address"));
        findViewById(R.id.left_tv).setOnClickListener(this);
        frameLayout = (FrameLayout) findViewById(R.id.layout_map);
        mMapView = new MapView(this,new BaiduMapOptions());
         latLng = new LatLng(lat,lng);
         storelatLng = new LatLng(store_lat,store_lng);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.point4);
        BitmapDescriptor bitmap2 = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        OverlayOptions option = new MarkerOptions()
                .position(latLng).icon(bitmap);
        OverlayOptions storeoption = new MarkerOptions()
                .position(storelatLng)
                .icon(bitmap2);
        mBaiduMap = mMapView.getMap();
        TextView tv = new TextView(this);
        if(store_name!=null && !store_name.equals("")){
            tv.setText(store_name);
        }else{
            tv.setText("位置目标");
        }
        InfoWindow mInfoWindow = new InfoWindow(tv, storelatLng, -47);
        mBaiduMap.showInfoWindow(mInfoWindow);
        mBaiduMap.addOverlay(option);
        mBaiduMap.addOverlay(storeoption);
        mBaiduMap.setMyLocationEnabled(true);
        LatLng ll = new LatLng(lat,lng);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
        // 移动到某经纬度
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(12));
        mBaiduMap.animateMapStatus(update);
        mMapView.showZoomControls(false);
        frameLayout.addView(mMapView);
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {

        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_tv:
                finish();
                break;
            case R.id.tv_gomap:
               startRoutePlanDriving();
                break;
        }
    }


    public void startRoutePlanDriving() {
        LatLng ptStart = new LatLng(lat, lng);
        LatLng ptEnd = new LatLng(store_lat, store_lng);

        // 构建 route搜索参数
        RouteParaOption para = new RouteParaOption()
                .startPoint(ptStart)
            .startName(store_name)
            .endPoint(ptEnd);
        try {
            BaiduMapRoutePlan.openBaiduMapDrivingRoute(para, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
