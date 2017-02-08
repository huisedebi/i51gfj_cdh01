package com.i51gfj.www.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.activity.LoginActivity;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.model.MineMoneyWrapper;
import com.i51gfj.www.model.TiXianWrapper;
import com.i51gfj.www.model.UserInfo;
import com.i51gfj.www.util.ShpfUtil;
import com.i51gfj.www.util.Util;
import com.i51gfj.www.view.WheelView;
import com.i51gfj.www.view.sortlistview.ClearEditText;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/11.
 */
public class MineTiXianFragment extends BaseFragment {

    EditText name,userAccount,et_code,et_money;
    TextView tv_bank,title_tv,tv_amount;
    List<String> PLANETS;
    Button btn_code;
    String code;
    TiXianWrapper data;
    @Override
    public int getLayout() {
        return R.layout.mine_fragment_tixian;
    }

    @Override
    public void initView(View view) {
        name = (EditText) view.findViewById(R.id.name);
        userAccount = (EditText) view.findViewById(R.id.userAccount);
        tv_bank = (TextView) view.findViewById(R.id.tv_bank);
        tv_bank.setOnClickListener(this);
        title_tv = (TextView) view.findViewById(R.id.title_tv);
        title_tv.setText("零钱提现");
        view.findViewById(R.id.left_tv).setOnClickListener(this);
        tv_amount = (TextView) view.findViewById(R.id.tv_amount);
        view.findViewById(R.id.bt_upload).setOnClickListener(this);
        btn_code = (Button) view.findViewById(R.id.btn_code);
        btn_code.setOnClickListener(this);
        et_code = (EditText) view.findViewById(R.id.et_code);
        et_money = (EditText) view.findViewById(R.id.et_money);

    }

    @Override
    public void initData() {
        post_main();
    }


    /**
     * 首页数据
     *uid    *用户id
     */
    public void post_main() {
        Util.showLoading(getActivity());
        JSONObject json = new JSONObject();
        UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        if(loginData==null){
            Toast.makeText(mActivity, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        try {
            json.put("uid",loginData.getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .postString()
                .url(MyURL.URL_GET_TIXIAN)
                .content(json.toString())
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
                        if (response.getStatus() == 1) {
                            data = response;
                            setValue(response);
                        }
                    }
                });
    }


    /**
     * 验证码数据
     *uid    *用户id
     */
    public void post_main_msm() {
        Util.showLoading(getActivity());
        JSONObject json = new JSONObject();
        UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
        if(loginData==null){
            Toast.makeText(mActivity, "请先登录", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        try {
            json.put("uid",loginData.getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .postString()
                .url(MyURL.URL_USER_SMS)
                .content(json.toString())
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
                        if (response.getStatus() == 1) {
                            code = response.getCode();
                            btn_code.postDelayed(timeRun, 1000);
                            btn_code.setEnabled(false);
                            Toast.makeText(getActivity(), "请求已发送，请耐心等待验证码！", Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMsg = response.getInfo();
                            Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                            onDetach();

                        }
                    }
                });
    }
    private void setValue(final TiXianWrapper response) {
        PLANETS = response.getBankList();
        tv_bank.setOnClickListener(this);
        tv_amount.setText(response.getText());
        userAccount.setText(response.getUserAccount());
        tv_bank.setText(response.getBankName());
        name.setText(response.getName());
        et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et_money.getText().toString().trim().equals("")){
                    tv_amount.setText(response.getText());
                }else if(Double.valueOf(et_money.getText().toString().trim()) <= Double.valueOf(response.getAmount())){
                    NumberFormat nf = NumberFormat.getNumberInstance();
                    nf.setMaximumFractionDigits(2);
                    Double fee = Double.valueOf(et_money.getText().toString().trim())*response.getFee();
                    String temp = response.getText1()+ nf.format(fee)+response.getText2()+ (Double.valueOf(et_money.getText().toString().trim())-fee);
                    tv_amount.setText(temp);
                }else{
                    tv_amount.setText("您当前的余额不足！");
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_bank:
            View outerView = LayoutInflater.from(mActivity).inflate(R.layout.wheel_view, null);
            WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
            wv.setOffset(2);
            wv.setItems(PLANETS);
            wv.setSeletion(3);
            wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    tv_bank.setText(item);
                    userAccount.setText("");
                }
            });

            new AlertDialog.Builder(mActivity)
                    .setTitle("请选择银行")
                    .setView(outerView)
                    .setPositiveButton("确定", null)
                    .show();
            break;
            case R.id.left_tv:
                mActivity.getSupportFragmentManager().popBackStack();
                break;
            case R.id.bt_upload:
                post_upload();
                break;
            case R.id.btn_code:
                String money =  et_money.getText().toString().trim();
                if(money.equals("")){
                    Toast.makeText(getActivity(), "请输入提现金额！", Toast.LENGTH_SHORT).show();
                    return;
                }if(Double.valueOf(money)>Double.valueOf(data.getAccount())){
                Toast.makeText(getActivity(), "您的可提现金额不足！", Toast.LENGTH_SHORT).show();
                return;
            }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("是否向绑定的手机号"+data.getMobile()+"发送验证码吗?");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        post_main_msm();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                break;
        }
    }
 /*   uid                  *用户id
    bankName             *银行名称
    account              *银行帐户
    bankUser             *银行帐户姓名
    amount　　           *提现的金额
*/
    private void post_upload() {
         String bankName =  tv_bank.getText().toString().trim();
        if(bankName.equals("")){
            Toast.makeText(mActivity, "请选择银行！", Toast.LENGTH_SHORT).show();
            return;
        }
         String account =  userAccount.getText().toString().trim();
        if(account.equals("")){
            Toast.makeText(mActivity, "请选择银行账户！", Toast.LENGTH_SHORT).show();
            return;
        }
         String bankUser =  name.getText().toString().trim();
        if(bankUser.equals("")){
            Toast.makeText(mActivity, "请选择银行帐户姓名！", Toast.LENGTH_SHORT).show();
            return;
        }
         String send_code =  et_code.getText().toString().trim();
        if(!send_code.equals(code)){
            Toast.makeText(mActivity, "输入的验证码不正确！", Toast.LENGTH_SHORT).show();
            return;
        }
        String money =  et_money.getText().toString().trim();
        if(money.equals("")){
            Toast.makeText(getActivity(), "请输入提现金额！", Toast.LENGTH_SHORT).show();
            return;
        }if(Double.valueOf(money)>Double.valueOf(data.getAccount())){
            Toast.makeText(getActivity(), "您的可提现金额不足！", Toast.LENGTH_SHORT).show();
            return;
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
            json.put("bankName",bankName);
            json.put("account",account);
            json.put("bankUser",bankUser);
            json.put("amount",money);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .postString()
                .url(MyURL.URL_DRWA_SUBMIT)
                .content(json.toString())
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
                        if (response.getStatus() == 1) {
                            getActivity().finish();
                        }
                        Toast.makeText(getActivity(),response.getInfo() , Toast.LENGTH_SHORT).show();
                    }
                });

    }



    /**
     * 按钮倒计时
     */

    private int time = 60;
    Runnable timeRun = new Runnable() {

        @Override
        public void run() {
            --time;
            btn_code.setText(time + "秒后再次发送");

            if (time <= 0) {
                btn_code.setEnabled(true);
                btn_code.setText("获取短信验证码");
                time = 60;
                return;
            }
            btn_code.postDelayed(timeRun, 1000);
        }
    };
}
