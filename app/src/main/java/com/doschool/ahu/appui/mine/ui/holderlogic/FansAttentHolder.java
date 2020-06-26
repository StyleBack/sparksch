package com.doschool.ahu.appui.mine.ui.holderlogic;

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
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.XLGlideLoader;

/**
 * Created by X on 2018/9/24
 *
 * 粉丝  关注
 */
public class FansAttentHolder extends BaseRvHolder {

    public ImageView fans_ivhead;
    public TextView fans_tvname;
    public ImageView fans_ivident;
    public ImageView fans_ivsex;
    public RelativeLayout fas_rl;

    public FansAttentHolder(View itemView) {
        super(itemView);
        fans_ivhead=findViewById(R.id.fans_ivhead);
        fans_tvname=findViewById(R.id.fans_tvname);
        fans_ivident=findViewById(R.id.fans_ivident);
        fans_ivsex=findViewById(R.id.fans_ivsex);
        fas_rl=findViewById(R.id.fas_rl);
    }

    public static FansAttentHolder newInstance(ViewGroup parent,int layout){
        View view= LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new FansAttentHolder(view);
    }

    public void fansHolder(Context context, SingleUserVO data){
        XLGlideLoader.loadCircleImage(fans_ivhead,data.getHeadImage());
        fans_tvname.setText(data.getNickName());
        if (data.isOR() || data.isTeacher()){
            fans_ivsex.setVisibility(View.GONE);
            fans_ivident.setVisibility(View.VISIBLE);
            UserComm.updateIdentify(fans_ivident,data);
        }else {
            fans_ivident.setVisibility(View.GONE);
            fans_ivsex.setVisibility(View.VISIBLE);
            UserComm.updateIdentify(fans_ivsex,data);
        }

        fas_rl.setOnClickListener(v -> gotoSingle(context,data));
    }

    private void gotoSingle(Context context,SingleUserVO data){
        Bundle bundle=new Bundle();
        bundle.putInt("userid",data.getUserId());
        IntentUtil.toActivity(context,bundle,OtherSingleActivity.class);
    }
}
