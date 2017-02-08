package com.i51gfj.www.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.i51gfj.www.R;
import com.i51gfj.www.fragment.ShopInfoFragment;
import com.i51gfj.www.holder.PickPhotoHolder;
import com.i51gfj.www.util.AppUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 */
public class PickPhotoAdapterForShopInfo extends RecyclerView.Adapter<PickPhotoHolder>{

    public List<String> data;
    FragmentActivity mActivity;
    ShopInfoFragment fragment;

    public PickPhotoAdapterForShopInfo(FragmentActivity mActivity, ShopInfoFragment mineSendRedFragment) {
        this.mActivity = mActivity;
        fragment =mineSendRedFragment;
    }


    @Override
    public PickPhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
            view = layoutInflater.inflate(R.layout.item_pick_photo,parent,false);

        return  new PickPhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(PickPhotoHolder holder, final int position) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = AppUtil.getNormalImageOptions();
        if(data!=null){
            if(data.size()==position){
                if(position >= 9){
                    return;
                }
                holder.img_main.setImageResource(R.drawable.ic_pick_photo);
                holder.img_main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.onMyItemClick(9-data.size());
                    }
                });
            }else{
                if(data.get(position).startsWith("http")){
                    imageLoader.displayImage(data.get(position),holder.img_main,options);
                }else {
                    imageLoader.displayImage("file://" + data.get(position), holder.img_main, options);
                }
                holder.img_main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
        }else{
            holder.img_main.setImageResource(R.drawable.ic_pick_photo);
            holder.img_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.onMyItemClick(9);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
      return data==null?1:data.size()+1;
    }

    public void setData(List<String> data) {
        if(this.data!=null && data!=null){
            this.data.addAll(data);
        }else if(this.data==null){
            this.data=data;
        }
        notifyDataSetChanged();
    }




}
