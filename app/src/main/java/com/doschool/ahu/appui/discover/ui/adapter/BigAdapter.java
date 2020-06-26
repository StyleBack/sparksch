package com.doschool.ahu.appui.discover.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.discover.ui.bean.ServiceConfigBean;
import com.doschool.ahu.appui.discover.ui.holderlogic.BigHolder;
import com.doschool.ahu.base.adapter.BaseRvAdapter;

/**
 * Created by X on 2018/9/25
 */
public class BigAdapter extends BaseRvAdapter<ServiceConfigBean,BigHolder> {
    public BigAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.big_item_lay;
    }

    @Override
    protected BigHolder onCreateHolder(ViewGroup parent, int viewType) {
        return BigHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(BigHolder holder, int position, ServiceConfigBean data) {
        holder.bigholder(context,data);
    }
}
