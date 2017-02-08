package com.i51gfj.www.view.map;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.i51gfj.www.R;

public class AroundPoiAdapter extends BaseAdapter {
	private Context mContext;
	private List<PoiInfo> mkPoiInfoList;
	private PoiInfo selectedPoiInfo;
	private int selectPosition = 0;

	public AroundPoiAdapter(Context context, List<PoiInfo> list) {
		this.mContext = context;
		this.mkPoiInfoList = list;
	}

	@Override
	public int getCount() {
		return mkPoiInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		if (mkPoiInfoList != null) {
			return mkPoiInfoList.get(position);
		}
		return null;
	}

	public void setSelctedLatAndLon(PoiInfo _SelectedPoiInfo) {
		selectedPoiInfo = _SelectedPoiInfo;
		notifyDataSetChanged();
	}

	public void setNewList(List<PoiInfo> list) {
		this.mkPoiInfoList = list;
		this.notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class RecordHolder {
		public RelativeLayout rlMLPIItem;
		public ImageView ivMLISelected;
		public TextView tvMLIPoiName, tvMLIPoiAddress;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		RecordHolder holder = null;
		if (convertView == null) {
			holder = new RecordHolder();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.optional_address_layout,
					null);
			holder.ivMLISelected = (ImageView) convertView
					.findViewById(R.id.ivMLISelected);
			holder.tvMLIPoiName = (TextView) convertView
					.findViewById(R.id.tvMLIPoiName);
			holder.tvMLIPoiAddress = (TextView) convertView
					.findViewById(R.id.tvMLIPoiAddress);
			holder.rlMLPIItem = (RelativeLayout) convertView
					.findViewById(R.id.rlMLPIItem);
			convertView.setTag(holder);
			/*holder.rlMLPIItem.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					selectPosition = position;
					notifyDataSetChanged();
				}
			});*/

		} else {
			holder = (RecordHolder) convertView.getTag();
		}
		holder.tvMLIPoiName.setText(mkPoiInfoList.get(position).name);
		holder.tvMLIPoiAddress.setText(mkPoiInfoList.get(position).address);
		if (mkPoiInfoList.get(position).location.latitude == selectedPoiInfo.location.latitude
				&& mkPoiInfoList.get(position).location.longitude == selectedPoiInfo.location.longitude
				&& mkPoiInfoList.get(position).name
						.equals(selectedPoiInfo.name)){
		/*if(position==selectPosition){*/
			holder.ivMLISelected.setVisibility(View.VISIBLE);
		} else {
			holder.ivMLISelected.setVisibility(View.GONE);
		}
		return convertView;
	}
}
