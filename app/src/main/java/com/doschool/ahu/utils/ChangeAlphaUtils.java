package com.doschool.ahu.utils;

import android.graphics.Color;

/**
 * Created by X on 2019/1/9.
 */
public class ChangeAlphaUtils {

    /** 根据百分比改变颜色透明度 */
    public static int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }
}
