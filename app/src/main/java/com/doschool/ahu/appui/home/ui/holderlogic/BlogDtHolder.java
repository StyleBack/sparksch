package com.doschool.ahu.appui.home.ui.holderlogic;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.home.ui.activity.OtherSingleActivity;
import com.doschool.ahu.appui.home.ui.adapter.DtChildAdapter;
import com.doschool.ahu.appui.main.event.UserComm;
import com.doschool.ahu.appui.main.ui.bean.MicroblogCommentVO;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.StringUtil;
import com.doschool.ahu.utils.TimeUtil;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.widget.component.SuperText;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Created by X on 2018/9/14
 */
public class BlogDtHolder extends BaseRvHolder {

    public View dt_child_view;
    public ImageView dt_child_iv, dt_child_love, dt_child_ivfy, dt_child_ivsex;
    public TextView dt_child_name, dt_child_time, dt_child_count, dt_child_comt, dt_chd_tvrank;
    public LinearLayout dt_child_lll;
    public RecyclerView dt_child_rv;
    public RelativeLayout child_dtrl;

    public LinearLayoutManager linearLayoutManager;
    public DtChildAdapter dtChildAdapter;

    public BlogDtHolder(View itemView) {
        super(itemView);
        dt_child_view = findViewById(R.id.dt_child_view);
        dt_child_iv = findViewById(R.id.dt_child_iv);
        dt_child_love = findViewById(R.id.dt_child_love);
        dt_child_name = findViewById(R.id.dt_child_name);
        dt_child_time = findViewById(R.id.dt_child_time);
        dt_child_count = findViewById(R.id.dt_child_count);
        dt_child_comt = findViewById(R.id.dt_child_comt);
        dt_child_lll = findViewById(R.id.dt_child_lll);
        dt_child_rv = findViewById(R.id.dt_child_rv);
        child_dtrl = findViewById(R.id.child_dtrl);
        dt_chd_tvrank = findViewById(R.id.dt_chd_tvrank);
        dt_child_ivsex = findViewById(R.id.dt_child_ivsex);
        dt_child_ivfy = findViewById(R.id.dt_child_ivfy);
    }

    public static BlogDtHolder newInstance(ViewGroup parent, int layout) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new BlogDtHolder(view);
    }

    public void setHolder(final Context context, int position, final MicroblogCommentVO data) {

        //个人信息
        dt_child_iv.setOnClickListener(v -> {
            if (new LoginDao(context).getObject().getUserDO().getId()!=data.getUserVO().getUserId()){
                Bundle bundle = new Bundle();
                bundle.putInt("userid", data.getUserVO().getUserId());
                IntentUtil.toActivity(context, bundle, OtherSingleActivity.class);
            }
        });

        dt_chd_tvrank.setText(position + 1 + "L");
        try {
            if (!TextUtils.isEmpty(data.getUserVO().getHeadImage())) {
                XLGlideLoader.loadCircleImage(dt_child_iv, data.getUserVO().getHeadImage());
            }

            //备注
            if (!TextUtils.isEmpty(data.getUserVO().getRemarkName())){
                dt_child_name.setText(data.getUserVO().getRemarkName());
            }else{
                dt_child_name.setText(data.getUserVO().getNickName());
            }
            if (data.getUserVO().isOR() || data.getUserVO().isTeacher()) {
                dt_child_ivsex.setVisibility(View.GONE);
                dt_child_ivfy.setVisibility(View.VISIBLE);
                UserComm.updateIdentify(dt_child_ivfy, data.getUserVO());
            } else {
                if (data.getUserVO().getSex() == 0) {
                    dt_child_ivsex.setVisibility(View.GONE);
                } else {
                    dt_child_ivsex.setVisibility(View.VISIBLE);
                }
                dt_child_ivfy.setVisibility(View.GONE);
                UserComm.updateIdentify(dt_child_ivsex, data.getUserVO());
            }
            Long time = TimeUtils.string2Millis(
                    data.getMicroblogCommentDO().getGmtCreate(),
                    new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
            if (!TextUtils.isEmpty(data.getUserVO().getDepartAbbr())) {
                dt_child_time.setText(TimeUtil.date2USDiy(time) + "\t" + data.getUserVO().getDepartAbbr());
            } else {
                dt_child_time.setText(TimeUtil.date2USDiy(time));
            }

            if (data.getIsLike() == 1) {
                XLGlideLoader.loadImageById(dt_child_love, R.mipmap.icon_love);
            } else {
                XLGlideLoader.loadImageById(dt_child_love, R.mipmap.icon_love_un);
            }
            if (data.getLikeCount() == 0) {
                dt_child_count.setText("0");
            } else {
                if (data.getLikeCount() >= 1000) {
                    String count = new DecimalFormat("0.00").format(Double.valueOf(String.valueOf(data.getLikeCount())) / 1000);
                    dt_child_count.setText(count + "k");
                } else {
                    dt_child_count.setText(String.valueOf(data.getLikeCount()));
                }
            }
            SpannableString spannableString = StringUtil.stringToSpannableString(data.getMicroblogCommentDO().getComment(), context, 16);
            dt_child_comt.setText(spannableString);
            SuperText.txtlink(context,dt_child_comt);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("exception", e.getMessage());
        }

        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dt_child_rv.setLayoutManager(linearLayoutManager);
        dtChildAdapter = new DtChildAdapter(context);
        dt_child_rv.setAdapter(dtChildAdapter);
        dtChildAdapter.setDatas(data.getChildCommentVOList());
    }

}
