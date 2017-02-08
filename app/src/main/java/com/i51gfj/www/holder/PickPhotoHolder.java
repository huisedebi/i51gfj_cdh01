package com.i51gfj.www.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.i51gfj.www.R;

/**
 * Created by Administrator on 2016/8/12.
 */
public class PickPhotoHolder extends RecyclerView.ViewHolder {
    public ImageView img_main;

    public PickPhotoHolder(View itemView) {
        super(itemView);
        img_main = (ImageView) itemView.findViewById(R.id.img_main);
    }
}
