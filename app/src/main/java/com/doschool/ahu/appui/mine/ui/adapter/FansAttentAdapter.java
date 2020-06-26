package com.doschool.ahu.appui.mine.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.SingleUserVO;
import com.doschool.ahu.appui.mine.ui.holderlogic.FansAttentHolder;
import com.doschool.ahu.appui.writeblog.ui.bean.AtBean;
import com.doschool.ahu.base.adapter.BaseRvAdapter;

/**
 * Created by X on 2018/9/24
 */
public class FansAttentAdapter extends BaseRvAdapter<SingleUserVO,FansAttentHolder> {

    public FansAttentAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.fans_attent_layout;
    }

    @Override
    protected FansAttentHolder onCreateHolder(ViewGroup parent, int viewType) {
        return FansAttentHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(FansAttentHolder holder, int position, SingleUserVO data) {
        holder.fansHolder(context,data);
    }
}
