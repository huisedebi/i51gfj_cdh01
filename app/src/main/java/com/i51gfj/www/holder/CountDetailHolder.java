package com.i51gfj.www.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.i51gfj.www.R;

/**
 * Created by Administrator on 2016/8/12.
 */
public class CountDetailHolder extends RecyclerView.ViewHolder {

    public TextView tv_count,tv_intro,tv_time;

    public CountDetailHolder(View itemView) {
        super(itemView);

        tv_count = (TextView) itemView.findViewById(R.id.tv_count);
        tv_intro = (TextView) itemView.findViewById(R.id.tv_intro);
        tv_time = (TextView) itemView.findViewById(R.id.tv_time);


    }
}
