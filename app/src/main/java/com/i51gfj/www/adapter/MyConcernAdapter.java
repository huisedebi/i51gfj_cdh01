package com.i51gfj.www.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Process;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.i51gfj.www.R;
import com.i51gfj.www.fragment.BaseFragment;
import com.i51gfj.www.fragment.MineMyConcernFragment;
import com.i51gfj.www.holder.IndexHolder;
import com.i51gfj.www.holder.MyCountHolder;
import com.i51gfj.www.holder.ShopHolder;
import com.i51gfj.www.model.ShopBean;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import io.rong.imkit.RongIM;


/**
 */
public class MyConcernAdapter extends RecyclerView.Adapter<ShopHolder>{
    ShopBean data;
    MineMyConcernFragment fragment;
    Activity mActivity;


    public MyConcernAdapter(ShopBean data, MineMyConcernFragment fragment, Activity mActivity) {
        this.data = data;
        this.fragment = fragment;
        this.mActivity = mActivity;
    }

    @Override
    public ShopHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShopHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_shop_one,parent,false));
    }

    @Override
    public void onBindViewHolder(ShopHolder holder, final int position) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = AppUtil.getNormalImageOptions();
        imageLoader.displayImage(data.getData().get(position).getImg(),holder.bigImg,options);
        holder.tv1.setText(data.getData().get(position).getName());
        holder.tv2.setText(data.getData().get(position).getAddress());
        holder.tv3.setText(data.getData().get(position).getCollect());
        holder.tv4.setText(data.getData().get(position).getDistance());
        holder.layout_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onMyItemClick(data.getData().get(position).getId());
            }
        });
        holder.img_star.setImageResource(Util.getStartResource(data.getData().get(position).getAvgPoint()));
        holder.tv_cancel.setVisibility(View.VISIBLE);
        holder.tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alterDialog = new AlertDialog.Builder(fragment.getActivity());
                alterDialog.setMessage("确定取消关注？");
                alterDialog.setCancelable(true);
                alterDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragment.onMyItemClick("cancel",data.getData().get(position).getId());
                        data.getData().remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    }
                });
                alterDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alterDialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        if(data==null){
            return 0;
        }
        return data.getData().size();
    }
}
