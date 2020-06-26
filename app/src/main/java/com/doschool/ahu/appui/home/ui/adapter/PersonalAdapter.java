package com.doschool.ahu.appui.home.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.home.ui.holderlogic.PersonalHolder;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVO;
import com.doschool.ahu.base.adapter.BaseRvAdapter;

import java.util.List;

/**
 * Created by X on 2018/9/12
 */
public class PersonalAdapter extends BaseRvAdapter<MicroblogVO.DataBean,PersonalHolder> {


    public PersonalAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.per_item_layout;
    }

    @Override
    protected PersonalHolder onCreateHolder(ViewGroup parent, int viewType) {
        return PersonalHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(PersonalHolder holder, int position, MicroblogVO.DataBean data) {
        holder.setPPData(context,data);
    }
}
