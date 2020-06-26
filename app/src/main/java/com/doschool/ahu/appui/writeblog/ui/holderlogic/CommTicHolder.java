package com.doschool.ahu.appui.writeblog.ui.holderlogic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.discover.ui.bean.MicroTopic;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.utils.XLGlideLoader;

/**
 * Created by X on 2018/11/7
 */
public class CommTicHolder extends BaseRvHolder {

    public LinearLayout tic_lool;
    public ImageView tic_iv;
    public TextView tic_tvn,tic_tvc;

    public CommTicHolder(View itemView) {
        super(itemView);
        tic_lool=findViewById(R.id.tic_lool);
        tic_iv=findViewById(R.id.tic_iv);
        tic_tvn=findViewById(R.id.tic_tvn);
        tic_tvc=findViewById(R.id.tic_tvc);
    }

    public static CommTicHolder newInstance(ViewGroup parent,int layout){
        View view= LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new CommTicHolder(view);
    }

    public void setTicHolder(MicroTopic.McData data){
        XLGlideLoader.loadImageByUrl(tic_iv,data.getMicroblogTopicDO().getIcon());
        tic_tvn.setText(data.getMicroblogTopicDO().getTopicName());
        tic_tvc.setText(data.getMicroblogNum()+"条内容");
    }
}
