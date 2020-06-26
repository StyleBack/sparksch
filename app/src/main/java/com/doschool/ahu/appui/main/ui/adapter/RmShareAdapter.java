package com.doschool.ahu.appui.main.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.MicroblogCommentVO;
import com.doschool.ahu.appui.main.ui.holderlogic.RmShareHolder;
import com.doschool.ahu.base.adapter.BaseRvAdapter;

/**
 * Created by X on 2019/1/22.
 */
public class RmShareAdapter extends BaseRvAdapter<MicroblogCommentVO,RmShareHolder> {

    public RmShareAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.item_rmshare_layout;
    }

    @Override
    protected RmShareHolder onCreateHolder(ViewGroup parent, int viewType) {
        return RmShareHolder.newIsntance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(RmShareHolder holder, int position, MicroblogCommentVO data) {
        holder.setRmhol(context,data);
        holder.rm_ll.setOnClickListener(view -> {
            if (onItemDis!=null) {
                onItemDis.onDismiss();
            }
        });
    }

    private OnItemDis onItemDis;

    public void setOnItemDis(OnItemDis onItemDis) {
        this.onItemDis = onItemDis;
    }

    public interface OnItemDis{
        void onDismiss();
    }
}
