package com.doschool.ahu.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.plattysoft.leonids.ParticleSystem;

/**
 * Created by X on 2018/9/21
 */
public class ParticleAnim {

    public static void getPartic(Context context, View view,int resId){
        ParticleSystem ps = new ParticleSystem((Activity) context, 50, resId, 800);
        ps.setScaleRange(0.7f, 1.3f);
        //setting angle（设置角度，只是上面显示）
        ps.setSpeedModuleAndAngleRange(0.1f, 0.5f, 180, 360);
        ps.setAcceleration(0.0001f, 90);
        ps.setRotationSpeedRange(90, 180);
        ps.setFadeOut(200, new AccelerateInterpolator());
        ps.oneShot(view, 10);
    }
}
