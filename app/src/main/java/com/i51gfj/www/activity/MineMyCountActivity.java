package com.i51gfj.www.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.i51gfj.www.R;
import com.i51gfj.www.adapter.MinePageAdapter;
import com.i51gfj.www.fragment.BaseFragment;
import com.i51gfj.www.fragment.MyCountFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


public class MineMyCountActivity extends FragmentActivity implements View.OnClickListener{
	private ViewPager viewPager;
	private String []  titles ={"充值记录","提现记录"};
	private PagerTabStrip tabStrip;
	List<BaseFragment> fragments= new ArrayList<>();
	private TextView title;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_activity_mycount);
		inteView();

	}

	private void inteView() {
		findViewById(R.id.left_tv).setOnClickListener(this);
		title = (TextView) findViewById(R.id.title_tv);
		title.setText("我的账单");
		viewPager= (ViewPager) findViewById(R.id.viewpager);
		tabStrip= (PagerTabStrip) findViewById(R.id.tabstrip);
		tabStrip = (PagerTabStrip) this.findViewById(R.id.tabstrip);
		//取消tab下面的长横线
		tabStrip.setDrawFullUnderline(false);
		//设置tab的背景色
		tabStrip.setBackgroundColor(this.getResources().getColor(R.color.line2));
		//设置当前tab页签的下划线颜色
		tabStrip.setTabIndicatorColor(this.getResources().getColor(R.color.index_main));
		tabStrip.setTextSpacing(200);
		fragments.add(new MyCountFragment("消费记录", "1"));
		fragments.add(new MyCountFragment("提现记录","2"));
		viewPager.setAdapter(new MinePageAdapter(getSupportFragmentManager(),fragments,titles));
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.left_tv){
			finish();
		}
	}


	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		JPushInterface.onResume(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);

	}
}
