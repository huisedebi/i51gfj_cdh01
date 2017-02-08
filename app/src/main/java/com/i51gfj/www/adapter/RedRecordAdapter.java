package com.i51gfj.www.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.i51gfj.www.R;
import com.i51gfj.www.banner.ADInfo;
import com.i51gfj.www.banner.ImageCycleView;
import com.i51gfj.www.fragment.MineRedRecordFragment;
import com.i51gfj.www.fragment.RedListFragment;
import com.i51gfj.www.holder.RedListHolder;
import com.i51gfj.www.model.RedListBean;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


/**
 */
public class RedRecordAdapter extends RecyclerView.Adapter<RedListHolder>{
        private Activity mActivity;
        private MineRedRecordFragment fragment;
        private RedListBean data;

    public RedRecordAdapter(FragmentActivity mActivity, MineRedRecordFragment redListFragment) {
        this.mActivity = mActivity;
        this.fragment = redListFragment;
    }

    @Override
    public RedListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
            view =layoutInflater.inflate(R.layout.item_red_record,parent,false);
        return  new RedListHolder(view,viewType+1);
    }

    @Override
    public void onBindViewHolder(RedListHolder holder, int position) {

            final int temp =position;
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = AppUtil.getNormalImageOptions();
        imageLoader.displayImage(data.getData().get(temp).getImg(), holder.big_img, options);
        if(fragment.type.equals("1")){//领取记录
            holder.name.setText(data.getData().get(temp).getTitle());
            if(data.getData().get(temp).getIsAd()!=1){
                holder.collect.setText(data.getData().get(temp).getCollect());
                holder.distent.setText(data.getData().get(temp).getDistance());
                holder.img_good.setVisibility(View.VISIBLE);
                holder.img_juli.setVisibility(View.VISIBLE);
                holder.img_start.setVisibility(View.VISIBLE);
                holder.img_start.setImageResource(Util.getStartResource(data.getData().get(temp).getAvgPoint()));
                holder.distent.setVisibility(View.VISIBLE);
                holder.tv_good.setVisibility(View.VISIBLE);
                holder.layout_collect.setVisibility(View.VISIBLE);
            }else{
               /* holder.tv_content.setText(Html.fromHtml(data.getData().get(temp).getTextH5()));*/
                holder.tv_content.setPadding(5,0,5,30);holder.tv_content.setVisibility(View.GONE);

            }
            holder.layout_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.onMyItemClick(data.getData().get(temp).getId());
                }
            });
        }else{
            holder.collect.setText(data.getData().get(temp).getCollect());
            holder.name.setText(data.getData().get(temp).getTitle());
            holder.tv_content.setText(Html.fromHtml(data.getData().get(temp).getTextH5()));
            holder.img_good.setVisibility(View.GONE);
            holder.img_juli.setVisibility(View.GONE);
            holder.img_start.setVisibility(View.GONE);
            holder.distent.setVisibility(View.GONE);
            holder.tv_good.setVisibility(View.GONE);
            holder.layout_collect.setVisibility(View.GONE);
            holder.name.setPadding(5,10,5,0);
            holder.tv_content.setPadding(5,0,5,30);

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
        return data.getData().size();
    }

    public void setData(RedListBean response) {
        data = response;
        notifyDataSetChanged();
    }
}
