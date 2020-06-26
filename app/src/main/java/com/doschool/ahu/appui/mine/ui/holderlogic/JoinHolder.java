package com.doschool.ahu.appui.mine.ui.holderlogic;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.discover.ui.activity.HotTopicListActivity;
import com.doschool.ahu.appui.mine.ui.bean.MineTopicBean;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.utils.IntentUtil;
import com.orhanobut.logger.Logger;

/**
 * Created by X on 2018/9/25
 */
public class JoinHolder extends BaseRvHolder {

    public TextView join_tvm;
    public TextView join_tvcount;
    public RelativeLayout join_rlgo;

    public JoinHolder(View itemView) {
        super(itemView);
        join_tvm=findViewById(R.id.join_tvm);
        join_tvcount=findViewById(R.id.join_tvcount);
        join_rlgo=findViewById(R.id.join_rlgo);
    }

    public static JoinHolder newInstance(ViewGroup parent,int layout){
        View view= LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new JoinHolder(view);
    }

    public void joinHolder(final Context context, final MineTopicBean.TD data){
        join_tvm.setText(data.getMicroblogTopicDO().getTopicName());
        join_tvcount.setText(data.getMicroblogNum()+"条参与");

        //go话题列表
        join_rlgo.setOnClickListener(v -> {
            Bundle bundle=new Bundle();
            bundle.putString("id",String.valueOf(data.getMicroblogTopicDO().getId()));
            bundle.putString("name",data.getMicroblogTopicDO().getTopicName());
            IntentUtil.toActivity(context,bundle, HotTopicListActivity.class);
        });
    }
}
