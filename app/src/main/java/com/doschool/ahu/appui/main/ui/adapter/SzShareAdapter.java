package com.doschool.ahu.appui.main.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVoteOptionsDto;
import com.doschool.ahu.appui.main.ui.holderlogic.SzShareHolder;
import com.doschool.ahu.base.adapter.BaseRvAdapter;

/**
 * Created by X on 2019/1/22.
 */
public class SzShareAdapter extends BaseRvAdapter<MicroblogVoteOptionsDto,SzShareHolder> {


    public SzShareAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.item_szshare_layout;
    }

    @Override
    protected SzShareHolder onCreateHolder(ViewGroup parent, int viewType) {
        return SzShareHolder.nesInstance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(SzShareHolder holder, int position, MicroblogVoteOptionsDto data) {
        if (position==0){
            holder.sz_winiv.setVisibility(View.VISIBLE);
        }else {
            holder.sz_winiv.setVisibility(View.GONE);
        }
        holder.setSzHol(context,data);
        holder.sz_rlmat.setOnClickListener(view -> {
            if (onItemDis!=null){
                onItemDis.onDismiss();
            }
        });
    }

    private RmShareAdapter.OnItemDis onItemDis;

    public void setOnItemDis(RmShareAdapter.OnItemDis onItemDis) {
        this.onItemDis = onItemDis;
    }

    public interface OnItemDis{
        void onDismiss();
    }
}
