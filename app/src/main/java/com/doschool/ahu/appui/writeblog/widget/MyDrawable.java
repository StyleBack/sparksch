package com.doschool.ahu.appui.writeblog.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.blankj.utilcode.util.ConvertUtils;

/**
 * Created by X on 2018/9/18
 */
public class MyDrawable extends Drawable {

    private Paint mPaint;

    private String name;
    private int userId;

    public MyDrawable(String name, int userId) {
        this.name = name;
        this.userId = userId;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.argb(255, 115, 199, 234));
        mPaint.setTextSize(ConvertUtils.dp2px(16));
    }

    @Override
    public void draw(Canvas canvas) {
        int y = ConvertUtils.sp2px(16);
        canvas.drawText(name, 0, y, mPaint);
    }

    /**
     * 计算文字宽度
     * @param paint
     * @param str
     * @return
     */
    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {

        super.setBounds(0, 0, getTextWidth(mPaint, name), ConvertUtils.sp2px(21));

    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


}

