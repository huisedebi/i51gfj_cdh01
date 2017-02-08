package com.i51gfj.www.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.i51gfj.www.R;

/**
 * Created by Administrator on 2016/8/12.
 */
public class RedDetailHolder extends RecyclerView.ViewHolder {
    public ImageView img_main;
    public TextView tv_title, tv_money, tv_remark,tv_phone,line;
    public Button bt_lingqu, bt_yilingqu;
    public LinearLayout layout_phone;

    public RedDetailHolder(View itemView) {
        super(itemView);

        img_main = (ImageView) itemView.findViewById(R.id.img_main);
        tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        line = (TextView) itemView.findViewById(R.id.line);
        tv_remark = (TextView) itemView.findViewById(R.id.tv_remark);
        tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
        tv_money = (TextView) itemView.findViewById(R.id.tv_money);
        bt_lingqu = (Button) itemView.findViewById(R.id.bt_lingqu);
        bt_yilingqu = (Button) itemView.findViewById(R.id.bt_yilingqu);
        layout_phone = (LinearLayout) itemView.findViewById(R.id.layout_phone);

    }
}
