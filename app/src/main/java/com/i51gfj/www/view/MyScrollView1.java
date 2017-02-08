package com.i51gfj.www.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2016/5/16.
 */
public class MyScrollView1 extends ScrollView {
    private OnScrollChangedListener listener;

    public interface OnScrollChangedListener{
        void onScrollChanged(View v, int scrollTop);
    }

    public MyScrollView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        // TODO Auto-generated method stub
        super.onScrollChanged(l, t, oldl, oldt);
        listener.onScrollChanged(this, t);
    }

    public void setOnScrollChangedListener(OnScrollChangedListener l){
        this.listener = l;
    }

}
