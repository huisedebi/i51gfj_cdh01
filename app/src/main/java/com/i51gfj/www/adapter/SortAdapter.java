package com.i51gfj.www.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.i51gfj.www.R;
import com.i51gfj.www.model.CityWrapper;
import com.i51gfj.www.util.ShpfUtil;
import com.i51gfj.www.view.sortlistview.SortModel;

import java.util.List;

public class SortAdapter extends BaseAdapter implements SectionIndexer {
    private List<SortModel> list = null;
    private List<String> last_call_city;
    private Context mContext;
    private CityWrapper data;

    public SortAdapter(Context mContext, List<SortModel> list, List<String> last_call_city) {
        this.mContext = mContext;
        this.list = list;
        this.last_call_city = last_call_city;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list post得到的城市列表数据
     */
    public void updateListView(List<SortModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {

        return this.list.size() + /*last_call_city.size() + */1;
    }

    public String getItem(int position) {
       /* if (position < last_call_city.size() + 1) {*/
            if (position == 0) {
                return ShpfUtil.getStringValue("city_name");
            }/*else {
                return last_call_city.get(position - 1);
            }*/
       /* }*/
        return list.get(position /*- last_call_city.size()*/ - 1).getName();
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder;
//		final SortModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_list_city, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (position == 0) {
            String city = ShpfUtil.getStringValue("location_city");
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText("当前定位的城市");
            viewHolder.tvTitle.setText(city);
        } else {
            //根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position /*- last_call_city.size()*/ - 1);

            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position /*- last_call_city.size()*/ - 1 == getPositionForSection(section)) {
                viewHolder.tvLetter.setVisibility(View.VISIBLE);
                viewHolder.tvLetter.setText(list.get(position /*- last_call_city.size()*/ - 1).getSortLetters());
            } else {
                viewHolder.tvLetter.setVisibility(View.GONE);
            }
            viewHolder.tvTitle.setText(this.list.get(position /*- last_call_city.size()*/ - 1).getName());
        }
        return view;

    }

    public void setData(CityWrapper data) {
        this.data = data;
    }


    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
    }


    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < list.size(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}