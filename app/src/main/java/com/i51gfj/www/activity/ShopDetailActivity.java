package com.i51gfj.www.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.i51gfj.www.R;
import com.i51gfj.www.adapter.ShopDetailAdapter;
import com.i51gfj.www.application.MyApplication;
import com.i51gfj.www.constant.MyURL;
import com.i51gfj.www.model.ShopDetailBean;
import com.i51gfj.www.model.UserInfo;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.ShpfUtil;
import com.i51gfj.www.util.Util;
import com.i51gfj.www.view.BounceScrollView;
import com.i51gfj.www.view.FullyLinearLayoutManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import okhttp3.Call;
import okhttp3.Response;


/**
 * AMapV1地图中介绍如何显示世界图
 */
public class ShopDetailActivity extends FragmentActivity implements View.OnClickListener{


    ShopDetailAdapter adapter;
	private TextView lable1,lable2,label3,distance,collect,ic_dp;
	private LinearLayout layout_collect,layout_3d;
	private BounceScrollView scrollView;
	private String store_id;
	private ImageView ic_back;
	private ImageView img_detail;
	private TextView tv_detail_one,tv_detail_two,tv_righ_juli,tv_name,tv_text1;

    public String store_lat;
    public String store_lng;
	private ShopDetailBean data;
	io.rong.imlib.model.UserInfo ry_data;
	private RecyclerView recyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_shopdetail);
		scrollView = (BounceScrollView) findViewById(R.id.scrollView);
		Util.showLoading(this);
		init();
		initData();
	}

	private void initData() {

		store_id =getIntent().getStringExtra("passData");
		post_main();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		lable1 = (TextView) findViewById(R.id.label1);
		label3 = (TextView) findViewById(R.id.label3);
		ic_dp = (TextView) findViewById(R.id.ic_dp);
		distance = (TextView) findViewById(R.id.distance);
		collect = (TextView) findViewById(R.id.collect);
		tv_text1 = (TextView) findViewById(R.id.tv_text1);

		findViewById(R.id.left_tv).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		layout_collect = (LinearLayout) findViewById(R.id.layout_collect);
		layout_3d = (LinearLayout) findViewById(R.id.layout_3d);
		layout_collect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				post_collect();
			}
		});
		findViewById(R.id.ic_phone).setOnClickListener(this);
		img_detail = (ImageView) findViewById(R.id.img_detail);
		TextView title = (TextView) findViewById(R.id.title_tv);
		title.setText("详情");
		tv_righ_juli = (TextView) findViewById(R.id.tv_righ_juli);
		tv_detail_one = (TextView) findViewById(R.id.tv_img_one);
		tv_detail_two = (TextView) findViewById(R.id.tv_img_two);
		tv_name = (TextView) findViewById(R.id.tv_name);
		findViewById(R.id.ic_gt).setOnClickListener(this);

		recyclerView = (RecyclerView)findViewById(R.id.recycler);
		FullyLinearLayoutManager manage = new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		manage.setCanScroll(false);
		recyclerView.setLayoutManager(manage);
		recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
			@Override
			public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
				super.getItemOffsets(outRect, view, parent, state);
				if(parent.getChildPosition(view)!= 0){
					outRect.top = 10;
				}
				outRect.left =10;
				outRect.right = 10;
			}
		});
		adapter = new ShopDetailAdapter(this);
		recyclerView.setAdapter(adapter);
	}



	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	private void post_main() {
		JSONObject json = new JSONObject();
		try {
			json.put("id",getIntent().getStringExtra("passData"));
			json.put("lng",ShpfUtil.getStringValue("lng"));
			json.put("lat",ShpfUtil.getStringValue("lat"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
						e.printStackTrace();
						Log.e("onError", e.toString(), e);
						Util.closeLoading();
					}

					@Override
					public void onResponse(ShopDetailBean response) {
						if (response.getStatus() == 1) {
							data = response;
                            store_lat = response.getStore_info().getLat();
                            store_lng = response.getStore_info().getLng();
							setMapView(response);
						}
						Util.closeLoading();
					}

				});
	}

	private void setMapView(final ShopDetailBean response) {
		lable1.setText(response.getStore_info().getName());
		//lable2.setText(response.getStore_info().getAddress());
		label3.setText(response.getStore_info().getRoute());
		distance.setText(response.getStore_info().getDistance());
		collect.setText(response.getStore_info().getCollect());
		ic_dp.setText(response.getStore_info().getReview());
		tv_text1.setText(response.getStore_info().getText1());
		ic_dp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShopDetailActivity.this, ShopDetailCommentActivity.class);
				intent.putExtra("id", getIntent().getStringExtra("passData"));
				intent.putExtra("name", response.getStore_info().getName());
				startActivity(intent);
			}
		});
		tv_righ_juli.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShopDetailActivity.this,BNDemoMainActivity.class);
				intent.putExtra("store_lat",store_lat);
				intent.putExtra("store_lng",store_lng);
				intent.putExtra("store_name",response.getStore_info().getName());
				intent.putExtra("store_address",response.getStore_info().getAddress());
				startActivity(intent);
			}
		});
		layout_3d.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(response.getStore_info().getUrl1()!=null && !response.getStore_info().getUrl1().equals("")){
					Intent intent = new Intent(ShopDetailActivity.this, WebViewActivity.class);
					intent.putExtra("url",response.getStore_info().getUrl1());
					intent.putExtra("title",response.getStore_info().getName());
					ShopDetailActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);
					ShopDetailActivity.this.startActivity(intent);
				}
			}
		});
		adapter.setData(response);
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = AppUtil.getNormalImageOptions();
		imageLoader.displayImage(response.getStore_info().getImg(), img_detail, options);
		tv_name.setText(response.getStore_info().getName());
		tv_detail_one.setText(response.getStore_info().getAdText());
		tv_detail_two.setText(response.getStore_info().getManageArea());
		tv_righ_juli.setText(response.getStore_info().getAddress());
		adapter.setData(response);
	}
	private void post_collect() {
		UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
		if (loginData == null) {
			Toast.makeText(ShopDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(ShopDetailActivity.this, LoginActivity.class));
			return;
		}
		JSONObject json = new JSONObject();
		try {
			json.put("id",getIntent().getStringExtra("passData"));
			json.put("uid",loginData.getUid());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpUtils
				.postString()
				.url(MyURL.URL_SHOP_COLLECT)
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
						Toast.makeText(ShopDetailActivity.this, response.getInfo(), Toast.LENGTH_SHORT).show();
					}

				});
	}


	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.ic_phone){
			Intent intent = new Intent(Intent.ACTION_DIAL);
			Uri num = Uri.parse("tel:" + data.getStore_info().getTel());
			intent.setData(num);
			startActivity(intent);
		}else if(v.getId() == R.id.ic_gt){
			if(data.getStore_info().getUid().equals("0")){
				Toast.makeText(ShopDetailActivity.this, "该商家尚未开启此功能！", Toast.LENGTH_SHORT).show();
				return;
			}else{
				if( !is_login()){
					return;
				}
				post_user_info();
				UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
				if(!AppUtil.is_ry_connet){
					ShpfUtil.setObject("DEMO_TOKEN", loginData.getToken());
					connect(loginData.getToken());
				}
				if (RongIM.getInstance() != null)
				     post_user_info();

				    RongIM.getInstance().setCurrentUserInfo( new io.rong.imlib.model.UserInfo(loginData.getUid(),loginData.getUserName(),Uri.parse(loginData.getHeadImg())));
					RongIM.getInstance().startPrivateChat(ShopDetailActivity.this, data.getStore_info().getUid(), data.getStore_info().getName());
			}
		}
	}

	public  Boolean is_login(){
		UserInfo loginData = ShpfUtil.getObject(ShpfUtil.LOGIN);
		if (loginData == null) {
			Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(this, LoginActivity.class));
			return false;
		}else{
			if(AppUtil.is_logining){
				return true;
			}else{
				if(loginData.is_remenrber()){
					return true;
				}else{
					Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
					this.startActivity(new Intent(this, LoginActivity.class));
					return false;
				}
			}
		}}



	/**
	 * 建立与融云服务器的连接
	 *
	 * @param token
	 */

	private void connect(String token) {

		if (this.getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(this.getApplicationContext()))) {


/**
 * IMKit SDK调用第二步,建立与服务器的连接
 */

			RongIM.connect(token, new RongIMClient.ConnectCallback() {


				/**
				 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
				 */

				@Override
				public void onTokenIncorrect() {

					Log.d("LoginActivity", "--onTokenIncorrect");
				}


				/**
				 * 连接融云成功
				 * @param userid 当前 token
				 */

				@Override
				public void onSuccess(String userid) {
					AppUtil.is_ry_connet = true;
                  Log.d("LoginActivity", "--onSuccess" + userid);
//                    if (RongIM.getInstance() != null)
//                        RongIM.getInstance().startConversationList(getApplicationContext());
					//startActivity(new Intent(MainActivity.this,ConversationListActivity.class));
				}


				/**
				 * 连接融云失败
				 * @param errorCode 错误码，可到官网 查看错误码对应的注释
				 *                  http://www.rongcloud.cn/docs/android.html#常见错误码
				 */

				@Override
				public void onError(RongIMClient.ErrorCode errorCode) {

					Log.d("LoginActivity", "--onError" + errorCode);
				}
			});
		}
	}



	private void post_user_info() {
		JSONObject json = new JSONObject();
		try {
			json.put("uid",data.getStore_info().getUid());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpUtils
				.postString()
				.url(MyURL.URL_USER_INFO)
				.content(json.toString())
				.build()
				.execute(new Callback<UserInfo>() {
					@Override
					public UserInfo parseNetworkResponse(Response response) throws IOException {
						String json = response.body().string();
						return new Gson().fromJson(json, UserInfo.class);
					}

					@Override
					public void onError(Call call, Exception e) {
						e.printStackTrace();
						Log.e("onError", e.toString(), e);
					}

					@Override
					public void onResponse(final UserInfo response) {
						if(response!=null){
							RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
								@Override
								public io.rong.imlib.model.UserInfo getUserInfo(String s) {
									return new io.rong.imlib.model.UserInfo(response.getUid(),response.getUserName(),Uri.parse(response.getHeadImg()));
								}
							},true);
						}
						}


				});
	}


}
