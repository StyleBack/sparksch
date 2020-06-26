package com.doschool.ahu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.doschool.ahu.R;


public class DotGroup extends LinearLayout {

    private int unchooseResId;
    private int chooseResId;
    private int pageCount;

    public DotGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DotGroup(Context context) {
        super(context);
    }


    public void init(int pageCount, int currentPage, int dotHeight, int unchooseResId, int chooseResId) {
        this.removeAllViews();
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.unchooseResId = unchooseResId;
        this.chooseResId = chooseResId;
        this.pageCount = pageCount;
        for (int i = 0; i < pageCount; i++) {
            ImageView ivDot = new ImageView(getContext());
            ivDot.setPadding(0, 0, dotHeight, 0);
            this.addView(ivDot, dotHeight * 2, dotHeight);
        }
        setCurrentItem(currentPage);
    }

    public void init(int pageCount) {
        init(pageCount, 0, ConvertUtils.dp2px(6), R.drawable.unshape_circle_grey, R.drawable.shape_circle_grey);
    }


    public void setCurrentItem(int cur) {
        for (int i = 0; i < pageCount; i++) {
            ImageView iv = (ImageView) this.getChildAt(i);
            if (i == cur)
                iv.setImageResource(chooseResId);
            else
                iv.setImageResource(unchooseResId);

        }
    }


}