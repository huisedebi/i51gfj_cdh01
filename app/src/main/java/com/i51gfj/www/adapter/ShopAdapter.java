package com.i51gfj.www.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.i51gfj.www.R;
import com.i51gfj.www.fragment.ShopFragment;
import com.i51gfj.www.holder.IndexHolder;
import com.i51gfj.www.holder.ShopHolder;
import com.i51gfj.www.model.ShopBean;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 */
public class ShopAdapter extends RecyclerView.Adapter<ShopHolder>{

    private ShopFragment fragment;
    private FragmentActivity mActivity;
    private ShopBean data;
    public ShopAdapter(ShopFragment shopFragment, FragmentActivity mActivity) {
        this.fragment = shopFragment;
        this.mActivity = mActivity;
    }

    @Override
    public ShopHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        View view = null;
        view = layoutInflater.inflate(R.layout.item_shop_one,parent,false);
        return new ShopHolder(view);
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
        holder.img_star.setImageResource(Util.getStartResource(data.getData().get(position).getAvgPoint()));
        holder.layout_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onMyItemClick(data.getData().get(position).getId());
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

    public void setData(ShopBean response) {
        this.data = response;
        notifyDataSetChanged();
    }
}
