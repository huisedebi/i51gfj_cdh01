package com.i51gfj.www.view.danmu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/7/26.
 */
public class BarrageView extends TextView {
    private Paint paint = new Paint(); //画布参数
    public BarrageView(Context context) {
        super(context);
    }

    public BarrageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /**
     * 初始化
     */
    protected void init() {}

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setTextSize(30);
        paint.setColor(0xffffffff); //白色
        //canvas.drawText(getText(), 0, 30, paint);
    }
}
