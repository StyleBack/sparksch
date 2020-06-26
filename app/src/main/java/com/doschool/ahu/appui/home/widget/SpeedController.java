package com.doschool.ahu.appui.home.widget;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by X on 2018/10/17
 */
public class SpeedController extends Scroller {
    private int mDuration = 1200;

    public SpeedController(Context context) {
        super(context);
    }

    public SpeedController(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public SpeedController(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }


    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}
