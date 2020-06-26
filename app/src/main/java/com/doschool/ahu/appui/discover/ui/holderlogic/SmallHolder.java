package com.doschool.ahu.appui.discover.ui.holderlogic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.discover.ui.bean.ServiceConfigBean;
import com.doschool.ahu.appui.main.ui.activity.WebActivity;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.factory.AppDoUrlFactory;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.XLGlideLoader;
import com.orhanobut.logger.Logger;

/**
 * Created by X on 2018/9/23
 */
public class SmallHolder extends BaseRvHolder {

    public ImageView smi_iv_icon;
    public TextView smi_tv_title;
    public LinearLayout small_llparent;

    public SmallHolder(View itemView) {
        super(itemView);
        smi_iv_icon=findViewById(R.id.smi_iv_icon);
        smi_tv_title=findViewById(R.id.smi_tv_title);
        small_llparent=findViewById(R.id.small_llparent);
    }

    public static SmallHolder newInstance(ViewGroup parent,int layout){
        View view= LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new SmallHolder(view);
    }

    public void smallHolder(Context context, ServiceConfigBean data){
        XLGlideLoader.loadImageByUrl(smi_iv_icon,data.getIcon());
        smi_tv_title.setText(data.getName());

        small_llparent.setOnClickListener(v->{
                    AppDoUrlFactory.clickService(context,data.getId());
                    AppDoUrlFactory.gotoAway(context,data.getDoUrl(),"","");
                });
    }
}
