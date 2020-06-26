package com.doschool.ahu.appui.mine.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.mine.ui.bean.MineTopicBean;
import com.doschool.ahu.appui.mine.ui.holderlogic.JoinHolder;
import com.doschool.ahu.base.adapter.BaseRvAdapter;

/**
 * Created by X on 2018/9/25
 */
public class JoinAdapter extends BaseRvAdapter<MineTopicBean.TD,JoinHolder> {
    public JoinAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.join_item_lay;
    }

    @Override
    protected JoinHolder onCreateHolder(ViewGroup parent, int viewType) {
        return JoinHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(JoinHolder holder, int position, MineTopicBean.TD data) {
        holder.joinHolder(context,data);
    }
}
