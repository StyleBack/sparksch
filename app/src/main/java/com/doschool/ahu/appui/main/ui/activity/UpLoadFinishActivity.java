package com.doschool.ahu.appui.main.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.event.XMessageEvent;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.utils.EventUtils;
import com.jakewharton.rxbinding2.view.RxView;

import org.xutils.view.annotation.ViewInject;

import java.util.concurrent.TimeUnit;

import static com.doschool.ahu.appui.main.ui.activity.ChangePersonalActivity.CHANGE_CODE;


/**
 * Created by X on 2018/10/25
 */
public class UpLoadFinishActivity extends BaseActivity {

    @ViewInject(R.id.fin_tvok)
    private TextView fin_tvok;

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_upload_finish;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        clicks();
    }

    @SuppressLint("CheckResult")
    private void clicks(){
        RxView.clicks(fin_tvok)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    EventUtils.onPost(new XMessageEvent(CHANGE_CODE));
                    ActivityUtils.finishActivity(UpLoadValidationActivity.class);
                    finish();
                });
    }

    @Override
    protected void DetoryViewAndThing() {

    }
}
