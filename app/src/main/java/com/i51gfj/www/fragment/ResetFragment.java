package com.i51gfj.www.fragment;

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
 * 重置密码
 */
public class ResetFragment extends BaseFragment {

    private EditText et_password1;
    private EditText et_password2,ed_old_psw;
    TextView tv_title;
    private HashMap<String, String> params = new HashMap<>();
    private MyCallBack anInterface;
    private LoadingDialog mloadingDialog;
    public void setBaseInterface(MyCallBack anInterface) {
        this.anInterface = anInterface;
    }

    /**
     * 得到布局
     */
    @Override
    public int getLayout() {
        return R.layout.fragment_reset;
    }

    /**
     * 初始化控件
     */
    @Override
    public void initView(View view) {
        view.findViewById(R.id.left_tv).setOnClickListener(this);
        tv_title = (TextView) view.findViewById(R.id.title_tv);
        tv_title.setText("修改密码");
        et_password1 = (EditText) view.findViewById(R.id.et_password1);
        et_password2 = (EditText) view.findViewById(R.id.et_password2);
        ed_old_psw = (EditText) view.findViewById(R.id.ed_old_psw);
        view.findViewById(R.id.bt_change).setOnClickListener(this);
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
       /* et_password1.setText("");
        et_password2.setText("");*/
    }

    /**
     * 验证
     */
    public void post_reset() {
        String old = ed_old_psw.getText().toString().trim();
        String new1 = et_password1.getText().toString().trim();
        String new2 = et_password2.getText().toString().trim();
        if (old.equals("")){
            Toast.makeText(getActivity(), "请输入旧密码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (new1.length() <6){
            Toast.makeText(getActivity(), "密码的最小长度为6位喔！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (new1.length() >21){
            Toast.makeText(getActivity(), "密码的最大长度为20位喔！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!new1.equals(new2)) {
            Toast.makeText(getActivity(), "您2次输入的密码不相同喔！", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getActivity(), "正在修改密码中，请稍后···", Toast.LENGTH_SHORT).show();
        params.clear();
        LoginActivity activity = (LoginActivity) getActivity();
       /* uid    *用户id
        pwd     旧密码   用户密码   客户端普通md5加密后传上来
        newPwd　新密码   用户密码   客户端普通md5加密后传上来*/
        params.put("uid", AppUtil.getUserId());
        params.put("pwd", AppUtil.getMD5(ed_old_psw.getText().toString().trim()));
        params.put("newPwd", AppUtil.getMD5(et_password1.getText().toString().trim()));
       // Util.showMyLoadingDialog(mloadingDialog,getActivity());
        OkHttpUtils.postString()
                .url(MyURL.URL_CHANGE_PSW)
                .content(new Gson().toJson(params))
                .build()
                .execute(new Callback<EntityWrapper>() {
                    @Override
                    public EntityWrapper parseNetworkResponse(Response response) throws IOException {
                        String json =response.body().string();
                        return new Gson().fromJson(json, EntityWrapper.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                       // Util.dissMyLoadingDialog(mloadingDialog);
                    }

                    @Override
                    public void onResponse(EntityWrapper response) {
                        String msg;
                        if (response.getStatus() == 1) {
                            msg = response.getInfo();
                          /*  LoginActivity activity = (LoginActivity) getActivity();
                            activity.setPassword(et_password1.getText().toString().trim());*/
                            //anInterface.onCallBack(3);
                        } else {
                            msg = response.getInfo();
                        }
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_change:
                post_reset();
                break;
            case R.id.left_tv:
                getActivity().finish();
                break;
        }
    }
}
