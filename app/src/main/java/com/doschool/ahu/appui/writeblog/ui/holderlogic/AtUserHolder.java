package com.doschool.ahu.appui.writeblog.ui.holderlogic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.home.ui.activity.OtherSingleActivity;
import com.doschool.ahu.appui.main.event.UserComm;
import com.doschool.ahu.appui.main.ui.bean.SingleUserVO;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.XLGlideLoader;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;



/**
 * Created by X on 2018/11/5
 */
public class AtUserHolder extends BaseRvHolder {

    public RelativeLayout atuser_rl;
    public ImageView atuser_iv_head,atu_ivor,atu_ivbs;
    public TextView atuser_name,atu_txy;

    public AtUserHolder(View itemView) {
        super(itemView);
        atuser_rl=findViewById(R.id.atuser_rl);
        atuser_iv_head=findViewById(R.id.atuser_iv_head);
        atu_ivor=findViewById(R.id.atu_ivor);
        atu_ivbs=findViewById(R.id.atu_ivbs);
        atuser_name=findViewById(R.id.atuser_name);
        atu_txy=findViewById(R.id.atu_txy);
    }

    public static AtUserHolder newInstance(ViewGroup parent,int layout){
        View view= LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new AtUserHolder(view);
    }

    @SuppressLint("CheckResult")
    public void setAtHolder(Context context, SingleUserVO singleUserVO){
        XLGlideLoader.loadCircleImage(atuser_iv_head,singleUserVO.getHeadImage());
        atuser_name.setText(singleUserVO.getNickName());
        if (singleUserVO.isOR() || singleUserVO.isTeacher()){
            atu_ivbs.setVisibility(View.GONE);
            atu_ivor.setVisibility(View.VISIBLE);
            UserComm.updateIdentify(atu_ivor,singleUserVO);
        }else {
            atu_ivor.setVisibility(View.GONE);
            atu_ivbs.setVisibility(View.VISIBLE);
            UserComm.updateIdentify(atu_ivbs,singleUserVO);
        }
        atu_txy.setText(singleUserVO.getDepartAbbr());

        //查看个人中心
        RxView.clicks(atuser_iv_head)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (new LoginDao(context).getObject().getUserDO().getId()!=singleUserVO.getUserId()){
                        Bundle bundle=new Bundle();
                        bundle.putInt("userid",singleUserVO.getUserId());
                        IntentUtil.toActivity(context,bundle,OtherSingleActivity.class);
                    }
                });

    }
}
