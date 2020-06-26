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


/**
 * Created by X on 2018/10/23
 *
 * 手机号验证成功
 */
public class BindSuccActivity extends BaseActivity {


    public static final int BIND_SUCC=10;

    @ViewInject(R.id.bind_suctv)
    private TextView bind_suctv;

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_bindsucc_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        click();
    }

    @SuppressLint("CheckResult")
    private void click(){
        RxView.clicks(bind_suctv)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    EventUtils.onPost(new XMessageEvent(BIND_SUCC));
                    ActivityUtils.finishActivity(BindPhoneActivity.class);
                    finish();
                });
    }

    @Override
    protected void DetoryViewAndThing() {

    }
}
