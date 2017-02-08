package com.i51gfj.www.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.activity.LoginActivity;
import com.i51gfj.www.alipay.PayResult;
import com.i51gfj.www.alipay.SignUtils;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.model.OpenHyBean;
import com.i51gfj.www.model.UserInfo;
import com.i51gfj.www.model.ZhiFuBaoBean;
import com.i51gfj.www.model.ZhiFuBean;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.ShpfUtil;
import com.i51gfj.www.util.Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Administrator on 2016/8/11.
 */
public class OpenHYFragment extends BaseFragment {
    private UserInfo loginData;
    TextView tv_title, tv_money, tv_one, tv_two, tv_three;
    ImageView img_zhifubao, img_weixin;
    Button open;
    LinearLayout layout_zhifubao, layout_weixin;
    private int tag = 28;//28表示支付宝，27表示微信   初始化是28
    private OpenHyBean data;
    StringBuffer sb;
    Map<String, String> resultunifiedorder;
    PayReq req;
    IWXAPI msgApi;
    private ZhiFuBean weixin_data;
    private ZhiFuBaoBean zhifubao_data;
    private MyHandler mHandler;
    private static boolean can_upload = true;

    @Override
    public int getLayout() {
        return R.layout.mine_fragment_open_hy;
    }

    @Override
    public void initView(View view) {
        tv_title = (TextView) view.findViewById(R.id.title_tv);
        view.findViewById(R.id.left_tv).setOnClickListener(this);
        tv_title.setText("开通会员");
        tv_money = (TextView) view.findViewById(R.id.money);
        tv_one = (TextView) view.findViewById(R.id.tv_one);
        tv_two = (TextView) view.findViewById(R.id.tv_two);
        tv_three = (TextView) view.findViewById(R.id.tv_three);
        img_zhifubao = (ImageView) view.findViewById(R.id.img_zhifubao);
        img_weixin = (ImageView) view.findViewById(R.id.img_weixin);
        open = (Button) view.findViewById(R.id.open);
        open.setOnClickListener(this);
        layout_zhifubao = (LinearLayout) view.findViewById(R.id.layout_zhifubao);
        layout_zhifubao.setOnClickListener(this);
        layout_weixin = (LinearLayout) view.findViewById(R.id.layout_weixin);
        layout_weixin.setOnClickListener(this);
    }

    @Override
    public void initData() {
        post_main();
    }

    public void post_main() {
        loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
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
        // Util.showMyLoadingDialog(mloadingDialog,getActivity());
        OkHttpUtils
                .postString()
                .url(MyURL.URL_MINE_OPEN_HY)
                .content(json.toString())
                .build()
                .execute(new Callback<OpenHyBean>() {
                    @Override
                    public OpenHyBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, OpenHyBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                    }

                    @Override
                    public void onResponse(OpenHyBean response) {
                        if (response.getStatus() == 1) {
                            data = response;
                            setValue(response);
                        }
                    }
                });
    }

    private void setValue(OpenHyBean response) {
        tv_money.setText(response.getAmount() + "/年");
        if (response.getText() != null && response.getText().size() > 2)
            tv_one.setText("1." + response.getText().get(0).getTitle());
        tv_two.setText("2." + response.getText().get(1).getTitle());
        tv_three.setText("3." + response.getText().get(2).getTitle());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_tv:
                getActivity().finish();
                break;
            case R.id.layout_zhifubao:
                img_zhifubao.setImageResource(R.drawable.login_select);
                img_weixin.setImageResource(R.drawable.login_no_select);
                tag = 28;
                break;
            case R.id.layout_weixin:
                img_zhifubao.setImageResource(R.drawable.login_no_select);
                img_weixin.setImageResource(R.drawable.login_select);
                tag = 27;
                break;
            case R.id.open:
                if (can_upload) {
                    can_upload = false;
                    if (tag == 28) {
                        post_upload_ZHIFUBAO();
                    } else {
                        post_upload();
                    }
                }
                break;
        }
    }

    /* uid        *用户id
     amount     *金额
     payment_id *支付id　　27微信支付 28支付宝支付*/
    private void post_upload() {
        Util.showLoading(getActivity());
        JSONObject json = new JSONObject();
        try {
            json.put("uid", loginData.getUid());
            json.put("amount", data.getAmount());
            json.put("payment_id", tag + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .postString()
                .url(MyURL.URL_MINE_OPEN_HY_UPLOAD)
                .content(json.toString())
                .build()
                .execute(new Callback<ZhiFuBean>() {
                    @Override
                    public ZhiFuBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, ZhiFuBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                    }

                    @Override
                    public void onResponse(ZhiFuBean response) {
                        if (response.getStatus() == 1) {
                            weixin_data = response;
                            Util.closeLoading();
                            if (tag == 27) {//微信支付
                                IWXAPI api= WXAPIFactory.createWXAPI(getActivity(), null);
                                api.registerApp(response.getPay().getConfig().getAppid());
                                PayReq request = new PayReq();
                                request.appId = response.getPay().getConfig().getAppid();
                                request.partnerId = response.getPay().getConfig().getPartnerid();
                                request.prepayId= response.getPay().getConfig().getPackagevalue().split("=")[1];
                                request.packageValue = response.getPay().getConfig().getPackagevalue();
                                request.nonceStr= response.getPay().getConfig().getNoncestr();
                                request.timeStamp= response.getPay().getConfig().getTimestamp();
                                request.sign= response.getPay().getConfig().getSign();
                                api.sendReq(request);
                                can_upload =true;
                            }
                        }
                    }
                });
    }

    /* uid        *用户id
       amount     *金额
       payment_id *支付id　　27微信支付 28支付宝支付*/
    private void post_upload_ZHIFUBAO() {
        JSONObject json = new JSONObject();
        try {
            json.put("uid", loginData.getUid());
            json.put("amount", data.getAmount());
            json.put("payment_id", tag + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .postString()
                .url(MyURL.URL_MINE_OPEN_HY_UPLOAD)
                .content(json.toString())
                .build()
                .execute(new Callback<ZhiFuBaoBean>() {
                    @Override
                    public ZhiFuBaoBean parseNetworkResponse(Response response) throws IOException {
                        String json = response.body().string();
                        return new Gson().fromJson(json, ZhiFuBaoBean.class);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        Log.e("onError", e.toString(), e);
                    }

                    @Override
                    public void onResponse(ZhiFuBaoBean response) {
                        if (response.getStatus() == 1) {
                            zhifubao_data = response;
                            mHandler = new MyHandler(mActivity);
                            pay_zhifubao(response, getActivity());
                        }
                    }
                });
    }


    /**
     * 支付宝支付
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay_zhifubao(ZhiFuBaoBean response, final FragmentActivity mActivity) {
        // 订单
        String orderInfo;
        // 签约合作者身份ID
        orderInfo = "partner=" + "\"" + com.i51gfj.www.util.Util.PARTNER + "\"";
        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + com.i51gfj.www.util.Util.SELLER + "\"";
        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + response.getOrder_sn() + "\"";
        // 商品名称
        orderInfo += "&subject=" + "\"" + response.getOrder_name() + "\"";
        // 商品详情
        orderInfo += "&body=" + "\"" + response.getOrder_desc() + "\"";
        // 商品金额
        orderInfo += "&total_fee=" + "\"" + response.getAmount() + "\"";
        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://www.51gfj.com/callback/payment/aliapp_notify.php" + "\"";
        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";
        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";
        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";
        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";
        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        // 对订单做RSA 签名
        String sign = SignUtils.sign(orderInfo, com.i51gfj.www.util.Util.RSA_PRIVATE);
        // 仅需对sign 做URL编码
        if (sign != null) {
            try {
                sign = URLEncoder.encode(sign, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + "sign_type=\"RSA\"";


        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                Looper.prepare();
                PayTask alipay = new PayTask(mActivity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                new MyHandler(mActivity).sendMessage(msg);
                Looper.loop();
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    static class MyHandler extends Handler {
        private FragmentActivity mActivity;

        MyHandler(FragmentActivity mActivity) {
            this.mActivity = mActivity;
        }


        @Override
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((String) msg.obj);
            // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
            String resultInfo = payResult.getResult();
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            can_upload =true;
            if (TextUtils.equals(resultStatus, "9000")) {
                Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
                AppUtil.is_frash = true;
//                }
                mActivity.setResult(9000);
            } else {

                // 判断resultStatus 为非“9000”则代表可能支付失败
                // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(resultStatus, "8000")) {
                    Toast.makeText(mActivity, "支付结果确认中", Toast.LENGTH_SHORT).show();
                } else {
                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    Toast.makeText(mActivity, "支付取消", Toast.LENGTH_SHORT).show();
                }
                mActivity.setResult(6001);
            }
        }
    }
}
