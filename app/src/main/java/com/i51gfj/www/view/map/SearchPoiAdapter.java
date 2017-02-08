package com.i51gfj.www.view.map;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.i51gfj.www.R;
import com.qyx.android.weight.utils.baidumap.LocationBean;

public class SearchPoiAdapter extends BaseAdapter {
	private Context mContext;
	private List<LocationBean> cityPoiList;

	public SearchPoiAdapter(Context context, List<LocationBean> list) {
		this.mContext = context;
		this.cityPoiList = list;
	}

	@Override
	public int getCount() {
		if (cityPoiList != null) {
			return cityPoiList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (cityPoiList != null) {
			return cityPoiList.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class CityPoiHolder {
		public TextView tvMLIPoiName, tvMLIPoiAddress;
	}

	private CityPoiHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new CityPoiHolder();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(
					R.layout.optional_address_layout, null);
			holder.tvMLIPoiName = (TextView) convertView
					.findViewById(R.id.tvMLIPoiName);
			holder.tvMLIPoiAddress = (TextView) convertView
					.findViewById(R.id.tvMLIPoiAddress);
			convertView.setTag(holder);
		} else {
			holder = (CityPoiHolder) convertView.getTag();
		}
		LocationBean cityPoi = cityPoiList.get(position);
		holder.tvMLIPoiName.setText(cityPoi.getLocName());
		holder.tvMLIPoiAddress.setText(cityPoi.getAddStr());
		return convertView;
	}
}