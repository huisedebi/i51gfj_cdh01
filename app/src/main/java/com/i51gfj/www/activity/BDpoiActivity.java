package com.i51gfj.www.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.impl.LocationListener2;
import com.i51gfj.www.impl.MyOnMapStatusChangeListener;
import com.i51gfj.www.model.SearchWrapper;
import com.i51gfj.www.util.ShpfUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class BDpoiActivity extends Activity implements View.OnClickListener {

    private String search;
    private int select_position;
    private ListView listview_search;
    private MySearchAdapter adapter;
    public LocationClient mLocationClient = null;
    //public BDLocationListener myListener = new MyLocationListener();

    private ArrayList<SearchWrapper.Data> searchDatas = new ArrayList<>();

    private TextView top_left;
    private TextView top_title;
    private TextView top_right;
    private EditText et_search;
    MapView mMapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pio_search);
        initView();
    }



    private void initView() {
        top_left = (TextView) findViewById(R.id.left_tv);
        top_title = (TextView) findViewById(R.id.title_tv);
        top_right = (TextView) findViewById(R.id.right_tv);
        mMapView = (MapView) findViewById(R.id.bmapView);

        MyOnMapStatusChangeListener myListen = new MyOnMapStatusChangeListener(mMapView);
        et_search = (EditText)findViewById(R.id.et_search);
        et_search.addTextChangedListener(new EditChangedListener());
        listview_search = (ListView)findViewById(R.id.listview_search);
        adapter = new MySearchAdapter();
        myListen.setCallback(adapter);
        listview_search.setAdapter(adapter);
        if(myListen.poiList!=null){
            adapter.setData(myListen.poiList);
        }
        top_left.setOnClickListener(this);
        top_title.setText("选择位置");
        top_right.setText("确定");
        top_right.setOnClickListener(this);
    }
    public class MySearchAdapter extends BaseAdapter {

        private List<PoiInfo> data;


        @Override
        public int getCount() {
            if(data != null && data.size() != 0){
                return data.size();
            }else{
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.item_poi_list, null);
            TextView tv_name = (TextView)view.findViewById(R.id.tv_name);
            TextView tv_intro = (TextView)view.findViewById(R.id.tv_intro);
            tv_name.setText(data.get(position).name);
            tv_intro.setText(data.get(position).address);
            view.findViewById(R.id.layout_item_poi).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select_position = position;
                    notifyDataSetChanged();
                }
            });
            if(position == select_position){
                view.findViewById(R.id.img_select).setVisibility(View.VISIBLE);
            }
            return view;
        }

        public void setData(List<PoiInfo> poiList) {
            data  = poiList;
            select_position = 0;
            notifyDataSetChanged();
        }
    }

    class EditChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            search = et_search.getText().toString().trim();
            if("".equals(search)){
                listview_search.setVisibility(View.GONE);
            }else{

            }
//            Log.e("search", "" + isChinese(search));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_tv:
                finish();
                break;
        }
    }
    public Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
           if(msg.what==0){

               Toast.makeText(getApplicationContext(), "HandleMessage 1", Toast.LENGTH_SHORT).show();
           }

            return false;
        }
    }) {


    };



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
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}
