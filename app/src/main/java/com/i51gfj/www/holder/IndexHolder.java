package com.i51gfj.www.holder;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.i51gfj.www.R;
import com.i51gfj.www.banner.ImageCycleView;
import com.i51gfj.www.fragment.BaseFragment;
import com.youth.banner.Banner;

/**
 * Created by Administrator on 2016/8/12.
 */
public class IndexHolder extends RecyclerView.ViewHolder {

    public TextView collect;
    public TextView distance;
    public TextView title;
    public TextView address;
    public ImageView big,img_star;
    public RelativeLayout rlout;
    public ViewPager viewPager;
    public LinearLayout dot_layout;
    public LinearLayout shop_layout;
    public ImageView img_frash;
    public TextView tv_where;
    public Banner banner;
    public View bg;





    public IndexHolder(View itemView, BaseFragment fragment, int viewType) {
        super(itemView);
        if(viewType==0){
            //fragment.banner_view = (ImageCycleView) itemView.findViewById(R.id.banner_view);
            banner = (Banner) itemView.findViewById(R.id.banner);
        }else if(viewType==1){
            viewPager = (ViewPager) itemView.findViewById(R.id.index_two_viewpager);
            dot_layout = (LinearLayout) itemView.findViewById(R.id.dot_layout);
        }else{
            collect = (TextView) itemView.findViewById(R.id.index_three_collect);
            distance = (TextView) itemView.findViewById(R.id.index_three_distance);
            big = (ImageView) itemView.findViewById(R.id.index_three_big);
            rlout = (RelativeLayout) itemView.findViewById(R.id.item_list_index_rl);
            title = (TextView) itemView.findViewById(R.id.index_three_title);
            shop_layout = (LinearLayout) itemView.findViewById(R.id.shop_layout);
            address = (TextView) itemView.findViewById(R.id.index_three_address);
            img_frash = (ImageView) itemView.findViewById(R.id.img_frash);
            tv_where = (TextView) itemView.findViewById(R.id.tv_where);
            img_star = (ImageView) itemView.findViewById(R.id.img_star);
            bg = itemView.findViewById(R.id.background);
        }

    }
}
