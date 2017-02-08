package com.i51gfj.www.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.i51gfj.www.R;

/**
 * Created by Administrator on 2016/8/12.
 */
public class ShopHolder extends RecyclerView.ViewHolder {

    public ImageView bigImg,img_star;
    public TextView tv1;
    public TextView tv2;
    public TextView tv3;
    public TextView tv4,tv_cancel;
    public LinearLayout layout_content;

    public ShopHolder(View itemView) {
        super(itemView);
        bigImg = (ImageView) itemView.findViewById(R.id.item_shop_one_img);
        tv1 = (TextView) itemView.findViewById(R.id.item_shop_one_tv1);
        tv2 = (TextView) itemView.findViewById(R.id.item_shop_one_tv2);
        tv3 = (TextView) itemView.findViewById(R.id.item_shop_collect);
        tv4 = (TextView) itemView.findViewById(R.id.item_shop_distance);
        layout_content = (LinearLayout) itemView.findViewById(R.id.layout_content);
        img_star = (ImageView) itemView.findViewById(R.id.img_star);
        tv_cancel = (TextView) itemView.findViewById(R.id.tv_cancel);
    }
}
