package com.doschool.ahu.appui.writeblog.ui.holderlogic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.writeblog.ui.bean.LandMarkBean;
import com.doschool.ahu.base.adapter.BaseRvHolder;

import static android.view.View.VISIBLE;

/**
 * Created by X on 2018/11/7
 */
public class LandMarkHolder extends BaseRvHolder {

    public RelativeLayout land_rool;
    public TextView land_tv;
    public ImageView land_iv;

    public LandMarkHolder(View itemView) {
        super(itemView);
        land_rool=findViewById(R.id.land_rool);
        land_tv=findViewById(R.id.land_tv);
        land_iv=findViewById(R.id.land_iv);
    }

    public static LandMarkHolder newInstance(ViewGroup parent,int layout){
        View view= LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new LandMarkHolder(view);
    }

    public void setLand(Context context,int id,int position, LandMarkBean.DataBean data){
        land_tv.setText(data.getPlaceName());
        if (position==0){
            land_tv.setTextColor(context.getResources().getColor(R.color.now_txt_color));
        }else {
            land_tv.setTextColor(context.getResources().getColor(R.color.title_color));
        }
        if (id==data.getId()){
            land_iv.setVisibility(VISIBLE);
        }else {
            land_iv.setVisibility(View.GONE);
        }
    }
}
