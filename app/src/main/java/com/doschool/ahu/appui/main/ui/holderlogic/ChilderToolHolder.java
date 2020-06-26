package com.doschool.ahu.appui.main.ui.holderlogic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.ToolsConfigDOList;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.factory.AppDoUrlFactory;
import com.doschool.ahu.utils.XLGlideLoader;

/**
 * Created by X on 2019/1/12.
 */
public class ChilderToolHolder extends BaseRvHolder {


    public RelativeLayout child_rl;
    public ImageView child_icon;
    public TextView child_tvname;
    public View child_view;
    public TextView child_xin;

    public ChilderToolHolder(View itemView) {
        super(itemView);
        child_rl=itemView.findViewById(R.id.child_rl);
        child_icon=itemView.findViewById(R.id.child_icon);
        child_tvname=itemView.findViewById(R.id.child_tvname);
        child_view=itemView.findViewById(R.id.child_view);
        child_xin=itemView.findViewById(R.id.child_xin);
    }

    public static ChilderToolHolder newInstance(ViewGroup parent,int layoutId){
        View view=LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        return new ChilderToolHolder(view);
    }

    public void setChildHodler(Context context, ToolsConfigDOList data){
        XLGlideLoader.loadImageByUrl(child_icon,data.getIcon());
        child_tvname.setText(data.getName());
        if (data.getBadgeType()==1){
            child_view.setVisibility(View.VISIBLE);
        }else if (data.getBadgeType()==2){
            child_xin.setVisibility(View.VISIBLE);
        }else {
            child_view.setVisibility(View.GONE);
            child_xin.setVisibility(View.GONE);
        }

        child_rl.setOnClickListener(view -> {
//            AppDoUrlFactory.clickService(context,data.getId());
            AppDoUrlFactory.gotoAway(context,data.getDoUrl(),String.valueOf(data.getId()),"");
        });
    }
}
