package com.doschool.ahu.appui.mine.ui.activity;

import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.base.BaseActivity;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by Zx on date
 */
public class MessageNotifyActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;
    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;
    @ViewInject(R.id.tv_message_state)
    private TextView tvMessageState;

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_message_notify_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        tool_back_iv.setVisibility(View.VISIBLE);
        tool_title_tv.setVisibility(View.VISIBLE);
        tool_title_tv.setText("通知设置");
        tool_back_iv.setOnClickListener(this::onClick);
        NotificationManagerCompat notification = NotificationManagerCompat.from(this);
        boolean isEnabled = notification.areNotificationsEnabled();
        if (isEnabled) {
            tvMessageState.setText("已开启");
        } else {
            tvMessageState.setText("已关闭");
        }
    }

    @Override
    protected void DetoryViewAndThing() {

    }

    @Override
    public void onClick(View v) {
        if (v.equals(tool_back_iv)) {
            finish();
        }
    }
}
