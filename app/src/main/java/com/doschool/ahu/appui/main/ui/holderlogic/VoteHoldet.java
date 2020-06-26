package com.doschool.ahu.appui.main.ui.holderlogic;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVoteOptionsDto;
import com.doschool.ahu.base.adapter.BaseRvHolder;

/**
 * Created by X on 2019/1/15.
 */
public class VoteHoldet extends BaseRvHolder {

    public RelativeLayout child_rl;
    public ProgressBar child_pb;
    public TextView child_vote_name;
    public TextView child_tv_num;

    private boolean isClick;

    public VoteHoldet(View itemView) {
        super(itemView);
        child_rl=itemView.findViewById(R.id.child_rl);
        child_pb=itemView.findViewById(R.id.child_pb);
        child_vote_name=itemView.findViewById(R.id.child_vote_name);
        child_tv_num=itemView.findViewById(R.id.child_tv_num);
    }

    public static VoteHoldet newInstance(ViewGroup parent,int layoutId){
        View view=LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        return new VoteHoldet(view);
    }

    public void setVoteHolder(Context context, MicroblogVoteOptionsDto data,boolean isVote,boolean isAnima){
        child_vote_name.setText(data.getMicroblogVoteOptionsDO().getContent());
        if (isVote){
            if (data.isSelected()){
                Drawable drpb=context.getResources().getDrawable(R.drawable.progress_lary);
                drpb.setBounds(child_pb.getProgressDrawable().getBounds());
                child_pb.setProgressDrawable(drpb);
            }else {
                Drawable drpb2=context.getResources().getDrawable(R.drawable.progress_lary_other);
                drpb2.setBounds(child_pb.getProgressDrawable().getBounds());
                child_pb.setProgressDrawable(drpb2);
            }
            int progress=Integer.parseInt(data.getProportion().substring(0,data.getProportion().indexOf("%")));
            if (isAnima){
                setAnimation(child_pb,progress);
            }else {
                child_pb.setProgress(progress);
            }
            child_tv_num.setText(data.getProportion()+"（"+data.getMicroblogVoteOptionsDO().getSelectNum()+"人"+"）");
        }else {
            Drawable drpb3=context.getResources().getDrawable(R.drawable.progress_no);
            drpb3.setBounds(child_pb.getProgressDrawable().getBounds());
            child_pb.setProgressDrawable(drpb3);
            child_pb.setProgress(0);
            child_tv_num.setText("");
        }
    }

    private void setAnimation(final ProgressBar progressBar, final int progress) {
        ValueAnimator animator = ValueAnimator.ofInt(0, progress).setDuration(1500);
        animator.addUpdateListener(valueAnimator -> progressBar.setProgress((int) valueAnimator.getAnimatedValue()));
        animator.start();
    }
}
