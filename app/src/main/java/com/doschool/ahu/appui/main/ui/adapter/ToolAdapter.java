package com.doschool.ahu.appui.main.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.ToolsBean;
import com.doschool.ahu.appui.main.ui.holderlogic.ToolHolder;
import com.doschool.ahu.base.adapter.BaseRvAdapter;

/**
 * Created by X on 2019/1/12.
 */
public class ToolAdapter extends BaseRvAdapter<ToolsBean.Tools,ToolHolder> {

    public ToolAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.item_tool_layout;
    }

    @Override
    protected ToolHolder onCreateHolder(ViewGroup parent, int viewType) {
        return ToolHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(ToolHolder holder, int position, ToolsBean.Tools data) {
        holder.setTool(context,data);
    }
}
