package com.i51gfj.www.adapter;

import com.i51gfj.www.fragment.BaseFragment;

import java.util.Objects;

/**
 * Created by Administrator on 2016/8/12.
 */
public interface   BaseAdapter {

    public BaseFragment fragment = null;
    public Objects data = null;


    public void setData(Objects O);
    public void setFragment(BaseFragment b);
}
