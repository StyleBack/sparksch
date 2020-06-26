package com.doschool.ahu.appui.discover.ui.holderlogic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.discover.ui.bean.LineBean;
import com.doschool.ahu.appui.main.ui.activity.WebActivity;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.factory.AppDoUrlFactory;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.XLGlideLoader;
import com.orhanobut.logger.Logger;

import static com.doschool.ahu.configfile.ApiConfig.HEADLINE_API;

/**
 * Created by X on 2018/9/27
 */
public class LineHolder extends BaseRvHolder {

    public ImageView line_bgiv,line_bgiv2;
    public TextView line_txi,line_txi2;
    public TextView line_txlab,line_txlab2;
    public TextView line_txtime,line_txtime2;
    public TextView line_txauthor,line_txauthor2;
    public RelativeLayout line_l1,line_l2;

    public LineHolder(View itemView) {
        super(itemView);
        line_bgiv=findViewById(R.id.line_bgiv);
        line_txi=findViewById(R.id.line_txi);
        line_txlab=findViewById(R.id.line_txlab);
        line_txtime=findViewById(R.id.line_txtime);
        line_txauthor=findViewById(R.id.line_txauthor);
        line_l1=findViewById(R.id.line_l1);

        line_bgiv2=findViewById(R.id.line_bgiv2);
        line_txi2=findViewById(R.id.line_txi2);
        line_txlab2=findViewById(R.id.line_txlab2);
        line_txtime2=findViewById(R.id.line_txtime2);
        line_txauthor2=findViewById(R.id.line_txauthor2);
        line_l2=findViewById(R.id.line_l2);
    }

    public static LineHolder newInstance(ViewGroup parent,int layout){
        View view= LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new LineHolder(view);
    }

    public void lineHolder(Context context, LineBean.LineDt dt,int type){

        StringBuilder urll = new StringBuilder();
        urll.append(HEADLINE_API).append("/article/index.html?").append("articleId=")
                .append(dt.getHeadlineArticleDO().getId()).append("&userId=")
                .append(new LoginDao(context).getObject().getUserDO().getId());
        if (type%2==0){
            XLGlideLoader.loadCornerImage(line_bgiv,dt.getHeadlineArticleDO().getCoverImage());
            line_txi.setText(dt.getHeadlineArticleDO().getTitle());
            line_txlab.setText(dt.getHeadlineArticleDO().getLabel());
            line_txtime.setText(dt.getHeadlineArticleDO().getDate());
            line_txauthor.setText(dt.getHeadlineArticleDO().getSource());
            line_l1.setOnClickListener(v -> {
                Bundle bundle=new Bundle();
                bundle.putString("URL", String.valueOf(urll));
                IntentUtil.toActivity(context,bundle, WebActivity.class);
                });
        }else {
            XLGlideLoader.loadCornerImage(line_bgiv2,dt.getHeadlineArticleDO().getCoverImage());
            line_txi2.setText(dt.getHeadlineArticleDO().getTitle());
            line_txlab2.setText(dt.getHeadlineArticleDO().getLabel());
            line_txtime2.setText(dt.getHeadlineArticleDO().getDate());
            line_txauthor2.setText(dt.getHeadlineArticleDO().getSource());
            line_l2.setOnClickListener(v -> {
                Bundle bundle=new Bundle();
                bundle.putString("URL", String.valueOf(urll));
                IntentUtil.toActivity(context,bundle, WebActivity.class);
            });
        }
    }
}
