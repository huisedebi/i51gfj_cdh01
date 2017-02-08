package com.i51gfj.www.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.i51gfj.www.R;
import com.i51gfj.www.activity.ShopDetailCommentActivity;
import com.i51gfj.www.holder.IndexHolder;
import com.i51gfj.www.holder.ShopDetailCommentHolder;
import com.i51gfj.www.model.ShopDetailCommentWrapper;
import com.i51gfj.www.util.AppUtil;
import com.i51gfj.www.util.PhoneUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


/**
 */
public class ShopDetailCommentAdapter extends RecyclerView.Adapter<ShopDetailCommentHolder>{
    private ShopDetailCommentWrapper data;
    ShopDetailCommentActivity mActivity;

    public ShopDetailCommentAdapter(ShopDetailCommentWrapper response,ShopDetailCommentActivity activity) {
        data = response;
       this.mActivity =activity;
    }

    @Override
    public ShopDetailCommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        View view = null;
        switch (viewType) {
            case 0:
                view = layoutInflater.inflate(R.layout.item_activity_comment_one, parent, false);
                break;
            default:
                view = layoutInflater.inflate(R.layout.item_activity_comment_two,parent,false);
                break;
        }
        return new ShopDetailCommentHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(ShopDetailCommentHolder holder, int position) {
           if(position ==0){
               holder.pro1.setProgress(Integer.valueOf(data.getStar_dp_width_1().indexOf(".")==-1?data.getStar_dp_width_1():data.getStar_dp_width_1().substring(0, data.getStar_dp_width_1().indexOf("."))));
               holder.tv_num1.setText(data.getStar_dp_width_1());
               holder.pro2.setProgress(Integer.valueOf(data.getStar_dp_width_2().indexOf(".")==-1?data.getStar_dp_width_2():data.getStar_dp_width_2().substring(0, data.getStar_dp_width_2().indexOf("."))));
               holder.tv_num2.setText(data.getStar_dp_width_2());
               holder.pro3.setProgress(Integer.valueOf(data.getStar_dp_width_3().indexOf(".")==-1?data.getStar_dp_width_3():data.getStar_dp_width_3().substring(0, data.getStar_dp_width_3().indexOf("."))));
               holder.tv_num3.setText(data.getStar_dp_width_3());
               holder.pro4.setProgress(Integer.valueOf(data.getStar_dp_width_4().indexOf(".")==-1?data.getStar_dp_width_4():data.getStar_dp_width_4().substring(0, data.getStar_dp_width_4().indexOf("."))));
               holder.tv_num4.setText(data.getStar_dp_width_4());
               holder.pro5.setProgress(Integer.valueOf(data.getStar_dp_width_5().indexOf(".")==-1?data.getStar_dp_width_5():data.getStar_dp_width_5().substring(0, data.getStar_dp_width_5().indexOf("."))));
               holder.tv_num5.setText(data.getStar_dp_width_5());
               holder.tv_point.setText(""+Float.valueOf(data.getBuy_dp_avg()));
               holder.tv_startnum.setText(data.getBuy_dp_sum());
           }else {
               ImageLoader imageLoader = ImageLoader.getInstance();
               DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisc(true).showImageOnLoading(R.drawable.img_loading)
                       .showImageForEmptyUri(R.drawable.img_loading)
                       .imageScaleType(ImageScaleType.EXACTLY)
                       .showImageOnFail(R.drawable.img_loading).imageScaleType(ImageScaleType.EXACTLY)
                       .displayer(new RoundedBitmapDisplayer(PhoneUtils.dipToPixels(40), 0))
                       .bitmapConfig(Bitmap.Config.RGB_565).build();
               int temp = position-1;
               holder.tv_time.setText(data.getData().get(temp).getCreate_time());
               holder.tv_content.setText(data.getData().get(temp).getContent());
               imageLoader.displayImage(data.getData().get(temp).getImg(), holder.img_person, options);
               holder.tv_name.setText(data.getData().get(temp).getUser_name());
           }
    }

    @Override
    public int getItemViewType(int position) {
       return position;
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.getData().size()+1;
    }
}
