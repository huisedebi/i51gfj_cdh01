package com.i51gfj.www.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.i51gfj.www.R;

/**
 * Created by Administrator on 2016/8/12.
 */
public class RedRecordHolder extends RecyclerView.ViewHolder {

    public ImageView ic_frash;
    public TextView red_num;
    public TextView name;
    public TextView distent;
    public TextView collect;
    public ImageView big_img;
    public Button red_button;


    public RedRecordHolder(View itemView, int viewType) {
        super(itemView);
        if (viewType == 0) {
            red_num = (TextView) itemView.findViewById(R.id.item_red_one_tv);
            ic_frash = (ImageView) itemView.findViewById(R.id.item_red_ic_frash);
        } else {
            name = (TextView) itemView.findViewById(R.id.item_red_two_tv1);
            distent = (TextView) itemView.findViewById(R.id.item_shop_distance);
            collect = (TextView) itemView.findViewById(R.id.item_shop_collect);
            big_img = (ImageView) itemView.findViewById(R.id.item_red_two_img);
            red_button = (Button) itemView.findViewById(R.id.red_button);
        }
    }
}
