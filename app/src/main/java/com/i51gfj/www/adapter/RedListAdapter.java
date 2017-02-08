package com.i51gfj.www.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.i51gfj.www.R;
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
public class RedListAdapter extends RecyclerView.Adapter<RedListHolder>{
        private Activity mActivity;
        private RedListFragment fragment;
        private RedListBean data;

    public RedListAdapter(FragmentActivity mActivity, RedListFragment redListFragment) {
        this.mActivity = mActivity;
        this.fragment = redListFragment;
    }



    @Override
    public RedListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        if(viewType==0){
            view = layoutInflater.inflate(R.layout.item_red_one,parent,false);
        }else{
            view =layoutInflater.inflate(R.layout.item_red_two,parent,false);
        }
        return  new RedListHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(final RedListHolder holder, int position) {
        if(position==0){
            List<String > imgurls = new ArrayList<>();
            List<String > titles = new ArrayList<>();
            final List<String > urls = new ArrayList<>();
            for (int i = 0; i < data.getAdvs().size(); i++) {
                imgurls.add(data.getAdvs().get(i).getImg());
                titles.add(data.getAdvs().get(i).getTitle());
                Log.i("Advs", "onBindViewHolder: "+data.getAdvs().get(i).getUrl()+"_____"+i);
                urls.add(data.getAdvs().get(i).getUrl());
            }
            holder.banner.setImageLoader(new GlideImageLoader());
            holder.banner.setImages(imgurls);
            holder.banner.setBannerTitles(titles);
            holder.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
            holder.banner.setIndicatorGravity(BannerConfig.RIGHT);
            holder.banner.start();
            holder.banner.setOnBannerClickListener(new OnBannerClickListener() {
                @Override
                public void OnBannerClick(int position) {
                    if(data.getAdvs().get(position-1).getTypeId()!=null || !data.getAdvs().get(position-1).getTypeId().equals("")){
                        fragment.onMyItemClick("advs",data.getAdvs().get(position-1).getTypeId());
                    }
                }
            });
        }
      else{
            final int temp =position-1;
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = AppUtil.getNormalImageOptions();
            imageLoader.displayImage(data.getData().get(temp).getImg(), holder.big_img,options);
            holder.collect.setText(data.getData().get(temp).getCollect());
            holder.distent.setText(data.getData().get(temp).getDistance());
            holder.name.setText(data.getData().get(temp).getTitle());
            holder.img_star.setImageResource(Util.getStartResource(data.getData().get(temp).getAvgPoint()));
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

    }

    @Override
    public int getItemViewType(int position) {
       return position;
    }

    @Override
    public int getItemCount() {
        if(data==null){
            return 0;
        }
        return data.getData().size()+1;
    }

    public void setData(RedListBean response) {
        data = response;
        notifyDataSetChanged();
    }
}
