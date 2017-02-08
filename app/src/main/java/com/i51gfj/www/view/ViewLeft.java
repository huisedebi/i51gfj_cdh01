package com.i51gfj.www.view;


import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.i51gfj.www.R;
import com.i51gfj.www.adapter.TextAdapter;
import com.i51gfj.www.model.ShopBean;

import java.util.List;


public class ViewLeft extends RelativeLayout implements ViewBaseAction{

	private ListView mListView;
	private  String[] items = new String[] { "默认", "好评",  "最新" };//显示字段
	private  String[] itemsVaule = new String[] { "default", "avg_point", "newest"};//隐藏id
	private OnSelectListener mOnSelectListener;
	private TextAdapter adapter;
	private String mDistance;
	private String showText = "默认";
	private Context mContext;

	public String getShowText() {
		return showText;
	}

	public ViewLeft(Context context) {
		super(context);
		init(context);
	}

	public ViewLeft(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ViewLeft(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_distance, this, true);
//		setBackgroundDrawable(getResources().getDrawable(R.drawable.choosearea_bg_mid));
		mListView = (ListView) findViewById(R.id.listView);
	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public void setItemsData(List<ShopBean.CateListBean> cateList, FragmentActivity activity) {
		mContext = activity;
		items = new String[cateList.size()];
		itemsVaule = new String[cateList.size()];
		for(int i=0;i<cateList.size();i++){
			items[i] = cateList.get(i).getName();
			itemsVaule[i] = cateList.get(i).getCid();
		}
		changeM();
	}

	public void setItemsRight(List<ShopBean.NavsBean> navs, FragmentActivity activity) {
		items = new String[navs.size()];
		itemsVaule = new String[navs.size()];
		for(int i=0;i<navs.size();i++){
			items[i] = navs.get(i).getName();
			itemsVaule[i] = navs.get(i).getOrderType();
		}
		changeM();
	}

	public interface OnSelectListener {
		public void getValue(String distance, String showText);
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void show() {
		
	}
	void  changeM(){
		adapter = new TextAdapter(mContext, items, R.drawable.choose_item_right, R.drawable.choose_eara_item_selector);
		adapter.setTextSize(17);
		if (mDistance != null) {
			for (int i = 0; i < itemsVaule.length; i++) {
				if (itemsVaule[i].equals(mDistance)) {
					adapter.setSelectedPositionNoNotify(i);
					showText = items[i];
					break;
				}
			}
		}
		mListView.setAdapter(adapter);
		adapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {

				if (mOnSelectListener != null) {
					showText = items[position];
					mOnSelectListener.getValue(itemsVaule[position], items[position]);
				}
			}
		});
	}



}
