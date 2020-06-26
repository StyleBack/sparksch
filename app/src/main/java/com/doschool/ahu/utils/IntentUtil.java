package com.doschool.ahu.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.doschool.ahu.base.BaseApplication;

/**
 * Created by X on 2018/9/13
 */
public class IntentUtil {

    public static void toActivity(Context context, Bundle bundle, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        if (context instanceof BaseApplication || context instanceof Service) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);

    }
}
