package com.doschool.ahu.appui.infors.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.infors.ui.bean.PushRecordBean;
import com.doschool.ahu.appui.infors.ui.holderlogic.NotifyAssHolder;
import com.doschool.ahu.base.adapter.BaseRvAdapter;

public class NotifyAssAdapter extends BaseRvAdapter<PushRecordBean.DataBean,NotifyAssHolder> {
    public NotifyAssAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.item_notify_layout;
    }

    @Override
    protected NotifyAssHolder onCreateHolder(ViewGroup parent, int viewType) {
        return NotifyAssHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(NotifyAssHolder holder, int position, PushRecordBean.DataBean data) {
        holder.setNotiDto(context,data);
    }
}
