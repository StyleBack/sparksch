package com.doschool.ahu.appui.main.ui.holderlogic;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.MicroblogCommentVO;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.utils.StringUtil;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.widget.component.SuperText;

/**
 * Created by X on 2019/1/22.
 */
public class RmShareHolder extends BaseRvHolder {

    public ImageView item_rm_iv;
    public TextView item_rm_tv;
    public LinearLayout rm_ll;

    public RmShareHolder(View itemView) {
        super(itemView);
        item_rm_iv=itemView.findViewById(R.id.item_rm_iv);
        item_rm_tv=itemView.findViewById(R.id.item_rm_tv);
        rm_ll=itemView.findViewById(R.id.rm_ll);
    }

    public static RmShareHolder newIsntance(ViewGroup parent,int layout){
        View view=LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new RmShareHolder(view);
    }

    public void setRmhol(Context context,MicroblogCommentVO data){
        XLGlideLoader.loadCircleImage(item_rm_iv,data.getUserVO().getHeadImage());
        //处理@用户  表情的标识
        SpannableString spannableString= StringUtil.stringToSpannableString(data.getUserVO().getNickName()+"："+data.getMicroblogCommentDO().getComment(), context, 15);
        item_rm_tv.setText(spannableString);
        SuperText.txtlink(context,item_rm_tv);
//        item_rm_tv.setText(data.getUserVO().getNickName()+"："+data.getMicroblogCommentDO().getComment());
    }
}
