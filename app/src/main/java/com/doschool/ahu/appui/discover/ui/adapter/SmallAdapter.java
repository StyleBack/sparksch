package com.doschool.ahu.appui.discover.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.discover.ui.bean.ServiceConfigBean;
import com.doschool.ahu.appui.discover.ui.holderlogic.SmallHolder;
import com.doschool.ahu.base.adapter.BaseRvAdapter;

/**
 * Created by X on 2018/9/23
 */
public class SmallAdapter extends BaseRvAdapter<ServiceConfigBean,SmallHolder> {
    public SmallAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.small_item_lay;
    }

    @Override
    protected SmallHolder onCreateHolder(ViewGroup parent, int viewType) {
        return SmallHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(SmallHolder holder, int position, ServiceConfigBean data) {
        holder.smallHolder(context,data);
    }
}
