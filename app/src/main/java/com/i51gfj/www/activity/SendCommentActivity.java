package com.i51gfj.www.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.adapter.ShopDetailCommentAdapter;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.fragment.BaseFragment;
import com.i51gfj.www.model.ShopDetailBean;
import com.i51gfj.www.model.ShopDetailCommentWrapper;
import com.i51gfj.www.model.UserInfo;
import com.i51gfj.www.util.ShpfUtil;
import com.i51gfj.www.view.FullyLinearLayoutManager;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/11.
 */
public class SendCommentActivity extends FragmentActivity implements View.OnClickListener{
   ImageView start_1,start_2,start_3,start_4,start_5;
    ImageView imgList[];
    EditText et_content;
    int start = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_comment);
        initView();
    }

    private void initView() {
       TextView title = (TextView) findViewById(R.id.title_tv);
        title.setText("发送点评");
        findViewById(R.id.left_tv).setOnClickListener(this);
        findViewById(R.id.bt_upload).setOnClickListener(this);
        start_1 = (ImageView) findViewById(R.id.start_1);
        start_2 = (ImageView) findViewById(R.id.start_2);
        start_3 = (ImageView) findViewById(R.id.start_3);
        start_4 = (ImageView) findViewById(R.id.start_4);
        start_5 = (ImageView) findViewById(R.id.start_5);
        et_content = (EditText) findViewById(R.id.et_content);
        et_content.setOnClickListener(this);
        imgList = new ImageView[]{ start_1,start_2,start_3,start_4,start_5};
        TextView name = (TextView) findViewById(R.id.tv_name);
        name.setText(getIntent().getStringExtra("name"));

        setOnclik();
    }
/*
    id         *店铺id
    uid        *用户id
            content    //点评内容
    point      *点评星数*/

    public void post_main() {
        UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        if (loginData == null) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("id",getIntent().getStringExtra("id"));
            json.put("uid",loginData.getUid());
            json.put("content",et_content.getText().toString().trim());
            json.put("point",start);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .postString()
                .url(MyURL.URL_UPLOAD_COMMENT)
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
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                    }

                    @Override
                    public void onResponse(ShopDetailBean response) {
                        Toast.makeText(SendCommentActivity.this, response.getInfo(), Toast.LENGTH_SHORT).show();
                        if(response.getStatus()==1){
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                });
    }

    private void setOnclik() {
        for(int i=0;i<5;i++){
            final int finalI = i;
            imgList[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    start = finalI+1;
                        for(int j=0;j<5;j++){
                            if(j<=finalI){
                                imgList[j].setImageResource(R.drawable.fullstart);
                            }else{
                                imgList[j].setImageResource(R.drawable.halfstart);
                            }

                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_tv:
                finish();
                break;
            case R.id.bt_upload:
                post_main();
                break;
            case R.id.et_content:
                et_content.setCursorVisible(true);
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
