package com.doschool.ahu.widget.component;

import android.os.Handler;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by X on 2018/9/14
 */
public abstract class TouchableBaseSpan extends ClickableSpan {

    public boolean touched = false;

    /**
     * This TouchableSpan has been clicked.
     * @param widget TextView containing the touchable span
     */
    @Override
    public void onClick(View widget) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TouchableMovementMethod.touched = false;
            }
        }, 500);
    }

    /**
     * This TouchableSpan has been long clicked.
     * @param widget TextView containing the touchable span
     */
    public void onLongClick(View widget) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TouchableMovementMethod.touched = false;
            }
        }, 500);
    }

    public boolean isTouched() {
        return touched;
    }

    /**
     * Specifiy whether or not the link is currently touched
     * @param touched
     */
    public void setTouched(boolean touched) {
        this.touched = touched;
    }
}

