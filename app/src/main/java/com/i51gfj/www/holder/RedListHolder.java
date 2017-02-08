package com.i51gfj.www.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.i51gfj.www.R;
import com.youth.banner.Banner;

/**
 * Created by Administrator on 2016/8/12.
 */
public class RedListHolder extends RecyclerView.ViewHolder {

    public ImageView ic_frash,img_juli,img_good,img_start;
    public TextView red_num,tv_good;
    public TextView name;
    public TextView distent;
    public TextView tv_content;
    public TextView collect;
    public ImageView big_img,img_star;
    public Button red_button;
    public LinearLayout layout_lin,layout_collect;
    public RelativeLayout layout_content;
    public Banner banner;


    public RedListHolder(View itemView, int viewType) {
        super(itemView);
        if (viewType == 0) {
            red_num = (TextView) itemView.findViewById(R.id.item_red_one_tv);
            ic_frash = (ImageView) itemView.findViewById(R.id.item_red_ic_frash);
            banner = (Banner) itemView.findViewById(R.id.banner);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        } else {
            name = (TextView) itemView.findViewById(R.id.item_red_two_tv1);
            distent = (TextView) itemView.findViewById(R.id.item_shop_distance);
            collect = (TextView) itemView.findViewById(R.id.item_shop_collect);
            tv_good = (TextView) itemView.findViewById(R.id.tv_good);
            big_img = (ImageView) itemView.findViewById(R.id.item_red_two_img);
            img_juli = (ImageView) itemView.findViewById(R.id.img_juli);
            img_good = (ImageView) itemView.findViewById(R.id.img_good);
            img_start = (ImageView) itemView.findViewById(R.id.img_start);
            red_button = (Button) itemView.findViewById(R.id.red_button);
            layout_lin = (LinearLayout) itemView.findViewById(R.id.layout_lin);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            layout_content= (RelativeLayout) itemView.findViewById(R.id.layout_content);
            img_star = (ImageView) itemView.findViewById(R.id.img_star);
            layout_collect = (LinearLayout) itemView.findViewById(R.id.layout_collect);
        }
    }
}
