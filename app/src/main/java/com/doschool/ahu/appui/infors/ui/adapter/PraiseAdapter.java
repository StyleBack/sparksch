package com.doschool.ahu.appui.infors.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.infors.ui.bean.PraiseBean;
import com.doschool.ahu.appui.infors.ui.holderlogic.PraiseHolder;
import com.doschool.ahu.base.adapter.BaseRvAdapter;

/**
 * Created by X on 2018/9/22
 */
public class PraiseAdapter extends BaseRvAdapter<PraiseBean.PraData,PraiseHolder> {
    public PraiseAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.item_praise_lay;
    }

    @Override
    protected PraiseHolder onCreateHolder(ViewGroup parent, int viewType) {
        return PraiseHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(PraiseHolder holder, int position, PraiseBean.PraData data) {
            holder.setHolder(context,data);
    }
}
