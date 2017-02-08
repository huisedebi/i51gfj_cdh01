package com.i51gfj.www.impl;

import android.os.Message;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.i51gfj.www.R;
import com.i51gfj.www.activity.BDpoiActivity;
import com.i51gfj.www.util.ShpfUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/10/13.
 */
public class MyOnMapStatusChangeListener implements BaiduMap.OnMapStatusChangeListener {
    public MapView mMapView;
    public BaiduMap mBaiduMap;
    OverlayOptions ooA;
    public PoiSearch mPoisearch = PoiSearch.newInstance();
    public List<PoiInfo> poiList;
    private BDpoiActivity.MySearchAdapter adapter;


    public MyOnMapStatusChangeListener(MapView mMapView) {
        this.mMapView = mMapView;
        this.mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        LatLng latlng = new LatLng(Double.valueOf(ShpfUtil.getStringValue("lat")),Double.valueOf(ShpfUtil.getStringValue("lng")));
        ooA = new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding)).zIndex(4).draggable(true);
        mBaiduMap.addOverlay(ooA);
        mBaiduMap.setMyLocationEnabled(true);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latlng);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
        mBaiduMap.animateMapStatus(update);
        mBaiduMap.setOnMapStatusChangeListener(this);
        serchNeayBy(latlng);
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        LatLng la = mBaiduMap.getMapStatus().target;
        mBaiduMap.clear();
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        OverlayOptions option = new MarkerOptions()
                .position(la).icon(bitmap);
        mBaiduMap.addOverlay(option);
        mBaiduMap.setMyLocationEnabled(true);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(la);
        mBaiduMap.animateMapStatus(update);
        serchNeayBy(la);
    }


    private void serchNeayBy(LatLng ll) {
        PoiNearbySearchOption option = new PoiNearbySearchOption();
        option.sortType(PoiSortType.distance_from_near_to_far);
        option.location(ll);
        option.radius(2000);
        option.pageCapacity(10);
        option.keyword("楼");
        mPoisearch.searchNearby(option);
        mPoisearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {


            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if (poiResult != null) {
                    if (poiResult.getAllPoi() != null && poiResult.getAllPoi().size() > 0) {
                        poiList = poiResult.getAllPoi();
                        adapter.setData(poiList);

                    }
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }
        });
    }

    public void setCallback(BDpoiActivity.MySearchAdapter adapter) {
        this.adapter =adapter;
    }
}
