package com.doschool.ahu.appui.infors.ui.holderlogic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.infors.ui.bean.PushRecordBean;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.base.model.DoUrlModel;
import com.doschool.ahu.factory.AppDoUrlFactory;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.utils.XLGson;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class NotifyAssHolder extends BaseRvHolder {

    public TextView noti_tv_time,noti_tv_content;
    public LinearLayout noti_ll,noti_par;
    public ImageView noti_iv;
    private String id,name;

    public NotifyAssHolder(View itemView) {
        super(itemView);
        noti_tv_time=findViewById(R.id.noti_tv_time);
        noti_tv_content=findViewById(R.id.noti_tv_content);
        noti_ll=findViewById(R.id.noti_ll);
        noti_par=findViewById(R.id.noti_par);
        noti_iv=findViewById(R.id.noti_iv);
    }

    public static NotifyAssHolder newInstance(ViewGroup parent,int layout){
        View view=LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new NotifyAssHolder(view);
    }

    @SuppressLint("CheckResult")
    public void setNotiDto(Context context, PushRecordBean.DataBean dto){
        noti_tv_time.setText(dto.getPushDate());
        noti_tv_content.setText(dto.getText());

        if (!TextUtils.isEmpty(dto.getImageUrl())){
            noti_iv.setVisibility(View.VISIBLE);
            XLGlideLoader.loadImageByUrl(noti_iv,dto.getImageUrl());
        }else {
            noti_iv.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(dto.getExtra())){
            noti_ll.setVisibility(View.VISIBLE);
            DoUrlModel model = XLGson.fromJosn(dto.getExtra(), DoUrlModel.class);
            if (model.getParamList()!=null && model.getParamList().size()>0){
                if (model.getParamList().size()>1){
                    id=model.getParamList().get(0);
                    name=model.getParamList().get(1);
                }
            }
            RxView.clicks(noti_par)
                    .throttleFirst(1,TimeUnit.SECONDS)
                    .subscribe(o -> AppDoUrlFactory.gotoAway(context,dto.getExtra(), id, name));
        }else {
            noti_ll.setVisibility(View.GONE);
        }
    }

}
