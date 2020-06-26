package com.doschool.ahu.utils;

import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import com.doschool.ahu.base.BaseApplication;

/**
 * Created by X on 2019/2/13.
 */
public class UIHelper {

    public static int getColor(@ColorRes int colorResId) {
        try {
            return ContextCompat.getColor(BaseApplication.getContext(), colorResId);
        } catch (Exception e) {
            return Color.TRANSPARENT;
        }
    }
}
