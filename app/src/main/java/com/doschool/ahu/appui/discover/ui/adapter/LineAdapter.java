package com.doschool.ahu.appui.discover.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.discover.ui.bean.LineBean;
import com.doschool.ahu.appui.discover.ui.holderlogic.LineHolder;
import com.doschool.ahu.base.adapter.BaseRvAdapter;

import java.util.Random;

/**
 * Created by X on 2018/9/27
 */
public class LineAdapter extends BaseRvAdapter<LineBean.LineDt,LineHolder> {

    private int ranType;

    public LineAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        if (viewType%2==0){
            return R.layout.line_item_lay1;
        }else {
            return R.layout.line_item_lay2;
        }
    }

    @Override
    protected LineHolder onCreateHolder(ViewGroup parent, int viewType) {
        return LineHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(LineHolder holder, int position, LineBean.LineDt data) {
        holder.lineHolder(context,data,ranType);
    }

    @Override
    public int getItemViewType(int position) {
        ranType=position;
        return ranType;
    }
}
