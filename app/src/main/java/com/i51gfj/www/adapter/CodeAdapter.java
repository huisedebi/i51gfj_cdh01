package com.i51gfj.www.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.i51gfj.www.R;
import com.i51gfj.www.banner.ADInfo;
import com.i51gfj.www.fragment.BaseFragment;
import com.i51gfj.www.holder.IndexHolder;
import com.i51gfj.www.model.MainBean;
import com.i51gfj.www.util.AppUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


/**
 */
public class CodeAdapter extends RecyclerView.Adapter<IndexHolder>{


    @Override
    public IndexHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(IndexHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
