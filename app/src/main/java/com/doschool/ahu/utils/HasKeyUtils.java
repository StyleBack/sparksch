package com.doschool.ahu.utils;

import android.content.Context;
import android.content.res.Resources;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;


/**
 * Created by X on 2018/11/13
 */
public class HasKeyUtils {

    /**
     * 获取是否虚拟键
     * 通过判断是否有物理返回键反向判断是否有虚拟键
     * @param context
     * @return
     */
    public static boolean chaeckDeviceNavBar(Context context){
        boolean hasMenuKey= ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean hasBacKey= KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey && !hasBacKey){
            //有导航栏
            return true;
        }
        return false;
    }

    //获取虚拟键的高度
    public static int getNavgationBarHeight(Context context){
        int result=0;
        if (chaeckDeviceNavBar(context)){
            Resources resources=context.getResources();
            int resourceId=resources.getIdentifier("navigation_bar_height","dimen","android");
            if (resourceId>0){
                result=resources.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

}
