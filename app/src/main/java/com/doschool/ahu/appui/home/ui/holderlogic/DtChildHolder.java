package com.doschool.ahu.appui.home.ui.holderlogic;

import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.home.ui.activity.OtherSingleActivity;
import com.doschool.ahu.appui.main.event.TxtClickSpan;
import com.doschool.ahu.appui.main.ui.bean.MicroblogCommentVO;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.StringUtil;
import com.doschool.ahu.widget.component.SuperText;

/**
 * Created by X on 2018/9/14
 */
public class DtChildHolder extends BaseRvHolder {

    public TextView child_lsit_tv;
    //去除部分文字点击和点击事件的冲突
    public boolean isClick=false;

    public DtChildHolder(View itemView) {
        super(itemView);
        child_lsit_tv = findViewById(R.id.child_lsit_tv);
    }

    public static DtChildHolder newInstance(ViewGroup parent, int layout) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new DtChildHolder(view);
    }

    public void setHodler(Context context, MicroblogCommentVO data) {
        try {

            String uName;
            //备注
            if (!TextUtils.isEmpty(data.getUserVO().getRemarkName())){
                uName= data.getUserVO().getRemarkName();
            }else {
                uName= data.getUserVO().getNickName();
            }
            String tagName ;
            //备注
            if (!TextUtils.isEmpty(data.getTargetUserVO().getRemarkName())){
                tagName= data.getTargetUserVO().getRemarkName();
            }else {
                tagName= data.getTargetUserVO().getNickName();
            }
            String sp = uName + "回复" + tagName + "：" + data.getMicroblogCommentDO().getComment();

            SpannableString spannableString = StringUtil.stringToSpannableString(sp, context, 14);
            //回复人 点击name事件处理
            spannableString.setSpan(new TxtClickSpan(widget -> {
                isClick=true;
                intentPersonal(context,data.getUserVO().getUserId());
            }),0, uName.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            //回复目标人 点击name事件处理
            spannableString.setSpan(new TxtClickSpan(widget -> {
                isClick=true;
                intentPersonal(context,data.getTargetUserVO().getUserId());
            }),uName.length() + 2, uName.length() + tagName.length() + 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            child_lsit_tv.setText(spannableString);
            child_lsit_tv.setMovementMethod(LinkMovementMethod.getInstance());
            SuperText.txtlink(context,child_lsit_tv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void intentPersonal(Context context,int userId){
        if (new LoginDao(context).getObject().getUserDO().getId()!=userId){
            Bundle bundle=new Bundle();
            bundle.putInt("userid",userId);
            IntentUtil.toActivity(context,bundle,OtherSingleActivity.class);
        }
    }
}
