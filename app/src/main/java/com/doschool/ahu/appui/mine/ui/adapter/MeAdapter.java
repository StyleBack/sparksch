package com.doschool.ahu.appui.mine.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVO;
import com.doschool.ahu.appui.mine.ui.holderlogic.MeHolder;
import com.doschool.ahu.base.adapter.BaseRvAdapter;

/**
 * Created by X on 2019/1/12.
 */
public class MeAdapter extends BaseRvAdapter<MicroblogVO.DataBean,MeHolder> {
    public MeAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.item_oth_layout;
    }

    @Override
    protected MeHolder onCreateHolder(ViewGroup parent, int viewType) {
        return MeHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(MeHolder holder, int position, MicroblogVO.DataBean data) {
        if (position==0){
            holder.oth_view.setVisibility(View.VISIBLE);
        }else {
            holder.oth_view.setVisibility(View.GONE);
        }
        holder.setHolder(context,data);
    }
}
