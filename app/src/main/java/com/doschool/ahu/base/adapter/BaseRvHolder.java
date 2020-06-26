package com.doschool.ahu.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by X on 2018/8/23
 *
 * 可继承实现BaseRvHolder，也可直接使用
 */
public class BaseRvHolder extends RecyclerView.ViewHolder {


    // 集合类,layout里包含的View,以view的id作为key,value是view对象
    private SparseArray<View> mViews;

    public BaseRvHolder(View itemView) {
        super(itemView);
        mViews=new SparseArray<>();
    }

    public <V extends View> V findViewById(int viewId){
        View view=mViews.get(viewId);
        if (view==null){
            view=itemView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (V) view;
    }
}
