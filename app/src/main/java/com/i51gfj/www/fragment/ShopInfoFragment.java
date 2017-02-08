package com.i51gfj.www.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.activity.LoginActivity;
import com.i51gfj.www.activity.MainActivity;
import com.i51gfj.www.activity.MineMidActivity;
import com.i51gfj.www.activity.PhotoPickerActivity;
import com.i51gfj.www.adapter.PickPhotoAdapterForShopInfo;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.model.ShopInfoBean;
import com.i51gfj.www.model.UserInfo;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.ShpfUtil;
import com.i51gfj.www.util.Util;
import com.i51gfj.www.view.WheelView;
import com.i51gfj.www.view.map.LocationActivtiy;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/11.
 */
public class ShopInfoFragment extends BaseFragment {
    ImageView img_store_one;
    private boolean isMultiSelect;
    Button bt_upload;
    EditText et_store_name, et_phone, et_conten, et_detail_where, et_jianjie;
    TextView tv_where, et_type;
    RecyclerView recyclerView;
    private PickPhotoAdapterForShopInfo adapter;
    private ShopInfoBean data;
    private String select_lat;
    private String select_lon;
    Handler myHandler;
    Boolean can_upload = true;

    @Override
    public int getLayout() {
        return R.layout.mine_fragment_shop_info;
    }

    @Override
    public void initView(View view) {
        Util.showLoading(getActivity());
        TextView title = (TextView) view.findViewById(R.id.title_tv);
        title.setText("商户信息");
        view.findViewById(R.id.left_tv).setOnClickListener(this);
        bt_upload = (Button) view.findViewById(R.id.bt_upload);
        bt_upload.setOnClickListener(this);
        et_store_name = (EditText) view.findViewById(R.id.et_store_name);
        et_store_name.setOnClickListener(this);
        et_phone = (EditText) view.findViewById(R.id.et_phone);
        et_detail_where = (EditText) view.findViewById(R.id.et_detail_where);
        et_conten = (EditText) view.findViewById(R.id.et_conten);
        et_jianjie = (EditText) view.findViewById(R.id.et_jianjie);
        et_type = (TextView) view.findViewById(R.id.et_type);
        et_type.setOnClickListener(this);
        tv_where = (TextView) view.findViewById(R.id.tv_where);
        img_store_one = (ImageView) view.findViewById(R.id.img_store_one);
        img_store_one.setOnClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        view.findViewById(R.id.layout_address).setOnClickListener(this);

        myHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 110:
                        String json = msg.getData().getString("json");
                        post_json(json);
                        break;
                }
            }
        };
    }

    @Override
    public void initData() {

        post_main();
    }

    public void post_main() {

        UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        if (loginData == null) {
            Toast.makeText(mActivity, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("uid", loginData.getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .postString()
                .url(MyURL.URL_MINE_SHOP_INFO)
                .content(json.toString())
                .build()
                .execute(new Callback<ShopInfoBean>() {
                    @Override
                    public ShopInfoBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, ShopInfoBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                        Util.closeLoading();
                    }

                    @Override
                    public void onResponse(ShopInfoBean response) {
                        if (response.getStatus() == 1) {
                            data = response;
                            setValue(response);
                        }
                        Util.closeLoading();
                    }
                });
    }

    private void setValue(ShopInfoBean response) {
        et_store_name.setText(response.getData().getName());
        et_phone.setText(response.getData().getTel());
        et_type.setText(response.getData().getCidStr());
        et_conten.setText(response.getData().getManageArea());
        tv_where.setText(response.getData().getApi_address());
        et_detail_where.setText(response.getData().getAddress());
        mImageLoader.displayImage(response.getData().getImg(), img_store_one, options);
        et_jianjie.setText(response.getData().getManageArea());
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 5));
        adapter = new PickPhotoAdapterForShopInfo(mActivity, ShopInfoFragment.this);
        recyclerView.setAdapter(adapter);
        if (response.getImgList() != null) {
            List<String> data = new ArrayList<>();
            for (int i = 0; i < response.getImgList().size(); i++) {
                data.add(response.getImgList().get(i).getImg());
            }
            adapter.setData(data);
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), PhotoPickerActivity.class);
        switch (v.getId()) {
            case R.id.img_store_one://单选
                if (mActivity instanceof MineMidActivity) {
                    MineMidActivity activity = (MineMidActivity) mActivity;
                    activity.isMultiSelect = false;
                    getActivity().startActivityForResult(intent, 2916);
                }
                break;
            case R.id.bt_upload:
                if(can_upload){
                    can_upload = false;
                    upload();
                }
                break;
            case R.id.left_tv:
                getActivity().finish();
                break;
            case R.id.et_type:
                final List<String> strins = new ArrayList<>();
                for (int i = 0; i < data.getCate().size(); i++) {
                    strins.add(data.getCate().get(i).getName());
                }
                View outerView = LayoutInflater.from(mActivity).inflate(R.layout.wheel_view, null);
                WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
                wv.setOffset(2);
                wv.setItems(strins);
                wv.setSeletion(3);
                wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        et_type.setText(item);
                    }
                });

                new AlertDialog.Builder(mActivity)
                        .setTitle("请选择")
                        .setView(outerView)
                        .setPositiveButton("确定", null)
                        .show();
                break;
            case R.id.layout_address:
                Intent intent1 = new Intent(getActivity(), LocationActivtiy.class);
                getActivity().startActivityForResult(intent1, 101);
                break;

        }

    }

    //提交
   /* uid          *用户id
    sid          *店铺　id
    cid          *店铺类型id   要必选，不选择的提示让他选
    name         //店铺名称
    tel          //电话
    manageArea   //经营范围
    api_address  //定位地址
    address      商家填写的地址   如果需要更改定位点旁边的箭头
    lng          //
    lat          //
    intro        //店铺简介
    img          //图片base64
    imgList      *图片列表base64位传上来*/
    private void upload() {
        UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        int cid = Cid();
        String tel = et_phone.getText().toString().trim();
        if (!AppUtil.isPhone(tel)) {
            Toast.makeText(getActivity(), "请输入正确的手机号喔！", Toast.LENGTH_SHORT).show();
            can_upload =true;
            return;
        }
        if (et_jianjie.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "请输入经营范围！", Toast.LENGTH_SHORT).show();
            can_upload =true;
            return;
        }
        if (et_detail_where.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "请输入详细地址！", Toast.LENGTH_SHORT).show();
            can_upload =true;
            return;
        }
        final String img =data.getData().getImg().startsWith("http")?"":Util.bitmapToBase64(Util.getBitmap(data.getData().getImg()));

        Util.showLoadingUpload(getActivity());
        final JSONObject json = new JSONObject();
        try {
                   json.put("uid", loginData.getUid());
                   json.put("sid", data.getData().getId());
                   json.put("cid", cid);
                   json.put("name", et_store_name.getText().toString().trim());
                   json.put("tel", tel);
                   json.put("manageArea", et_jianjie.getText().toString().trim());
                   json.put("api_address", tv_where.getText().toString().trim());
                   json.put("address", et_detail_where.getText().toString().trim());
                   json.put("lng", select_lon == null ? ShpfUtil.getStringValue("lng") : select_lon);
                   json.put("lat", select_lat == null ? ShpfUtil.getStringValue("lat") : select_lat);
                  // json.put("intro", et_jianjie.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
           @Override
           public void run() {
               String jsonString = null;
               JSONArray  listArry = new JSONArray();
               JSONArray  listUrlArry = new JSONArray();
               for (String imgurl : adapter.data) {
                   JSONObject json1 = new JSONObject();
                   try {
                       if(imgurl.startsWith("http")){
                           json1.put("img", imgurl);
                           //imgUrls.add(json1.toString());
                           listUrlArry.put(json1);
                       }else{
                           json1.put("img", Util.bitmapToBase64(Util.getBitmap(imgurl)));
                           //imglists.add(json1.toString());
                           listArry.put(json1);
                       }
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }
               try {

                   json.put("img", img);
                   json.put("imgList", listArry);
                   json.put("imgUrl", listUrlArry);
                   jsonString = json.toString();
               } catch (JSONException e) {
                   e.printStackTrace();
               }
               Log.i("Imglisetjson", "upload:" + jsonString);
               Message message = new Message();
               message.what = 110;
               Bundle bundle = new Bundle();
               bundle.putString("json",jsonString);
               message.setData(bundle);
               myHandler.sendMessage(message);
           }
       }).start();

    }
    void post_json(String json){
        OkHttpUtils
                .postString()
                .url(MyURL.URL_STORE_SAVE)
                .content(json)
                .build()
                .execute(new Callback<ShopInfoBean>() {
                    @Override
                    public ShopInfoBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, ShopInfoBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                        Util.closeLoading();
                    }

                    @Override
                    public void onResponse(ShopInfoBean response) {
                        can_upload =true;
                        Util.closeLoading();
                        if (response.getStatus() == 1) {
                            final android.app.AlertDialog.Builder alterDialog = new android.app.AlertDialog.Builder(getActivity());
                            alterDialog.setMessage("保存成功！");
                            alterDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            alterDialog.show();
                        }
                    }
                });
    }


    private int Cid() {
        for (int i = 0; i < data.getCate().size(); i++) {
            if (data.getCate().get(i).getName().equals(et_type.getText().toString().trim())) {
                return data.getCate().get(i).getId();
            }
        }
        return 0;
    }

    public void changeImg(String imgresult) {
        imageLoader.displayImage("file://" + imgresult, img_store_one, options);
        data.getData().setImg(imgresult);
    }

    //选择图片列表
    @Override
    public void onMyItemClick(Object data) {
        Intent intent = new Intent(getActivity(), PhotoPickerActivity.class);
        if (mActivity instanceof MineMidActivity) {
            MineMidActivity activity = (MineMidActivity) mActivity;
            activity.isMultiSelect = true;
            Bundle bundle = new Bundle();
            bundle.putBoolean(PhotoPickerActivity.IS_MULTI_SELECT, true);//设置是否支持多选
            bundle.putInt(PhotoPickerActivity.MAX_SELECT_SIZE, (Integer) data);//设置最大选择数量
            intent.putExtras(bundle);
            getActivity().startActivityForResult(intent, 3001);
        }
    }

    public void changeAdapte(List<String> data) {
        adapter.setData(data);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == 102) {
            if (data != null) {
                tv_where.setText(data.getStringExtra("address"));
                select_lat = data.getStringExtra("latitude");
                select_lon = data.getStringExtra("longitude");
            }
        }
    }



}