package com.doschool.ahu.appui.qrcode.manager;

import android.app.Activity;
import android.content.DialogInterface;

/**
 * Created by X on 2019/2/28.
 * 用于在少数情况下退出App的监听
 */
public final class FinishListener implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {

    private final Activity activityToFinish;

    public FinishListener(Activity activityToFinish) {
        this.activityToFinish = activityToFinish;
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        run();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        run();
    }

    private void run() {
        activityToFinish.finish();
    }

}
