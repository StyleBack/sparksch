package com.doschool.ahu.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;

/**
 * Created by X on 2018/9/25
 */
public class RvGridDecoration extends RecyclerView.ItemDecoration {

    private int margin;

    public RvGridDecoration(float value) {

        margin = ConvertUtils.dp2px(value);

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //由于每行都只有2个，所以第一个都是2的倍数，把左边距设为0
        if (parent.getChildLayoutPosition(view)%2==0){
            outRect.set(margin,0,margin,0);
        }else {
            outRect.set(0,0,margin,0);
        }

    }
}
