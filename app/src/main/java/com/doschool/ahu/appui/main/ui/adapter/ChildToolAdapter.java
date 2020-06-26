package com.doschool.ahu.appui.main.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.ToolsConfigDOList;
import com.doschool.ahu.appui.main.ui.holderlogic.ChilderToolHolder;
import com.doschool.ahu.base.adapter.BaseRvAdapter;

/**
 * Created by X on 2019/1/12.
 */
public class ChildToolAdapter extends BaseRvAdapter<ToolsConfigDOList,ChilderToolHolder> {

    public ChildToolAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.item_child_tool;
    }

    @Override
    protected ChilderToolHolder onCreateHolder(ViewGroup parent, int viewType) {
        return ChilderToolHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(ChilderToolHolder holder, int position, ToolsConfigDOList data) {
        holder.setChildHodler(context,data);
    }
}
