
package com.i51gfj.www.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import com.i51gfj.www.DemoContext;
import com.i51gfj.www.R;
import com.i51gfj.www.activity.LoginActivity;
import com.i51gfj.www.activity.RongyunLoginActivity;
import com.i51gfj.www.activity.RongyunMainActivity;
import com.i51gfj.www.application.MyApplication;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.dialog.LoadingDialog;
import com.i51gfj.www.impl.MyCallBack;
import com.i51gfj.www.model.UserInfo;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.NetUtils;
import com.i51gfj.www.util.ShpfUtil;
import com.i51gfj.www.util.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 登录
 */

public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private HashMap<String, String> params = new HashMap<>();
    private EditText et_phone;
    private EditText et_code;
    private MyCallBack anInterface;
    private Button btn_login;
    private Button btn_register;
    private TextView left_tv;
    private TextView title_tv;
    private TextView right_tv;
    private LoadingDialog mloadingDialog;
    private ImageView back_ic;
    private ImageView remember_code;
    private Boolean tag = false;//是否记录密码下次自动登录
    String token;
/**
     * 得到布局
     */

    @Override
    public int getLayout() {
        return R.layout.fragment_login;
    }


/**
     * 初始化控件
     * @param view 布局对象
     */

    @Override
    public void initView(View view) {
        left_tv = (TextView) view.findViewById(R.id.left_tv);
        left_tv.setOnClickListener(this);
        title_tv = (TextView) view.findViewById(R.id.title_tv);
        right_tv = (TextView) view.findViewById(R.id.right_tv);
        right_tv.setOnClickListener(this);
        et_phone = (EditText) view.findViewById(R.id.et_phone);
        et_code = (EditText) view.findViewById(R.id.et_code);
        btn_login = (Button) view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        remember_code = (ImageView) view.findViewById(R.id.remember_code);
        remember_code.setOnClickListener(this);
        view.findViewById(R.id.lost_password).setOnClickListener(this);

       // back_ic = (ImageView) view.findViewById(R.id.back_ic);

    }


/**
     * 初始化数据
     */

    @Override
    public void initData() {
        title_tv.setText("登录");
        right_tv.setText("注册");
    }

    @Override
    public void onResume() {
        super.onResume();
        LoginActivity activity = (LoginActivity) getActivity();
        if (activity.getPassword() != null) {
            et_phone.setText(activity.getPhone());
            et_code.setText(activity.getPassword());
        }
    }


/**
     * 登录  userName    用户名
            userPwd   "51"+md5(用户密码)+"gfj"
     */

    public void post_login() {
        String phone = et_phone.getText().toString().trim();
        String password = et_code.getText().toString().trim();
        if (!AppUtil.isPhone(phone)) {
            Toast.makeText(getActivity(), "请输入正确的手机号喔！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getActivity(), "密码最少6位喔！", Toast.LENGTH_SHORT).show();
            return;
        }
        params.clear();
        params.put("userName",phone);
        String tem ="51"+AppUtil.getMD5(password)+"gfj";
        params.put("userPwd",AppUtil.getMD5(tem));
        String registrationID = JPushInterface.getRegistrationID(mActivity.getApplicationContext());
        params.put("jpushId",registrationID);
        params.put("lng",ShpfUtil.getStringValue("lng"));
        params.put("lat",ShpfUtil.getStringValue("lat"));
        params.put("jpushId",registrationID);
        OkHttpUtils
                .postString()
                .url(MyURL.URL_LOGIN)
                .content(new Gson().toJson(params))
                .build()
                .execute(new Callback<UserInfo>() {
                    @Override
                    public UserInfo parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        Log.e("json", "" + json);
                        Log.e("parseNetworkResponse", "parseNetworkResponse");

                        return  new Gson().fromJson(json, UserInfo.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e("onError", "onError");
                    }

                    @Override
                    public void onResponse(UserInfo response) {
                        Log.e("onResponse", "onResponse");
                        String msg;
                        if (response.getStatus() == 1) {
                            msg = response.getInfo();
                            response.setIs_remenrber(tag);
//                           Util.connect(response.getToken(),getActivity());
                            AppUtil.is_logining = true;
                            ShpfUtil.setObject(ShpfUtil.LOGIN, response);
                            Util.connect(response.getToken(),getActivity());
                            anInterface.onCallBack(0);
                        } else {
                            msg = response.getInfo();
                        }
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }



    public void setBaseInterface(MyCallBack anInterface) {
        this.anInterface = anInterface;
    }


/**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_tv:
                getActivity().finish();
                break;
            case R.id.btn_register :
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.content_login, new RegisterFragment(), "Register");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.right_tv :
                FragmentManager managers = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransactions = managers.beginTransaction();
                fragmentTransactions.replace(R.id.content_login, new RegisterFragment(), "Register");
                fragmentTransactions.addToBackStack(null);
                fragmentTransactions.commit();
                break;
            case R.id.lost_password :
                FragmentManager managerss = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransactionss = managerss.beginTransaction();
                ForgotFragment fragment =   new ForgotFragment();
                fragment.setBaseInterface(anInterface);
                fragmentTransactionss.replace(R.id.content_login,fragment, "Forgot");
                fragmentTransactionss.addToBackStack(null);
                fragmentTransactionss.commit();
                break;
            case R.id.btn_login:
                post_login();
                break;
            case R.id.remember_code:
               if(!tag){
                   remember_code.setImageResource(R.drawable.login_select);
                   tag = true;
               }else {
                   remember_code.setImageResource(R.drawable.login_no_select);
                   tag = false;
               }
                break;
        }
    }




}

