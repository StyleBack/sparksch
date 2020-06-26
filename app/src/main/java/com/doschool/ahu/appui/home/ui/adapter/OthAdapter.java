package com.doschool.ahu.appui.home.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.home.ui.holderlogic.OthHolder;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVO;
import com.doschool.ahu.base.adapter.BaseRvAdapter;

/**
 * Created by X on 2019/1/10.
 */
public class OthAdapter extends BaseRvAdapter<MicroblogVO.DataBean,OthHolder> {

    public OthAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.item_oth_layout;
    }

    @Override
    protected OthHolder onCreateHolder(ViewGroup parent, int viewType) {
        return OthHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(OthHolder holder, int position, MicroblogVO.DataBean data) {
        if (position==0){
            holder.oth_view.setVisibility(View.VISIBLE);
        }else {
            holder.oth_view.setVisibility(View.GONE);
        }
        holder.setOth(context,data);
    }
}
