package com.doschool.ahu.appui.mine.ui.holderlogic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.mine.ui.bean.ToolService;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.factory.AppDoUrlFactory;
import com.doschool.ahu.utils.XLGlideLoader;

/**
 * Created by X on 2018/9/11
 */
public class ServiceHolder extends BaseRvHolder {


    public ImageView service_iv;
    public TextView service_tv;
    public View view_line;
    public RelativeLayout servce_rlparent;

    public ServiceHolder(View itemView) {
        super(itemView);
        service_iv=findViewById(R.id.service_iv);
        service_tv=findViewById(R.id.service_tv);
        view_line=findViewById(R.id.view_line);
        servce_rlparent=findViewById(R.id.servce_rlparent);
    }

    public static ServiceHolder newInstance(ViewGroup parent,int layout){
        View view= LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new ServiceHolder(view);
    }

    public void setTool(Context context,int postion,ToolService.ToolBean data){
        if (postion==0){
            view_line.setVisibility(View.GONE);
        }else {
            view_line.setVisibility(View.VISIBLE);
        }
        XLGlideLoader.loadImageByUrl(service_iv,data.getIcon());
        service_tv.setText(data.getName());

        servce_rlparent.setOnClickListener(v->{
            AppDoUrlFactory.clickService(context,data.getId());
            AppDoUrlFactory.gotoAway(context,data.getDoUrl(),"","");
        });
    }
}
