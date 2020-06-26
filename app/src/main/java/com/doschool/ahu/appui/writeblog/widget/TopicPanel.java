package com.doschool.ahu.appui.writeblog.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.doschool.ahu.R;

/**
 * Created by X on 2018/9/18
 *
 * 常用话题
 */
public class TopicPanel extends FrameLayout {

    private WriteKeybordBox.WrCallBack callback;

    private LinearLayout love, lost, advice, market, question, report, joke, work;

    public TopicPanel(@NonNull Context context) {
        this(context,null);
    }

    public TopicPanel(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TopicPanel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.cpnt_blogwrite_topicgroup, this);
        love = (LinearLayout) findViewById(R.id.topic_love);
        lost = (LinearLayout) findViewById(R.id.topic_lost);
        advice = (LinearLayout) findViewById(R.id.topic_advice);
        market = (LinearLayout) findViewById(R.id.topic_market);
        question = (LinearLayout) findViewById(R.id.topic_question);
        report = (LinearLayout) findViewById(R.id.topic_report);
        joke = (LinearLayout) findViewById(R.id.topic_joke);
        work = (LinearLayout) findViewById(R.id.topic_work);

        love.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onTopicClick("表白墙");
            }
        });
        lost.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onTopicClick("失物招领");
            }
        });
        advice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onTopicClick("意见建议");
            }
        });
        market.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onTopicClick("闲置出售");
            }
        });
        question.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onTopicClick("有问必答");
            }
        });
        report.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onTopicClick("我在现场");
            }
        });
        joke.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onTopicClick("段子专区");
            }
        });
        work.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onTopicClick("兼职实习");
            }
        });
    }

    public void setCallBack(WriteKeybordBox.WrCallBack callback) {
        this.callback = callback;
    }

}

