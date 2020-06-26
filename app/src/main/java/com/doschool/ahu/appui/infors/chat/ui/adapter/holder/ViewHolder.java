package com.doschool.ahu.appui.infors.chat.ui.adapter.holder;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by X on 2018/8/27
 */
public class ViewHolder {

    public ViewHolder() {
    }

    public RelativeLayout leftMessage;
    public RelativeLayout rightMessage;
    public RelativeLayout leftPanel;
    public RelativeLayout rightPanel;
    public ProgressBar sending;
    public ImageView error;
    public TextView sender;
    public TextView systemMessage;
    public TextView rightDesc;
    public ImageView leftAvatar;
    public ImageView rightAvatar;
}
