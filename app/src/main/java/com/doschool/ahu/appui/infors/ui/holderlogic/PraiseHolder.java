package com.doschool.ahu.appui.infors.ui.holderlogic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.home.ui.activity.BlogDetailActivity;
import com.doschool.ahu.appui.home.ui.activity.OtherSingleActivity;
import com.doschool.ahu.appui.infors.ui.bean.PraiseBean;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.configfile.CodeConfig;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.StringUtil;
import com.doschool.ahu.utils.TimeUtil;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.widget.component.SuperText;
import com.jakewharton.rxbinding2.view.RxView;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;


/**
 * Created by X on 2018/9/22
 */
public class PraiseHolder extends BaseRvHolder {

    public ImageView pra_iv;
    public TextView pra_tv_name,pra_tv_status,pra_tv_time,pra_tv_content,pra_tv_comm;
    public RelativeLayout pra_rl;

    public PraiseHolder(View itemView) {
        super(itemView);
        pra_iv=findViewById(R.id.pra_iv);
        pra_tv_name=findViewById(R.id.pra_tv_name);
        pra_tv_status=findViewById(R.id.pra_tv_status);
        pra_tv_time=findViewById(R.id.pra_tv_time);
        pra_tv_content=findViewById(R.id.pra_tv_content);
        pra_tv_comm=findViewById(R.id.pra_tv_comm);
        pra_rl=findViewById(R.id.pra_rl);
    }

    public static PraiseHolder newInstance(ViewGroup parent,int layout){
        View view= LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new PraiseHolder(view);
    }

    @SuppressLint("CheckResult")
    public void setHolder(Context context, PraiseBean.PraData data){
        if (data==null){
            return;
        }
        XLGlideLoader.loadCircleImage(pra_iv,data.getFromUser().getHeadImage());
        pra_tv_name.setText(data.getFromUser().getNickName());

        if (data.getMessageType()== CodeConfig.TYPE_MICROBLOG_ZAN){
            pra_tv_status.setText("赞了你的动态!");
        }else if (data.getMessageType()== CodeConfig.TYPE_COMMENT_ZAN){
            pra_tv_status.setText("赞了你的评论!");
        }else if (data.getMessageType()== CodeConfig.TYPE_MICROBLOG_AT){
            pra_tv_status.setText("在动态中@你了!");
        }else if (data.getMessageType()== CodeConfig.TYPE_MICROBLOG_COMMENT){
            pra_tv_status.setText("评论了你的动态!");
        }else if (data.getMessageType()== CodeConfig.TYPE_MICROBLOG_TRANSMIT){
            pra_tv_status.setText("转发了你的动态!");
        }else if (data.getMessageType()== CodeConfig.TYPE_MICROBLOG_DELETE){
            pra_tv_status.setText("你的动态已被删除!");
        }else if (data.getMessageType()== CodeConfig.TYPE_COMMENT_AT){
            pra_tv_status.setText("在评论中@你了!");
        }else if (data.getMessageType()== CodeConfig.TYPE_COMMENT_COMMENT){
            pra_tv_status.setText("回复了你的评论!");
        }else if (data.getMessageType()== CodeConfig.TYPE_COMMENT_DELETE){
            pra_tv_status.setText("你的评论已被删除!");
        }else if (data.getMessageType()== CodeConfig.TYPE_MICROBLOG_ROOTTRANSMIT){
            pra_tv_status.setText("转发了你的动态!");
        }

        if (data.getNewObject()!=null){
            pra_tv_comm.setVisibility(View.VISIBLE);
            SpannableString spannableString= StringUtil.stringToSpannableString(data.getNewObject().getComment(), context, 14);
            pra_tv_comm.setText(spannableString);
            SuperText.txtlink(context,pra_tv_comm);
        }else {
            pra_tv_comm.setVisibility(View.GONE);
        }

        Long time= TimeUtils.string2Millis(
                data.getCreateTime(),
                new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
        pra_tv_time.setText(TimeUtil.date2USDiy(time));
        SpannableString spannableString= StringUtil.stringToSpannableString(data.getOriginalObject().getContent(), context, 14);
        pra_tv_content.setText(spannableString);
        SuperText.txtlink(context,pra_tv_content);

        pra_rl.setOnClickListener(v -> {
            if (data.getOriginalObject().getIsDeleted()==0){
                Bundle bundle=new Bundle();
                bundle.putInt("blogId",data.getOriginalObject().getId());
                bundle.putString("blogTag","unRecom");
                IntentUtil.toActivity(context,bundle, BlogDetailActivity.class);
            }else {
                XLToast.showToast("此微博不存在！");
            }
        });

        //个人
        RxView.clicks(pra_iv)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (new LoginDao(context).getObject().getUserDO().getId()!=data.getFromUser().getUserId()){
                        Bundle bundle=new Bundle();
                        bundle.putInt("userid",data.getFromUser().getUserId());
                        IntentUtil.toActivity(context,bundle,OtherSingleActivity.class);
                    }
                });
    }

}
