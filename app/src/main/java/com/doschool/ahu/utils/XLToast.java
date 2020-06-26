package com.doschool.ahu.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.doschool.ahu.base.BaseApplication;

/**
 * Created by X on 2018/7/4.
 *
 * Toast简单封装  调用showToast
 */
public class XLToast {
    private static Toast toast;

    public static Handler mHandler = new Handler(Looper.getMainLooper());

    public static void showToast(String text)
    {
        showToast(text, Toast.LENGTH_SHORT);
    }

    public static void showToast(final String text, final int duration)
    {
        if (Looper.myLooper() == Looper.getMainLooper())
        {
            show(text, duration);
        } else
        {
            mHandler.post(() -> show(text, duration));
        }
    }

    private static void show(String text, int duration)
    {
        if (TextUtils.isEmpty(text))
        {
            return;
        }
        if (toast != null)
        {
            toast.cancel();
        }
        toast = Toast.makeText(BaseApplication.getInstance(), text, duration);
        toast.show();
    }
}
