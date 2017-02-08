
package com.i51gfj.www.impl;

import android.content.Context;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
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
import com.baidu.mapapi.utils.CoordinateConverter;
import com.i51gfj.www.R;

import java.util.List;


/**
 * 百度地图poi
 */

public class LocationListener2 implements BDLocationListener, BaiduMap.OnMapStatusChangeListener {


    private final LocationClient mLocClient;
    public MapView mapView;
    public BaiduMap mBaiduMap;
    private double mCurrentLantitude;
    private double mCurrentLongitude;
    PoiSearch mPoisearch = PoiSearch.newInstance();
    public List<PoiInfo> poiList;

    public LocationListener2(Context context,MapView view) {
        mapView = view;
        mBaiduMap = view.getMap();
        mBaiduMap.setOnMapStatusChangeListener(this);
        mLocClient = new LocationClient(context);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(0);
        option.setIsNeedAddress(true);
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIsNeedLocationPoiList(true);
        option.setIsNeedLocationDescribe(true);
        option.setProdName("i51gfj");
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if(location==null){
            return;
        }
        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        OverlayOptions ooA = new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding)).zIndex(4).draggable(true);
        mBaiduMap.addOverlay(ooA);
        mBaiduMap.setMyLocationEnabled(true);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latlng);
        mBaiduMap.animateMapStatus(update);
    }

    private void serchNeayBy() {
        PoiNearbySearchOption option = new PoiNearbySearchOption();
        option.sortType(PoiSortType.distance_from_near_to_far);
        option.location(new LatLng(mCurrentLantitude, mCurrentLongitude));
        option.radius(2000);
        option.pageCapacity(10);
        option.keyword("");
        mPoisearch.searchNearby(option);
        mPoisearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {


            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if(poiResult !=null){
                    if(poiResult.getAllPoi()!=null&& poiResult.getAllPoi().size()>0){
                        poiList = poiResult.getAllPoi();
                        Message msg = new Message();
                        msg.what =0;

                    }
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }
        });
    }
    private void showMap(double latitude, double longtitude, String address) {
        LatLng llA = new LatLng(latitude, longtitude);
        CoordinateConverter converter = new CoordinateConverter();
        converter.coord(llA);
        converter.from(CoordinateConverter.CoordType.COMMON);
        LatLng convertLatLng = converter.convert();
        OverlayOptions ooA = new MarkerOptions().position(convertLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.point4)).zIndex(4).draggable(true);
        //markerA = (Marker) (mBaiduMap.addOverlay(ooA));
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 16.0f);
        mBaiduMap.animateMapStatus(u);
        new Thread(new Runnable() {
            @Override
            public void run() {
                serchNeayBy();
            }
        }).start();
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {

    }
}


