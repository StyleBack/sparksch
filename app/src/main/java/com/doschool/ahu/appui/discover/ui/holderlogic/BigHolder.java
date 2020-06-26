package com.doschool.ahu.appui.discover.ui.holderlogic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.discover.ui.bean.ServiceConfigBean;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.factory.AppDoUrlFactory;
import com.doschool.ahu.utils.XLGlideLoader;

/**
 * Created by X on 2018/9/25
 */
public class BigHolder extends BaseRvHolder {

    public ImageView big_iv;

    public BigHolder(View itemView) {
        super(itemView);
        big_iv=findViewById(R.id.big_iv);
    }

    public static BigHolder newInstance(ViewGroup parent,int layout){
        View view= LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new BigHolder(view);
    }

    public void bigholder(final Context context, final ServiceConfigBean data){
        XLGlideLoader.loadCornerImage(big_iv, data.getIcon());
        big_iv.setOnClickListener(v -> AppDoUrlFactory.gotoAway(context,data.getDoUrl(),"",""));
    }
}
