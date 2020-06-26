package com.doschool.ahu.appui.home.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.AppBannerDo;

/**
 * Created by X on 2018/10/17
 * 话题banner的假输入框
 */
public class FakeEditTextWithIcon extends FrameLayout {

    private AppCompatEditText editText;
    private ImageView imageView;

    public FakeEditTextWithIcon(Context context) {
        this(context,null);
    }

    public FakeEditTextWithIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_topic_box, this);
        editText = (AppCompatEditText) findViewById(R.id.topicBox_edittext);
        imageView = (ImageView) findViewById(R.id.topicBox_chooseImage);
    }

    public void setHint(String hint){
        editText.setHint(hint);
    }

    public void updateUI(AppBannerDo.BannerData bannerData) {

        editText.setHint("参与话题#" + bannerData.getTopicName() + "#");
        editText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果是游客，弹Dialog提示不能发动态

            }
        });
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果是游客，弹Dialog提示不能发动态

            }
        });
    }

}
