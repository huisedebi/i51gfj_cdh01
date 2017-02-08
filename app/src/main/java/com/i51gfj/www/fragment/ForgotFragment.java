package com.i51gfj.www.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.activity.LoginActivity;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.dialog.LoadingDialog;
import com.i51gfj.www.impl.MyCallBack;
import com.i51gfj.www.model.CodeWrapper;
import com.i51gfj.www.model.EntityWrapper;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;


/**
 * 忘记密码
 */
public class ForgotFragment extends BaseFragment implements TextWatcher, View.OnClickListener{

    private EditText et_phone,et_password,sure_et_password;
    private EditText et_code;
    private Button btn_code;
    private HashMap<String, String> params = new HashMap<>();
    private String code;
    private MyCallBack anInterface;
    private LoadingDialog mloadingDialog;
    /**
     * 得到布局
     */
    @Override
    public int getLayout() {
        return R.layout.fragment_forgot;
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    @Override
    public void initView(View view) {
        view.findViewById(R.id.left_tv).setOnClickListener(this);
        et_phone = (EditText) view.findViewById(R.id.et_phone);
        et_code = (EditText) view.findViewById(R.id.et_code);
        sure_et_password = (EditText) view.findViewById(R.id.sure_et_password);
        et_password = (EditText) view.findViewById(R.id.et_password);
        btn_code = (Button) view.findViewById(R.id.btn_code);
        et_phone.addTextChangedListener(this);
        btn_code.setOnClickListener(this);
        view.findViewById(R.id.btn_change).setOnClickListener(this);
        TextView title_tv = (TextView) view.findViewById(R.id.title_tv);
        title_tv.setText("忘记密码");
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        //mloadingDialog = Util.getLoadingDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        et_phone.setText("");
        et_code.setText("");
        code = "";
    }

    public void setBaseInterface(MyCallBack anInterface){
        this.anInterface = anInterface;
    }

    /**
     * 验证
     */
    public void validate() {
        if (!AppUtil.isPhone(et_phone.getText().toString().trim()))
        {
            Toast.makeText(getActivity(), "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!code.equals(et_code.getText().toString().trim()) || code.equals("")) {
            Toast.makeText(getActivity(), "验证码不正确！", Toast.LENGTH_SHORT).show();
            return;
        }
        LoginActivity activity = (LoginActivity) getActivity();
        activity.setPhone(et_phone.getText().toString().trim());
        anInterface.onCallBack(2);

    }

    /**
     * 获取验证码
     */
    public void post_get_code() {
       // Util.showMyLoadingDialog(mloadingDialog, getActivity());
        params.clear();
        if(!AppUtil.isPhone(et_phone.getText().toString().trim())){
            Toast.makeText(getActivity(), "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
            return;
        }
        params.put("userName", et_phone.getText().toString().trim());
        OkHttpUtils.postString()
                .url(MyURL.URL_FORGET_REG)
                .content(new Gson().toJson(params))
                .build()
                .execute(new Callback<CodeWrapper>() {
                    @Override
                    public CodeWrapper parseNetworkResponse(Response response) throws IOException {
                        return new Gson().fromJson(response.body().string(), CodeWrapper.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                    }

                    @Override
                    public void onResponse(CodeWrapper response) {
                        //Util.dissMyLoadingDialog(mloadingDialog);
                        if (response.getStatus() == 1) {
                            code = response.getCode();
                            btn_code.postDelayed(timeRun, 1000);
                            btn_code.setEnabled(false);
                            Toast.makeText(getActivity(), "请求已发送，请耐心等待验证码！", Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMsg = response.getInfo();
                            Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 按钮倒计时
     */
    private int time = 30;
    Runnable timeRun = new Runnable() {

        @Override
        public void run() {
            --time;
            btn_code.setText(time + "秒后再次发送");

            if (time <= 0) {
                btn_code.setEnabled(true);
                btn_code.setText("获取短信验证码");
                time = 30;
                return;
            }
            btn_code.postDelayed(timeRun, 1000);
        }
    };

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
            case R.id.btn_change:
                post_change();
                break;
        }
    }
/*//userName    用户名
  userPwd     用户密码   客户端普通md5加密后传上来*/
    private void post_change() {
        String phone = et_phone.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String sure_password = sure_et_password.getText().toString().trim();
        String code = et_code.getText().toString().trim();
        if (!AppUtil.isPhone(phone)) {
            Toast.makeText(getActivity(), "请输入正确的手机号喔！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(sure_password)){
            Toast.makeText(getActivity(), "二次密码不一致！", Toast.LENGTH_SHORT).show();
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
        //Util.showMyLoadingDialog(mloadingDialog,getActivity());
        OkHttpUtils
                .postString()
                .url(MyURL.URL_FORGET)
                .content(new Gson().toJson(params))
                .build()
                .execute(new Callback<EntityWrapper>() {
                    @Override
                    public EntityWrapper parseNetworkResponse(Response response) throws IOException {
                        return new Gson().fromJson(response.body().string(), EntityWrapper.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        //Util.dissMyLoadingDialog(mloadingDialog);
                    }

                    @Override
                    public void onResponse(EntityWrapper response) {
                        // Util.dissMyLoadingDialog(mloadingDialog);
                        String msg;
                        if (response.getStatus() == 1) {
                            msg = response.getInfo();
                            LoginActivity activity = (LoginActivity) getActivity();
                            activity.setPhone(et_phone.getText().toString().trim());
                            activity.setPassword(et_password.getText().toString().trim());
                           // anInterface.onCallBack(1);
                        } else {
                            msg = response.getInfo();
                        }
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
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
}
