package com.i51gfj.www.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.model.UserInfo;
import com.i51gfj.www.model.UserInfoForMinFragementBean;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.Logger;
import com.i51gfj.www.util.ShpfUtil;
import com.i51gfj.www.view.WheelView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/11.
 */
public class UpdataFragment extends BaseFragment {

    TextView title_tv;
    TextView ic_back,tv_sex;
    EditText et_phone,et_name,et_re_name;
    Button bt_save;
    private static final String[] PLANETS = new String[]{"男", "女"};


    @Override
    public int getLayout() {
        return R.layout.fragment_updata;
    }

    @Override
    public void initView(View view) {
        title_tv = (TextView) view.findViewById(R.id.title_tv);
        title_tv.setText("编辑资料");
        ic_back = (TextView) view.findViewById(R.id.left_tv);
        ic_back.setOnClickListener(this);
        et_phone = (EditText) view.findViewById(R.id.et_phone);
        tv_sex = (TextView) view.findViewById(R.id.tv_sex);
        tv_sex.setOnClickListener(this);
        et_name = (EditText) view.findViewById(R.id.et_name);
        et_re_name = (EditText) view.findViewById(R.id.et_re_name);
        view.findViewById(R.id.bt_save).setOnClickListener(this);
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
            json.put("uid",loginData.getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Util.showMyLoadingDialog(mloadingDialog,getActivity());
        OkHttpUtils
                .postString()
                .url(MyURL.URL_INFORMATION)
                .content(json.toString())
                .build()
                .execute(new Callback<UserInfoForMinFragementBean>() {
                    @Override
                    public UserInfoForMinFragementBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, UserInfoForMinFragementBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                    }

                    @Override
                    public void onResponse(UserInfoForMinFragementBean response) {
                        if (response.getStatus() == 1) {
                            setValue(response);
                        }
                    }
                });
    }
  /*  uid            *用户id
    sex            性别   -1无选中    0为女  1为男
    realName       姓名
    nickName       昵称   //201609新加*/
    public void post_main_save(int sex,String realName,String nickName) {
        UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        if (loginData == null) {
            Toast.makeText(mActivity, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("uid",loginData.getUid());
            json.put("sex",sex);
            json.put("realName",realName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Util.showMyLoadingDialog(mloadingDialog,getActivity());
        OkHttpUtils
                .postString()
                .url(MyURL.URL_INFORMATION_SAVE)
                .content(json.toString())
                .build()
                .execute(new Callback<UserInfoForMinFragementBean>() {
                    @Override
                    public UserInfoForMinFragementBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, UserInfoForMinFragementBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                    }

                    @Override
                    public void onResponse(UserInfoForMinFragementBean response) {
                        if (response.getStatus() == 1) {
                            Toast.makeText(mActivity, "修改成功！", Toast.LENGTH_SHORT).show();
                            post_main();
                            AppUtil.info_frash = true;
                            getActivity().setResult(Activity.RESULT_OK);
                            getActivity().finish();
                        }
                    }
                });
    }

    private void setValue(UserInfoForMinFragementBean response) {
        if(response.getData()!=null){
            tv_sex.setText(response.getData().getSex().equals("1")?"男":"女");
            et_phone.setText(response.getData().getMobile());
            et_name.setText(response.getData().getRealName());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_tv:
             mActivity.finish();
                break;
            case R.id.bt_save:
                saveData();
                break;
            case R.id.tv_sex:
                View outerView = LayoutInflater.from(mActivity).inflate(R.layout.wheel_view, null);
                WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
                wv.setOffset(2);
                wv.setItems(Arrays.asList(PLANETS));
                wv.setSeletion(3);
                wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                    @Override
                    public void onSelected(int selectedIndex, String item) {
                        //Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                        tv_sex.setText(item);
                    }
                });

                new AlertDialog.Builder(mActivity)
                        .setTitle("请选择性别")
                        .setView(outerView)
                        .setPositiveButton("确定", null)
                        .show();
                break;
        }
    }

    private void saveData() {
        String  phone= et_phone.getText().toString();
        if (!AppUtil.isPhone(phone)) {
            Toast.makeText(getActivity(), "请输入正确的手机号喔！", Toast.LENGTH_SHORT).show();
            return;
        }
        String sex = tv_sex.getText().toString();
        if(sex==null || sex.equals("")){
            Toast.makeText(mActivity, "请选择性别！", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = et_name.getText().toString();
        if(name==null || name.equals("")){
            Toast.makeText(mActivity, "请输入姓名！", Toast.LENGTH_SHORT).show();
            return;
        }
        post_main_save(sex.equals("男")?1:0,name,"");
    }
}
