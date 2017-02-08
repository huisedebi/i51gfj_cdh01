package com.i51gfj.www.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.i51gfj.www.fragment.BaseFragment;

import java.util.List;


/**
 */
public class MinePageAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> fragments;
    private String [] titles;


    public MinePageAdapter(FragmentManager fm,List<BaseFragment> fragments,String [] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (titles.length > position) ? titles[position] : "";
    }

    @Override
    public Fragment getItem(int position) {
        return (fragments == null || fragments.size() == 0) ? null : fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size() ;
    }
}
