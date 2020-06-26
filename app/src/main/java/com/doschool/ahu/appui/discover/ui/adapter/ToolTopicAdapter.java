package com.doschool.ahu.appui.discover.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.discover.ui.bean.MicroTopic;
import com.doschool.ahu.appui.discover.ui.holderlogic.ToolTopicHolder;
import com.doschool.ahu.base.adapter.BaseRvAdapter;

/**
 * Created by X on 2018/9/24
 *
 * 热门话题卡片
 */
public class ToolTopicAdapter extends BaseRvAdapter<MicroTopic.McData,ToolTopicHolder> {
    public ToolTopicAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.tool_topic_item;
    }

    @Override
    protected ToolTopicHolder onCreateHolder(ViewGroup parent, int viewType) {
        return ToolTopicHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(ToolTopicHolder holder, int position, MicroTopic.McData data) {
        holder.toolHolder(context,data);
    }
}
