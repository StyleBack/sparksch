package com.doschool.ahu.appui.main.event;

import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.doschool.ahu.R;
import com.doschool.ahu.base.BaseApplication;

/**
 * Created by X on 2018/11/28.
 */
public class TxtClickSpan extends ClickableSpan {

    public TxtClickSpan(OnSpanClick onSpanClick) {
        this.onSpanClick=onSpanClick;
    }

    @Override
    public void onClick( View widget) {
        if (onSpanClick!=null){
            onSpanClick.onSpanClick(widget);
        }
    }

    @Override
    public void updateDrawState( TextPaint ds) {
        ds.setColor(BaseApplication.getContext().getResources().getColor(R.color.now_txt_color));
    }

    private OnSpanClick onSpanClick;

    public interface OnSpanClick{
        void onSpanClick(View widget);
    }
}
