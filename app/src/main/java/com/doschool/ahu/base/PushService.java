package com.doschool.ahu.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.AppUtils;

/**
 * Created by Zx on date
 */
public class PushService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent intentForPackage = getPackageManager().getLaunchIntentForPackage(AppUtils.getAppPackageName());
        startActivity(intentForPackage);
        return super.onStartCommand(intent, flags, startId);
    }
}
