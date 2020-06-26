package com.doschool.ahu.appui.main.ui.holderlogic;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.MicroblogCommentVO;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.utils.StringUtil;
import com.doschool.ahu.widget.component.SuperText;

/**
 * Created by X on 2018/9/7
 */
public class BlogCommentHolder extends BaseRvHolder {

    public TextView child_tvcomment;

    public BlogCommentHolder(View itemView) {
        super(itemView);
        child_tvcomment=findViewById(R.id.child_tvcomment);
    }

    public static BlogCommentHolder newInstance(ViewGroup parent,int layoutID){
        View view= LayoutInflater.from(parent.getContext()).inflate(layoutID,parent,false);
        return new BlogCommentHolder(view);
    }

    public void initData(Context context, MicroblogCommentVO data){

        if (data.getUserVO()!=null){
            String name;
            if (!TextUtils.isEmpty(data.getUserVO().getRemarkName())){
                name=data.getUserVO().getRemarkName();
            }else {
                name=data.getUserVO().getNickName();
            }
            String span=name+"："+data.getMicroblogCommentDO().getComment();

            SpannableString spannableString= StringUtil.stringToSpannableString(span, context, 14);
            spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.now_txt_color)),
                    0,name.length()+1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            child_tvcomment.setText(spannableString);
            SuperText.txtlink(context,child_tvcomment);
        }
    }

}
