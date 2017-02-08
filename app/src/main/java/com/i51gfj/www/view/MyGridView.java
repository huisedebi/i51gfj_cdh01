package com.i51gfj.www.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 可以在SrollView中嵌套的ListView
 */
public class MyGridView extends GridView {

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    public void SetHeight(int totalHeight) {
//        ViewGroup.LayoutParams params = getLayoutParams();
//        params.height = totalHeight;
//        setLayoutParams(params);
//    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
