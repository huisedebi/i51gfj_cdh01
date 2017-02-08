package com.i51gfj.www.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.i51gfj.www.R;
import com.i51gfj.www.fragment.RedAdvaFragment;
import com.i51gfj.www.fragment.RedListFragment;
import com.i51gfj.www.holder.RedListHolder;
import com.i51gfj.www.model.RedListBean;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.Util;
import com.i51gfj.www.view.GlideImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 */
public class RedAdvaAdapter extends RecyclerView.Adapter<RedListHolder>{
        private Activity mActivity;
        private RedAdvaFragment fragment;
        private RedListBean data;

    public RedAdvaAdapter(FragmentActivity mActivity, RedAdvaFragment redListFragment) {
        this.mActivity = mActivity;
        this.fragment = redListFragment;
    }



    @Override
    public RedListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
            view =layoutInflater.inflate(R.layout.item_red_adva,parent,false);
        return  new RedListHolder(view,viewType+1);
    }

    @Override
    public void onBindViewHolder(final RedListHolder holder, int position) {

            final int temp =position;
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = AppUtil.getNormalImageOptions();
            imageLoader.displayImage(data.getData().get(temp).getImg(), holder.big_img,options);
            holder.name.setText(data.getData().get(temp).getTitle());
            holder.distent.setText(data.getData().get(temp).getContent());
            if(data.getData().get(temp).getIsGet()==1){
                holder.red_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.onMyItemClick(data.getData().get(temp).getId());
                    }
                });
                holder.layout_lin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.onMyItemClick(data.getData().get(temp).getId());
                    }
                });
            }else if(data.getData().get(temp).getIsGet()== -1){
                holder.red_button.setText("未登录");
                holder.red_button.setBackgroundResource(R.drawable.corner_org_5dp_gred);
            }else{
                holder.red_button.setText("已领完");
                holder.red_button.setBackgroundResource(R.drawable.corner_org_5dp_gred);
            }


    }

    @Override
    public int getItemViewType(int position) {
       return position;
    }

    @Override
    public int getItemCount() {
        if(data==null || data.getData()==null){
            return 0;
        }
        return data.getData().size();
    }

    public void setData(RedListBean response) {
        data = response;
        notifyDataSetChanged();
    }
}
