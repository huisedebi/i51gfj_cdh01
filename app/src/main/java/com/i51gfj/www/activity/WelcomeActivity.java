package com.i51gfj.www.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.impl.LocationListener;
import com.i51gfj.www.model.JpushBean;
import com.i51gfj.www.model.MainBean;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.ShpfUtil;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Response;


public class WelcomeActivity extends Activity implements Runnable {

    private static final int sleepTime = 1500;
    //是否第一次使用
    private boolean isFirstUse;
    private String lng;
    private String lat;
    private JpushBean city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getLocation();

        //启动一个延迟线程
        // new Thread(this).start();
    }

    public void run() {
        try {
            //延迟两秒
            Thread.sleep(sleepTime);
            //读取SharedPreferences中需要的数据
            SharedPreferences preferences = getSharedPreferences("isFirstUse", MODE_WORLD_READABLE);
            isFirstUse = preferences.getBoolean("isFirstUse", true);
            /**
             *如果用户不是第一次使用则直接调转到显示界面,否则调转到引导界面
             */
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            if (isFirstUse) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            }
            finish();

            //实例化Editor对象
            Editor editor = preferences.edit();
            //存入数据
            editor.putBoolean("isFirstUse", false);
            //提交修改
            editor.commit();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



  /*  @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }*/

    /**
     * 定位
     */
    public void getLocation() {
        new LocationListener(this).setMyLocationFinishListener(new LocationListener.MyLocationFinishListener() {
            @Override
            public void finish(BDLocation location) {

                if (location.getLocType() == 62) {
                    if (AppUtil.isNetworkAvailable()) {
                        Toast.makeText(WelcomeActivity.this, "无法获取有效定位依据导致定位失败，请确认手机是否允许APP获取定位的信息", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(WelcomeActivity.this, "无法获取有效定位依据导致定位失败，" +
                                "一般是由于手机的原因，处于飞行模式下一般会造成这种结果，" +
                                "可以试着重启手机", Toast.LENGTH_LONG).show();
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
                }
            }
        });
    }


    public void post_jpush(String cityName) {
        JSONObject json = new JSONObject();
        try {
            //经度(百度的)
            json.put("lng", lng);
            //纬度(百度的)
            json.put("lat", lat);
            //版本号，传固定参数
            json.put("version", AppUtil.getAppVersion());
            json.put("type", "android");
            json.put("cityName", cityName);
            //设备的信息
            json.put("model", android.os.Build.MODEL);
            json.put("sdk", android.os.Build.VERSION.RELEASE);
            //极光ID
            String registrationID = JPushInterface.getRegistrationID(WelcomeActivity.this.getApplicationContext());
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
                    }

                    @Override
                    public void onResponse(JpushBean response) {
                        if (response.getStatus() == 1) {
                            city = response;
                            ShpfUtil.setValue("lng", city.getLng());
                            ShpfUtil.setValue("lat", city.getLat());
                            ShpfUtil.setValue("city_id", city.getCityId());
                            ShpfUtil.setValue("city_neme", city.getCityName());
                            //tv_city.setText(response.getCityName());
                            post_main();
                        }
                    }
                });
    }


    /**
     * 首页数据
     */
    public void post_main() {
        JSONObject json = new JSONObject();
        try {
            json.put("cityId", city.getCityId());
            json.put("lng", city.getLng());
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
                        Log.e("onError", e.toString(), e);
                    }

                    @Override
                    public void onResponse(MainBean response) {

                        if (response.getStatus() == 1) {
                            //data = response;
                            // adapter.setData(response);
                            response.setCity_name(city.getCityName());
                            AppUtil.indexData = response;
                            AppUtil.city = city;

                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        JPushInterface.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);

    }

}
