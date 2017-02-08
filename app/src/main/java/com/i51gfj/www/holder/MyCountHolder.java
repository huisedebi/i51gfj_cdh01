package com.i51gfj.www.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.i51gfj.www.R;

/**
 * Created by Administrator on 2016/8/12.
 */
public class MyCountHolder extends RecyclerView.ViewHolder {

    public TextView tv_num;
    public TextView tv_content;
    public TextView tv_cz;
    public TextView tv_time;


    public MyCountHolder(View itemView) {
        super(itemView);
        tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        tv_num = (TextView) itemView.findViewById(R.id.tv_num);
        tv_cz = (TextView) itemView.findViewById(R.id.tv_cz);
        tv_time = (TextView) itemView.findViewById(R.id.tv_time);
    }




}
