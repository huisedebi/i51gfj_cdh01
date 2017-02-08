package com.i51gfj.www.fragment;


import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.impl.MyItemClickListener;
import com.i51gfj.www.model.ShopDetailBean;
import com.i51gfj.www.util.ShpfUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/11.  弃用。。
 */
public class ShopDetailFragment extends BaseFragment implements MyItemClickListener{
    public String store_id;
    private ShopDetailBean data;



    @Override
    public int getLayout() {
        return R.layout.fragment_shopdetail;
    }

    @Override
    public void initView(View view) {

        view.findViewById(R.id.left_tv).setOnClickListener(this);
        TextView tv_title = (TextView) view.findViewById(R.id.title_tv);
        tv_title.setText("详情");
    }

    @Override
    public void initData() {
        store_id = getArguments().getString("passData");
        post_main();
    }

    private void post_main() {
        JSONObject json = new JSONObject();
        try {
            json.put("id",store_id);
            json.put("lng", ShpfUtil.getStringValue("lng"));
            json.put("lat",ShpfUtil.getStringValue("lat"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Util.showMyLoadingDialog(mloadingDialog,getActivity());
        OkHttpUtils
                .postString()
                .url(MyURL.URL_SHOP_DETAIL)
                .content(json.toString())
                .build()
                .execute(new Callback<ShopDetailBean>() {
                    @Override
                    public ShopDetailBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, ShopDetailBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        //                       Util.dissMyLoadingDialog(mloadingDialog);
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);

                    }

                    @Override
                    public void onResponse(ShopDetailBean response) {
                        if (response.getStatus() == 1) {
                            data = response;
                            //adapter.setData(response);

                        }
                    }

                });
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.left_tv:
               getActivity().getSupportFragmentManager().popBackStack();
       }
    }
}
