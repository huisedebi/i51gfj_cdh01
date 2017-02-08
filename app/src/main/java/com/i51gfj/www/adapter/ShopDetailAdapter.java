package com.i51gfj.www.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.i51gfj.www.R;
import com.i51gfj.www.activity.ShopDetailActivity;
import com.i51gfj.www.fragment.RedDetaileFragment;
import com.i51gfj.www.holder.RedDetailHolder;
import com.i51gfj.www.model.RedDetailBean;
import com.i51gfj.www.model.ShopDetailBean;
import com.i51gfj.www.util.AppUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 */
public class ShopDetailAdapter extends RecyclerView.Adapter<RedDetailHolder>{
    FragmentActivity mActivity;
    ShopDetailBean data;


    public ShopDetailAdapter(FragmentActivity mActivity) {
        this.mActivity = mActivity;

    }

    @Override
    public RedDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
            view = layoutInflater.inflate(R.layout.item_red_detail_one,parent,false);
        return  new RedDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(RedDetailHolder holder, int position) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = AppUtil.getNormalImageOptions();
        WindowManager wm = mActivity.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams lp = holder.img_main.getLayoutParams();
        lp.width = width;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        holder.img_main.setLayoutParams(lp);
        holder.img_main.setMaxWidth(width);
        holder.img_main.setMaxHeight(width*data.getStore_info().getStoreImages().get(position).getH()/data.getStore_info().getStoreImages().get(position).getW());
            imageLoader.displayImage(data.getStore_info().getStoreImages().get(position).getImg(),holder.img_main,options);
        }

    @Override
    public int getItemCount() {
        if(data!=null && data.getStore_info()!=null && data.getStore_info().getStoreImages()!=null){
            return data.getStore_info().getStoreImages().size();
        }
        return 0;
    }


    @Override
    public int getItemViewType(int position) {
      return position;
    }

    public void setData(ShopDetailBean response) {
        this.data = response;
        notifyDataSetChanged();
    }
}
