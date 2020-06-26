package com.doschool.ahu.appui.writeblog.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.discover.ui.bean.MicroTopic;
import com.doschool.ahu.appui.writeblog.event.OnTicListener;
import com.doschool.ahu.appui.writeblog.ui.holderlogic.CommTicHolder;
import com.doschool.ahu.base.adapter.BaseRvAdapter;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by X on 2018/11/7
 */
public class CommTicAdapter extends BaseRvAdapter<MicroTopic.McData,CommTicHolder> {

    public CommTicAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.item_comtic_lay;
    }

    @Override
    protected CommTicHolder onCreateHolder(ViewGroup parent, int viewType) {
        return CommTicHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void bindData(CommTicHolder holder, int position, MicroTopic.McData data) {
        holder.setTicHolder(data);

        RxView.clicks(holder.tic_lool)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (onTicListener!=null){
                        onTicListener.onTic(data.getMicroblogTopicDO().getTopicName());
                    }
                });
    }

    private OnTicListener onTicListener;

    public void setOnTicListener(OnTicListener onTicListener) {
        this.onTicListener = onTicListener;
    }
}
