package com.i51gfj.www.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiResult;
import com.i51gfj.www.R;
import com.i51gfj.www.util.Tool;
import com.i51gfj.www.util.Util;
import com.i51gfj.www.view.map.AroundPoiAdapter;
import com.i51gfj.www.view.map.SearchPoiAdapter;
import com.qyx.android.weight.utils.baidumap.BaiduMapUtilByRacer;
import com.qyx.android.weight.utils.baidumap.LocationBean;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import io.rong.message.LocationMessage;

public class MapLocationActivity2 extends AppCompatActivity {

    private LocationBean mLocationBean;
    private List<PoiInfo> aroundPoiList;
    private AroundPoiAdapter mAroundPoiAdapter;
    private Marker mMarker = null;
    private static List<LocationBean> searchPoiList;
    private SearchPoiAdapter mSearchPoiAdapter;
    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private EditText etMLCityPoi;
    private TextView tvShowLocation;
    private LinearLayout llMLMain;
    private ListView lvAroundPoi, lvSearchPoi;
    private ImageView ivMLPLoading;
    private Button btMapZoomIn, btMapZoomOut;
    private ImageButton ibMLLocate;
    public static final int SHOW_MAP = 0;
    private static final int SHOW_SEARCH_RESULT = 1;
    private static final int DELAY_DISMISS = 1000 * 30;
    private boolean is_show_more = false;
    private PoiInfo selectedPoiInfo = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapview_location_poi);
        Util.showLoading(this);
        initView();
        ibMLLocate = (ImageButton) findViewById(R.id.ibMLLocate);
        etMLCityPoi = (EditText) findViewById(R.id.etMLCityPoi);
        etMLCityPoi.setVisibility(View.GONE);
        tvShowLocation = (TextView) findViewById(R.id.tvShowLocation);
        lvAroundPoi = (ListView) findViewById(R.id.lvPoiList);
        lvSearchPoi = (ListView) findViewById(R.id.lvMLCityPoi);
        ivMLPLoading = (ImageView) findViewById(R.id.ivMLPLoading);
        btMapZoomIn = (Button) findViewById(R.id.btMapZoomIn);
        btMapZoomOut = (Button) findViewById(R.id.btMapZoomOut);
        llMLMain = (LinearLayout) findViewById(R.id.llMLMain);
        mMapView = (MapView) findViewById(R.id.mMapView);
        BaiduMapUtilByRacer.goneMapViewChild(mMapView, true, true);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
        mBaiduMap.setOnMapStatusChangeListener(mapStatusChangeListener);
        mBaiduMap.setOnMapClickListener(mapOnClickListener);
        mBaiduMap.getUiSettings().setZoomGesturesEnabled(false);
        mBaiduMap.setMyLocationEnabled(true);
        locate();
        iniEvent();
    }

    private void initView() {
        ((TextView) findViewById(R.id.title_textview))
                .setText(R.string.my_location);
        ((TextView) findViewById(R.id.title_right_tv)).setText(R.string.sure);
        findViewById(R.id.title_right_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.back_layout).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        finish();
                    }
                });
        findViewById(R.id.title_right_layout).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (selectedPoiInfo != null) {
                           /* Uri uri = Uri.parse("http://api.map.baidu.com/staticimage?width = 300 & height = 200 & center = " + selectedPoiInfo.location.longitude + ", " + selectedPoiInfo.location.latitude + " & zoom = 17 & markers = " + selectedPoiInfo.location.longitude +"," + selectedPoiInfo.location.latitude + "&markerStyles=m,A");
                            LocationMessage locationMessage = LocationMessage.obtain(selectedPoiInfo.location.longitude, selectedPoiInfo.location.latitude,selectedPoiInfo.address, uri);
                            //如果地图地位成功，那么调用
                            Tool.mLastLocationCallback.onSuccess(locationMessage);
                //如果地图地位失败，那么调用
                            Tool.mLastLocationCallback.onFailure("定位失败!");
                            finish();*/
                            Uri uri = Uri.parse("http://api.map.baidu.com/staticimage?width=300&height=200&center=" + selectedPoiInfo.location.longitude + "," + selectedPoiInfo.location.latitude + "&zoom=17&markers=" + selectedPoiInfo.location.longitude + "," + selectedPoiInfo.location.latitude + "&markerStyles=m,A");
                            LocationMessage locationMessage = LocationMessage.obtain(selectedPoiInfo.location.latitude, selectedPoiInfo.location.longitude, selectedPoiInfo.address, uri);
                            //如果地图地位成功，那么调用
                            Tool.mLastLocationCallback.onSuccess(locationMessage);
               /* //如果地图地位失败，那么调用
                Tool.mLastLocationCallback.onFailure("定位失败!");*/
                            finish();
                        } else {
                            Util.closeLoading();
                            Toast.makeText(
                                    MapLocationActivity2.this,
                                    getResources().getString(
                                            R.string.locate_failed),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showMapOrSearch(int index) {
        if (index == SHOW_SEARCH_RESULT) {
            llMLMain.setVisibility(View.GONE);
            lvSearchPoi.setVisibility(View.VISIBLE);
        } else {
            lvSearchPoi.setVisibility(View.GONE);
            llMLMain.setVisibility(View.VISIBLE);
            if (searchPoiList != null) {
                searchPoiList.clear();
            }
        }
    }

    public void locate() {
        BaiduMapUtilByRacer.locateByBaiduMap(MapLocationActivity2.this, 2000,
                new BaiduMapUtilByRacer.LocateListener() {

                    @Override
                    public void onLocateSucceed(LocationBean locationBean) {
                        Util.closeLoading();
                        mLocationBean = locationBean;
                        if (mMarker != null) {
                            mMarker.remove();
                        } else {
                            if (mBaiduMap != null)
                                mBaiduMap.clear();
                        }
                        mMarker = BaiduMapUtilByRacer.showMarkerByResource(
                                locationBean.getLatitude(),
                                locationBean.getLongitude(), R.drawable.point4,
                                mBaiduMap, 0, true);
                    }

                    @Override
                    public void onLocateFiled() {
                        Util.closeLoading();
                    }

                    @Override
                    public void onLocating() {

                    }
                });
    }

    public void getPoiByPoiSearch() {
        BaiduMapUtilByRacer.getPoiByPoiSearch(mLocationBean.getCity(),
                etMLCityPoi.getText().toString().trim(), 0,
                new BaiduMapUtilByRacer.PoiSearchListener() {

                    @Override
                    public void onGetSucceed(List<LocationBean> locationList,
                                             PoiResult res) {
                        if (etMLCityPoi.getText().toString().trim().length() > 0) {
                            if (searchPoiList == null) {
                                searchPoiList = new ArrayList<LocationBean>();
                            }
                            searchPoiList.clear();
                            searchPoiList.addAll(locationList);
                            updateCityPoiListAdapter();
                        }
                    }

                    @Override
                    public void onGetFailed() {
                        Toast.makeText(MapLocationActivity2.this, "暂无结果",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void reverseGeoCode(final LatLng ll, final boolean isShowTextView) {
        BaiduMapUtilByRacer.getPoisByGeoCode(ll.latitude, ll.longitude,
                new BaiduMapUtilByRacer.GeoCodePoiListener() {

                    @Override
                    public void onGetSucceed(LocationBean locationBean,
                                             List<PoiInfo> poiList) {
                        mLocationBean = (LocationBean) locationBean.clone();

                        if (isShowTextView && tvShowLocation != null) {
                            if (is_show_more) {
                                is_show_more = false;
                            } else {
                                if (poiList != null) {
                                    tvShowLocation.setText(poiList.get(0).name);
                                    selectedPoiInfo = poiList.get(0);
                                }
                            }
                        }
                        if (poiList != null) {
                            if (aroundPoiList == null) {
                                aroundPoiList = new ArrayList<PoiInfo>();
                                aroundPoiList.addAll(poiList);
                                selectedPoiInfo = aroundPoiList.get(0);
                                updatePoiListAdapter(aroundPoiList);
                            } else {
                                getNewLists(poiList);
                                updatePoiListAdapter(aroundPoiList);
                            }
                        } else {
                        }
                    }

                    @Override
                    public void onGetFailed() {

                    }
                });
    }

    private void iniEvent() {
        etMLCityPoi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (etMLCityPoi.getText().toString().trim().length() > 0) {
                    getPoiByPoiSearch();
                }
            }
        });
        etMLCityPoi.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int start, int before,
                                      int count) {
                if (cs.toString().trim().length() > 0) {
                    getPoiByPoiSearch();
                } else {
                    if (searchPoiList != null) {
                        searchPoiList.clear();
                    }
                    showMapOrSearch(SHOW_MAP);
                    hideSoftKeyboard(etMLCityPoi);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ibMLLocate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                locate();
            }
        });
        btMapZoomIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isCanUpdateMap = false;
                BaiduMapUtilByRacer.zoomInMapView(mMapView);
            }
        });
        btMapZoomOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isCanUpdateMap = false;
                BaiduMapUtilByRacer.zoomOutMapView(mMapView);
            }
        });
        lvAroundPoi.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                isCanUpdateMap = false;
                is_show_more = true;
                BaiduMapUtilByRacer.moveToTarget(
                        aroundPoiList.get(arg2).location.latitude,
                        aroundPoiList.get(arg2).location.longitude, mBaiduMap);
                selectedPoiInfo = aroundPoiList.get(arg2);
                mAroundPoiAdapter.setSelctedLatAndLon(selectedPoiInfo);
            }
        });
        lvSearchPoi.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Uri uri = Uri.parse("http://api.map.baidu.com/staticimage?width=300&height=200&center=" + selectedPoiInfo.location.longitude + "," + selectedPoiInfo.location.latitude + "&zoom=17&markers=" + selectedPoiInfo.location.longitude + "," + selectedPoiInfo.location.latitude + "&markerStyles=m,A");
                LocationMessage locationMessage = LocationMessage.obtain(selectedPoiInfo.location.longitude, selectedPoiInfo.location.latitude, selectedPoiInfo.address, uri);
                //如果地图地位成功，那么调用
                Tool.mLastLocationCallback.onSuccess(locationMessage);
               /* //如果地图地位失败，那么调用
                Tool.mLastLocationCallback.onFailure("定位失败!");*/
                finish();

            }
        });
    }


    @Override
    public void onBackPressed() {
        if (llMLMain.getVisibility() == View.GONE) {
            showMapOrSearch(SHOW_MAP);
        } else {
            this.finish();
        }
    }

    BaiduMap.OnMapClickListener mapOnClickListener = new BaiduMap.OnMapClickListener() {
        public void onMapClick(LatLng point) {
            hideSoftKeyboard(etMLCityPoi);
        }

        public boolean onMapPoiClick(MapPoi poi) {
            return false;
        }
    };
    private boolean isCanUpdateMap = true;
    BaiduMap.OnMapStatusChangeListener mapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        public void onMapStatusChangeStart(MapStatus status) {
        }

        public void onMapStatusChange(MapStatus status) {
        }

        public void onMapStatusChangeFinish(MapStatus status) {
            if (isCanUpdateMap) {
                LatLng ptCenter = new LatLng(status.target.latitude,
                        status.target.longitude);
                if (!is_show_more && aroundPoiList != null) {
                    aroundPoiList.clear();
                    aroundPoiList = null;
                }
                reverseGeoCode(ptCenter, true);

            } else {
                isCanUpdateMap = true;
            }
        }
    };
    private static Animation hyperspaceJumpAnimation = null;
    Handler loadingHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case 0: {
                    // if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    // mLoadingDialog.dismiss();
                    // // showToast(mActivity.getString(R.string.map_locate_fault),
                    // // DialogType.LOAD_FAILURE);
                    // }
                    if (ivMLPLoading != null) {
                        ivMLPLoading.clearAnimation();
                        ivMLPLoading.setVisibility(View.GONE);
                    }
                    break;
                }
                case 1: {
                    // ���ض���
                    hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                            MapLocationActivity2.this, R.anim.dialog_loading_animation);
                    lvAroundPoi.setVisibility(View.GONE);
                    ivMLPLoading.setVisibility(View.VISIBLE);
                    // ʹ��ImageView��ʾ����
                    ivMLPLoading.startAnimation(hyperspaceJumpAnimation);
                    if (ivMLPLoading != null
                            && ivMLPLoading.getVisibility() == View.VISIBLE) {
                        loadingHandler.sendEmptyMessageDelayed(0, DELAY_DISMISS);
                    }
                    break;
                }
                default:
                    break;
            }

            return false;
        }
    });

    private void updatePoiListAdapter(List<PoiInfo> list) {
        ivMLPLoading.clearAnimation();
        ivMLPLoading.setVisibility(View.GONE);
        lvAroundPoi.setVisibility(View.VISIBLE);
        if (mAroundPoiAdapter == null) {
            mAroundPoiAdapter = new AroundPoiAdapter(MapLocationActivity2.this,
                    list);
            lvAroundPoi.setAdapter(mAroundPoiAdapter);
        } else {
            mAroundPoiAdapter.setNewList(list);
        }

        mAroundPoiAdapter.setSelctedLatAndLon(selectedPoiInfo);

    }

    private void updateCityPoiListAdapter() {
        if (mSearchPoiAdapter == null) {
            mSearchPoiAdapter = new SearchPoiAdapter(MapLocationActivity2.this,
                    searchPoiList);
            lvSearchPoi.setAdapter(mSearchPoiAdapter);
        } else {
            mSearchPoiAdapter.notifyDataSetChanged();
        }
        showMapOrSearch(SHOW_SEARCH_RESULT);
    }

    @Override
    protected void onDestroy() {
        mLocationBean = null;
        lvAroundPoi = null;
        lvSearchPoi = null;
        btMapZoomIn.setBackgroundResource(0);
        btMapZoomIn = null;
        btMapZoomOut.setBackgroundResource(0);
        btMapZoomOut = null;
        ibMLLocate.setImageBitmap(null);
        ibMLLocate.setImageResource(0);
        ibMLLocate = null;
        if (aroundPoiList != null) {
            aroundPoiList.clear();
            aroundPoiList = null;
        }
        mAroundPoiAdapter = null;
        if (searchPoiList != null) {
            searchPoiList.clear();
            searchPoiList = null;
        }
        mSearchPoiAdapter = null;
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationEnabled(false);// �رն�λͼ��
            mBaiduMap = null;
        }
        if (mMapView != null) {
            mMapView.destroyDrawingCache();
            mMapView.onDestroy();
            mMapView = null;
        }
        if (etMLCityPoi != null) {
            etMLCityPoi.setBackgroundResource(0);
            etMLCityPoi = null;
        }
        mMarker = null;
        super.onDestroy();
        System.gc();
    }

    private void getNewLists(List<PoiInfo> newList) {
        List<PoiInfo> temp = new ArrayList<PoiInfo>();
        for (int i = 0, size = aroundPoiList.size(); i < size; i++) {
            for (int j = 0, length = newList.size(); j < length; j++) {
                if (newList.get(j).location.latitude == aroundPoiList.get(i).location.latitude
                        && newList.get(j).location.longitude == aroundPoiList
                        .get(i).location.longitude) {
                    temp.add(newList.get(j));
                }
            }
        }

        if (temp.size() > 0) {
            newList.removeAll(temp);
        }
        aroundPoiList.addAll(aroundPoiList.size(), newList);
    }

    public void hideSoftKeyboard(EditText edit_info_edittext) {
        if (edit_info_edittext == null) {
            return;
        }
	/*	InputMethodManager imm = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit_info_edittext.getWindowToken(), 0);*/
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
        MobclickAgent.onResume(this);
        JPushInterface.onResume(this);

    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);

    }


}