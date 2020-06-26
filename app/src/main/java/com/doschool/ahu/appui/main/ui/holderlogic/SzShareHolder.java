package com.doschool.ahu.appui.main.ui.holderlogic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVoteOptionsDto;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.XLGlideLoader;

/**
 * Created by X on 2019/1/22.
 */
public class SzShareHolder extends BaseRvHolder {

    public RelativeLayout sz_rlmat;
    public LinearLayout sz_llsu;
    public ImageView sz_ivsu;
    public ProgressBar sz_pb;
    public TextView sz_tvn;
    public TextView sz_pcet;
    public ImageView sz_winiv;

    public SzShareHolder(View itemView) {
        super(itemView);
        sz_llsu=itemView.findViewById(R.id.sz_llsu);
        sz_ivsu=itemView.findViewById(R.id.sz_ivsu);
        sz_pb=itemView.findViewById(R.id.sz_pb);
        sz_tvn=itemView.findViewById(R.id.sz_tvn);
        sz_pcet=itemView.findViewById(R.id.sz_pcet);
        sz_winiv=itemView.findViewById(R.id.sz_winiv);
        sz_rlmat=itemView.findViewById(R.id.sz_rlmat);
    }

    public static SzShareHolder nesInstance(ViewGroup parent,int layout){
        View view=LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new SzShareHolder(view);
    }

    public void setSzHol(Context context,MicroblogVoteOptionsDto data){
        if (data.isSelected()){
            sz_llsu.setVisibility(View.VISIBLE);
            XLGlideLoader.loadCircleImage(sz_ivsu,new LoginDao(context).getObject().getUserDO().getHeadImage());
        }else {
            sz_llsu.setVisibility(View.GONE);
        }
        sz_pb.setProgress(Integer.parseInt(data.getProportion().substring(0,data.getProportion().indexOf("%"))));
        sz_tvn.setText(data.getMicroblogVoteOptionsDO().getContent());
        sz_pcet.setText(data.getProportion());
    }
}
