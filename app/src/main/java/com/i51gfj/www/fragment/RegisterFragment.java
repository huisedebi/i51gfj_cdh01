
package com.i51gfj.www.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.i51gfj.www.R;
import com.i51gfj.www.activity.LoginActivity;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.impl.MyCallBack;
import com.i51gfj.www.model.CodeWrapper;
import com.i51gfj.www.model.EntityWrapper;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;


/**
 * 注册账号
 */

public class RegisterFragment extends BaseFragment implements TextWatcher, View.OnClickListener {

    private EditText et_phone;
    private EditText et_password;
    private EditText et_code;
    private Button btn_code;    //获取验证码按钮
    private String code;    //验证码
    private HashMap<String, String> params = new HashMap<>();
    private MyCallBack anInterface;

/**
     * 初始化控件
     */

    @Override
    public void initView(View view) {
        et_phone = (EditText) view.findViewById(R.id.et_phone);
        et_password = (EditText) view.findViewById(R.id.et_password);
        et_code = (EditText) view.findViewById(R.id.et_code);
        btn_code = (Button) view.findViewById(R.id.btn_code);
        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        et_phone.addTextChangedListener(this);
        btn_code.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        view.findViewById(R.id.left_tv).setOnClickListener(this);
        TextView tv_title = (TextView) view.findViewById(R.id.title_tv);
        tv_title.setText("注册");
    }

    public void setBaseInterface(MyCallBack anInterface){
        this.anInterface = anInterface;
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


/**
     * 获取验证码
     */

    public void post_get_code() {
        JSONObject json = new JSONObject();
        if(!AppUtil.isPhone(et_phone.getText().toString().trim())){
            Toast.makeText(getActivity(), "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            json.put("userName", et_phone.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Util.showLoading(getActivity());
        OkHttpUtils.postString()
                .url(MyURL.URL_REG)
                .content(json.toString())
                .build()
                .execute(new Callback<CodeWrapper>() {
                    @Override
                    public CodeWrapper parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, CodeWrapper.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                       Util.closeLoading();
                    }

                    @Override
                    public void onResponse(CodeWrapper response) {
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


/**
     * 注册账号
     */

    public void post_register() {
        Util.showLoading(getActivity());
        String phone = et_phone.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String code = et_code.getText().toString().trim();
        if (!AppUtil.isPhone(phone)) {
            Toast.makeText(getActivity(), "请输入正确的手机号喔！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length() < 6){
            Toast.makeText(getActivity(), "密码最少6位喔！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!code.equals(this.code)) {
            Toast.makeText(getActivity(), "验证码不正确喔！", Toast.LENGTH_SHORT).show();
            return;
        }
        params.clear();
        params.put("userName", phone);
        params.put("userPwd", AppUtil.getMD5(password));
        OkHttpUtils
                .postString()
                .url(MyURL.URL_REGISTER)
                .content(new Gson().toJson(params))
                .build()
                .execute(new Callback<EntityWrapper>() {
                    @Override
                    public EntityWrapper parseNetworkResponse(Response response) throws IOException {
                        return new Gson().fromJson(response.body().string(), EntityWrapper.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Util.closeLoading();
                    }

                    @Override
                    public void onResponse(EntityWrapper response) {
                        Util.closeLoading();
                        String msg;
                        if (response.getStatus() == 1) {
                            msg = response.getInfo();
                            LoginActivity activity = (LoginActivity) getActivity();
                            activity.setPhone(et_phone.getText().toString().trim());
                            activity.setPassword(et_password.getText().toString().trim());
                            getActivity().finish();
                        } else {
                            msg = response.getInfo();
                        }
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (AppUtil.isPhone(et_phone.getText().toString().trim())) {
            btn_code.setEnabled(true);
        } else {
            btn_code.setEnabled(false);
        }
    }


/**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_tv:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btn_code:
                post_get_code();
                break;
            case R.id.btn_register:
                post_register();
                break;
        }

    }

/**
     * 得到布局
     */

    @Override
    public int getLayout() {
        return R.layout.fragment_register;
    }




/**
     * 初始化数据
     */

    @Override
    public void initData() {

    }
}

