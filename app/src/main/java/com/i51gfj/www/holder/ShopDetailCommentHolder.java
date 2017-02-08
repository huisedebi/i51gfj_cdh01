package com.i51gfj.www.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.i51gfj.www.R;

/**
 * Created by Administrator on 2016/8/12.
 */
public class ShopDetailCommentHolder extends RecyclerView.ViewHolder {
    public ProgressBar pro1,pro2,pro3,pro4,pro5;
    public TextView tv_num1,tv_num2,tv_num3,tv_num4,tv_num5;
    public TextView tv_time,tv_content,tv_point,tv_name,tv_startnum;
    public ImageView img_person,img_start;



    public ShopDetailCommentHolder(View itemView,int type) {
        super(itemView);
        if(type == 0){
            pro1 = (ProgressBar) itemView.findViewById(R.id.pro1);
            pro2 = (ProgressBar) itemView.findViewById(R.id.pro2);
            pro3 = (ProgressBar) itemView.findViewById(R.id.pro3);
            pro4 = (ProgressBar) itemView.findViewById(R.id.pro4);
            pro5 = (ProgressBar) itemView.findViewById(R.id.pro5);
            tv_num1 = (TextView) itemView.findViewById(R.id.tv_num1);
            tv_num2 = (TextView) itemView.findViewById(R.id.tv_num2);
            tv_num3 = (TextView) itemView.findViewById(R.id.tv_num3);
            tv_num4 = (TextView) itemView.findViewById(R.id.tv_num4);
            tv_num5 = (TextView) itemView.findViewById(R.id.tv_num5);
            tv_point = (TextView) itemView.findViewById(R.id.tv_point);
            tv_startnum = (TextView) itemView.findViewById(R.id.tv_startnum);
            img_start = (ImageView) itemView.findViewById(R.id.img_start);
        }else{
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            img_person = (ImageView) itemView.findViewById(R.id.img_person);
        }
    }
}
