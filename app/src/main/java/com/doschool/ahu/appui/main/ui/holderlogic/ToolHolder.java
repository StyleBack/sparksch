package com.doschool.ahu.appui.main.ui.holderlogic;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.adapter.ChildToolAdapter;
import com.doschool.ahu.appui.main.ui.bean.ToolsBean;
import com.doschool.ahu.base.adapter.BaseRvHolder;

/**
 * Created by X on 2019/1/12.
 */
public class ToolHolder extends BaseRvHolder {

    public TextView item_name;
    public RecyclerView child_rv;
    private GridLayoutManager grid;
    private ChildToolAdapter childToolAdapter;
    public ToolHolder(View itemView) {
        super(itemView);
        item_name=itemView.findViewById(R.id.item_name);
        child_rv=itemView.findViewById(R.id.child_rv);
    }

    public static ToolHolder newInstance(ViewGroup parent,int layoutId){
        View view=LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        return new ToolHolder(view);
    }

    public void setTool(Context context, ToolsBean.Tools tools){
        item_name.setText(tools.getToolsConfigTypeDO().getTypeName());
        grid=new GridLayoutManager(context,4);
        child_rv.setLayoutManager(grid);
        childToolAdapter=new ChildToolAdapter(context);
        child_rv.setAdapter(childToolAdapter);
        childToolAdapter.setDatas(tools.getToolsConfigDOList());
    }
}
