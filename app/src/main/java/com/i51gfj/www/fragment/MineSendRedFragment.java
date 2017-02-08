package com.i51gfj.www.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.activity.BDpoiActivity;
import com.i51gfj.www.activity.LoginActivity;
import com.i51gfj.www.activity.MainActivity;
import com.i51gfj.www.activity.MineMidActivity;
import com.i51gfj.www.activity.PhotoPickerActivity;
import com.i51gfj.www.adapter.PickPhotoAdapter;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.model.MyCountBean;
import com.i51gfj.www.model.SendRedWrapper;
import com.i51gfj.www.model.TiXianWrapper;
import com.i51gfj.www.model.UserInfo;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.ShpfUtil;
import com.i51gfj.www.util.Util;
import com.i51gfj.www.view.map.LocationActivtiy;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/11.
 */
public class MineSendRedFragment extends BaseFragment {

    TextView title_tv,tv_where;

    RecyclerView recyclerView;
    PickPhotoAdapter adapter;
    EditText et_money,et_count,et_content,et_red_content,et_phone;
    TextView tv_totle;
    private String select_lat;
    private String select_lon;
    private SendRedWrapper data;
    Button bt_upload;
    Handler myHandler;
    Boolean is_mainactivity = false;
    Boolean can_upload = true;


    @Override
    public int getLayout() {
        return R.layout.mine_fragment_send_red;

    }

    @Override
    public void initView(View view) {
        if(getActivity() instanceof MainActivity){
            view.findViewById(R.id.left_tv).setVisibility(View.GONE);
            view.findViewById(R.id.left_tv2).setVisibility(View.VISIBLE);
            is_mainactivity = true;
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 5));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top=2;

            }
        });
        adapter = new PickPhotoAdapter(mActivity,MineSendRedFragment.this);
        recyclerView.setAdapter(adapter);
        title_tv = (TextView) view.findViewById(R.id.title_tv);
        title_tv.setText("推广红包");
        tv_where = (TextView) view.findViewById(R.id.tv_where);
        view.findViewById(R.id.left_tv).setOnClickListener(this);
        view.findViewById(R.id.layout_map).setOnClickListener(this);
        et_money= (EditText) view.findViewById(R.id.et_money);
        et_count= (EditText) view.findViewById(R.id.et_count);
        et_content= (EditText) view.findViewById(R.id.et_content);
        et_phone= (EditText) view.findViewById(R.id.et_phone);
        et_red_content= (EditText) view.findViewById(R.id.et_red_content);
        view.findViewById(R.id.bt_upload).setOnClickListener(this);
        myHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 110:
                        String json = msg.getData().getString("json");
                       upload(json);
                        break;
                }
            }
        };

        et_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setTotle();
            }
        });
        et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setTotle();
            }
        });
        tv_totle = (TextView) view.findViewById(R.id.tv_totle);
    }
    @Override
    public void initData() {
        post_main();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_tv:
                mActivity.finish();
                break;
            case R.id.bt_upload:
                if(can_upload){
                    can_upload =false;
                    cheak_upload();
                }
                break;
            case R.id.layout_map:
              //getActivity().startActivity(new Intent(getActivity(), BDpoiActivity.class));
                Intent intent1 = new Intent(getActivity(), LocationActivtiy.class);
                getActivity().startActivityForResult(intent1, 105);
                break;
        }
    }
   public void setTotle(){
       if(!et_money.getText().toString().trim().isEmpty() && !et_count.getText().toString().trim().isEmpty()){
           tv_totle.setText("￥"+Double.valueOf(et_money.getText().toString().trim()) * Double.valueOf(et_count.getText().toString().trim()));
       }
   }

    @Override
    public void onMyItemClick(Object data) {
        Intent intent = new Intent(getActivity(), PhotoPickerActivity.class);
        if(mActivity instanceof MineMidActivity){
            MineMidActivity activity = (MineMidActivity) mActivity;
            activity.isMultiSelect = true;
            Bundle bundle = new Bundle();
            bundle.putBoolean(PhotoPickerActivity.IS_MULTI_SELECT, true);//设置是否支持多选
            bundle.putInt(PhotoPickerActivity.MAX_SELECT_SIZE, (Integer) data);//设置最大选择数量
            intent.putExtras(bundle);
            getActivity().startActivityForResult(intent, 3000);
        }else{
            Bundle bundle = new Bundle();
            bundle.putBoolean(PhotoPickerActivity.IS_MULTI_SELECT, true);//设置是否支持多选
            bundle.putInt(PhotoPickerActivity.MAX_SELECT_SIZE, (Integer) data);//设置最大选择数量
            intent.putExtras(bundle);
            getActivity().startActivityForResult(intent, 3000);
        }
    }

    public void changeAdapte(List<String >data ){
        adapter.setData(data);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==105 && resultCode == 102){
            if(data !=null){
                DecimalFormat df=new DecimalFormat(".######");
                tv_where.setText(data.getStringExtra("address"));
                select_lat =  df.format(data.getDoubleExtra("latitude",0));
                select_lon = df.format(data.getDoubleExtra("longitude",0));
                Log.i("select_lat", "onActivityResult: "+select_lat+"___"+select_lon);
            }
        }
    }


   /* uid        *用户id
    lng        *经纬度    请求红包时获取到的经纬度
    lat        *经纬度    请求红包时获取到的经纬度
    sid        *请求红包时获取到的值
    content    *说什么里的内容
    title      *活动信息里的内容
    address　　*请求红包时获取到的值
    amount　　 *红包的单价
    quantity   *红包的数量
    avgPoint   *请求红包时获取到的值
    collect    *请求红包时获取到的值
    remark　　 *备注
    imgList    *图片列表base64位传上来*/
    public void cheak_upload() {
        final JSONObject json = new JSONObject();
        final UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        if(loginData==null){
            Toast.makeText(mActivity, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mActivity, LoginActivity.class));
            can_upload =true;
            return;
        }
          String totle = tv_totle.getText().toString().trim().substring(1);
        if( totle==null || totle.equals("0") || totle.equals("0.0")){
            Toast.makeText(mActivity, "请输入正确的金额和红包个数！", Toast.LENGTH_SHORT).show();
            can_upload =true;
            return;
        }
          final String where = tv_where.getText().toString().trim();
        Log.i("where", "totle: "+totle);
        Log.i("where", "cheak_upload: "+where.equals(""));
        if(where.equals("请选择活动地址")){
            Toast.makeText(mActivity, "请选择红包地址！", Toast.LENGTH_SHORT).show();
            can_upload =true;
            return;
        }
        Util.showLoadingUpload(getActivity());
         final String content = et_content.getText().toString().trim();
        final JSONArray jsonArray = new JSONArray();
        if(adapter.data!=null){
            for(int i=0;i<adapter.data.size();i++){
                JSONObject j = new JSONObject();
                try {
                    j.put("img",Util.bitmapToBase64(Util.getBitmap(adapter.data.get(i))));
                    jsonArray.put(j);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    json.put("uid", loginData.getUid());
                    json.put("lng", (select_lon == null ||select_lon.equals("0")) ? data.getLng(): select_lon);
                    json.put("lat", (select_lat == null ||select_lat.equals("0"))? data.getLat(): select_lat);
                    json.put("sid",data.getSid());
                    json.put("content",content);
                    json.put("title",loginData.getUid());
                    json.put("address",where);
                    json.put("amount",et_money.getText().toString().trim());
                    json.put("quantity",et_count.getText().toString().trim());
                    json.put("phone",et_phone.getText().toString().trim());
                    json.put("avgPoint",data.getAvgPoint());
                    json.put("collect",data.getCollect());
                    json.put("remark", et_red_content.getText().toString().trim());
                    json.put("imgList", jsonArray);
                    String jsonString = json.toString();
                    Log.i("jsonString", "run: "+jsonString);
                    Message message = new Message();
                    message.what = 110;
                    Bundle bundle = new Bundle();
                    bundle.putString("json", jsonString);
                    message.setData(bundle);
                    myHandler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
      }


    public void upload(String jsonString ) {
        OkHttpUtils
                .postString()
                .url(MyURL.URL_UPLOAD_RED)
                .content(jsonString)
                .build()
                .execute(new Callback<TiXianWrapper>() {
                    @Override
                    public TiXianWrapper parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, TiXianWrapper.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                        Util.closeLoading();
                    }

                    @Override
                    public void onResponse(TiXianWrapper response) {
                        Util.closeLoading();
                        can_upload =true;
                        //Toast.makeText(mActivity, "发布成功！", Toast.LENGTH_SHORT).show();
                        if (response.getStatus() == 1) {
                            final android.app.AlertDialog.Builder alterDialog = new android.app.AlertDialog.Builder(getActivity());
                            alterDialog.setMessage("发布成功！");
                            alterDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            alterDialog.show();
                            AppUtil.is_frash =true;
                            if(getActivity() instanceof MineMidActivity){
                                getActivity().finish();
                            }else{
                                removeData();
                            }
                        }else{
                            Toast.makeText(mActivity, "服务器繁忙！", Toast.LENGTH_SHORT).show();
                            removeData();
                        }
                    }
                });
    }


    private void removeData() {
        et_content.setText(null);
        adapter.removeData();
        et_money.setText(null);
        et_count.setText(null);
        tv_where.setText("请选择活动地址");
        et_red_content.setText(null);
        select_lat=null;
        select_lon=null;
        tv_totle.setText("￥0");
    }

    /*  uid        *用户id
      lng        *经纬度    极光请求里的经纬度
      lat        *经纬度    极光请求里的经纬度*/
    public void post_main() {
        if(!is_mainactivity){
            Util.showLoading(getActivity());
        }
        JSONObject json = new JSONObject();
        UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        if(loginData==null){
            Toast.makeText(mActivity, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        try {
            json.put("uid",loginData.getUid());
            json.put("lng",  ShpfUtil.getStringValue("lng") );
            json.put("lat",  ShpfUtil.getStringValue("lat"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .postString()
                .url(MyURL.URL_WANG_RED)
                .content(json.toString())
                .build()
                .execute(new Callback<SendRedWrapper>() {
                    @Override
                    public SendRedWrapper parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, SendRedWrapper.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                        if(!is_mainactivity){
                            Util.closeLoading();
                        }
                    }

                    @Override
                    public void onResponse(SendRedWrapper response) {
                        if(!is_mainactivity){
                            Util.closeLoading();
                        }
                        if (response.getStatus() == 1) {
                            data = response;
                        }
                    }
                });
    }



}
