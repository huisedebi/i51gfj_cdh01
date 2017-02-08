package com.i51gfj.www.view.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.i51gfj.www.R;
import com.i51gfj.www.application.MyApplication;
import com.qyx.android.weight.utils.baidumap.BaiDuMapUtil;
import com.qyx.android.weight.utils.baidumap.IGetLocationListener;

public class LocationReportActivity extends Activity implements OnClickListener, OnGetGeoCoderResultListener {

	BitmapDescriptor mCurrentMarker;
	MapView mMapView;
	BaiduMap mBaiduMap;
	boolean isFirstLoc = true;
	GeoCoder mSearch = null; 
	TextView showAddr;
	private TextView send;
	private double lLatitude = 0.0;
	private double lLongitude = 0.0;
	boolean is_exit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//SDKInitializer.initialize(this.getApplicationContext());
		setContentView(R.layout.baidumap);

		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		
		showAddr = (TextView) findViewById(R.id.map_bubbleText);
		mMapView = (MapView) findViewById(R.id.map_view);
		mBaiduMap = mMapView.getMap();
//		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
//			public void onMapClick(LatLng point) {
//				SearchButtonProcess(point);
//			}
//
//			public boolean onMapPoiClick(MapPoi poi) {
//				return false;
//			}
//		});
		
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mBaiduMap.setMyLocationEnabled(true);
		float zoomLevel = 16;
		MapStatusUpdate u2 = MapStatusUpdateFactory.zoomTo(zoomLevel);
		mBaiduMap.animateMapStatus(u2);

		getLocation();

		findViewById(R.id.back_layout).setOnClickListener(this);
		((TextView) findViewById(R.id.title_textview))
				.setText(R.string.my_location);
		send = (TextView) findViewById(R.id.title_right_tv);
		send.setText(R.string.save);
		findViewById(R.id.title_right_layout).setVisibility(View.VISIBLE);
		findViewById(R.id.title_right_layout).setOnClickListener(this);

	}
	
	private void getLocation() {
		MyApplication.getInstance().GetBaiduLoacationUtil().getLoaction(
				new IGetLocationListener() {

					@Override
					public void onSuccess(BDLocation location) {
						if(is_exit || isFinishing()) {
							return;
						}
						MyLocationData locData = new MyLocationData.Builder()
						.latitude(location.getLatitude())
						.longitude(location.getLongitude()).build();
						mBaiduMap.setMyLocationData(locData);
						if (isFirstLoc) {
							isFirstLoc = false;
							LatLng ll = new LatLng(location.getLatitude(),
									location.getLongitude());
							MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
							mBaiduMap.animateMapStatus(u);
						}
		
						showAddr.setText(location.getAddrStr());
						lLatitude = location.getLatitude();
						lLongitude = location.getLongitude();
					}

					@Override
					public void onFail() {
					}
				});
	}

	public void SearchButtonProcess(LatLng mLatLng) {
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(mLatLng));
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			back();

			break;
		case R.id.title_right_layout:
			if (lLongitude != 0.0) {
				Intent intent = new Intent();
				double[] lat_lng = BaiDuMapUtil.baiduToGaode(lLatitude, lLongitude);
				lLatitude = lat_lng[0];
				lLongitude = lat_lng[1];
				intent.putExtra("locationlng", lLongitude);
				intent.putExtra("locationlat", lLatitude);
				intent.putExtra("addr", showAddr.getText().toString());
				setResult(RESULT_OK, intent);
				back();

			} else {
			}
			break;
		default:
			break;
		}
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
	public void onBackPressed() {
		back();
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		showAddr.setText(arg0.getAddress());
		lLatitude = arg0.getLocation().latitude;
		lLongitude = arg0.getLocation().longitude;
	}
	
	private void back() {
		is_exit = true;
		finish();
	}
}