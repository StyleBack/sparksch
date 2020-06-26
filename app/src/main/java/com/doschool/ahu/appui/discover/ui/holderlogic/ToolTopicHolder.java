package com.doschool.ahu.appui.discover.ui.holderlogic;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.discover.ui.activity.HotTopicListActivity;
import com.doschool.ahu.appui.discover.ui.bean.MicroTopic;
import com.doschool.ahu.appui.discover.ui.bean.MicroblogTopicDO;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.XLGlideLoader;

/**
 * Created by X on 2018/9/24
 */
public class ToolTopicHolder extends BaseRvHolder {

    public ImageView tool_iv;
    public TextView tool_tv;
    public ToolTopicHolder(View itemView) {
        super(itemView);
        tool_iv=findViewById(R.id.tool_iv);
        tool_tv=findViewById(R.id.tool_tv);
    }

    public static ToolTopicHolder newInstance(ViewGroup parent,int layout){
        View view= LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new ToolTopicHolder(view);
    }

    public void toolHolder(final Context context, final MicroTopic.McData data){
        XLGlideLoader.loadImageByUrl(tool_iv,data.getMicroblogTopicDO().getBackground());
        tool_tv.setText("#\t"+data.getMicroblogTopicDO().getTopicName()+"\t#");

        //跳转某个类型话题的微博
        tool_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("id",String.valueOf(data.getMicroblogTopicDO().getId()));
                bundle.putString("name",data.getMicroblogTopicDO().getTopicName());
                IntentUtil.toActivity(context,bundle, HotTopicListActivity.class);
            }
        });
    }
}
