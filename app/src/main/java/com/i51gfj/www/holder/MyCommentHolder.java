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
public class MyCommentHolder extends RecyclerView.ViewHolder {
    public TextView tv_name;
    public TextView tv_time;
    public TextView tv_content;
    public ImageView start;

    public LinearLayout layout_name;

    public MyCommentHolder(View itemView) {
        super(itemView);

        tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        start = (ImageView) itemView.findViewById(R.id.start);
        layout_name = (LinearLayout) itemView.findViewById(R.id.lyout_name);

    }
}
