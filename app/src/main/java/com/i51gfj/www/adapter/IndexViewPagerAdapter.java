package com.i51gfj.www.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/8/15.
 */
public class IndexViewPagerAdapter extends PagerAdapter {

    List<View> viewLists;


    public IndexViewPagerAdapter(List<View> viewLists) {
        this.viewLists = viewLists;
    }

    @Override
    public int getCount() {
        return viewLists.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewGroup parent = (ViewGroup) viewLists.get(position).getParent();
        if (parent != null) {
            return viewLists.get(position);
        }
         container.addView(viewLists.get(position));
        return viewLists.get(position);
    }
}
