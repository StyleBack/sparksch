package com.doschool.ahu.appui.mine.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.mine.ui.bean.ToolService;
import com.doschool.ahu.appui.mine.ui.holderlogic.ServiceHolder;
import com.doschool.ahu.base.adapter.BaseRvAdapter;

/**
 * Created by X on 2018/9/11
 */
public class ServiceAdapter extends BaseRvAdapter<ToolService.ToolBean,ServiceHolder> {


    public ServiceAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.service_item_layout;
    }

    @Override
    protected ServiceHolder onCreateHolder(ViewGroup parent, int viewType) {
        return ServiceHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(ServiceHolder holder, int position, ToolService.ToolBean data) {
        holder.setTool(context,position,data);
    }
}
