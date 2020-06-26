package com.doschool.ahu.appui.home.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.home.event.OnCommentListener;
import com.doschool.ahu.appui.home.event.OnCommentLong;
import com.doschool.ahu.appui.home.ui.holderlogic.DtChildHolder;
import com.doschool.ahu.appui.main.ui.bean.MicroblogCommentVO;
import com.doschool.ahu.base.adapter.BaseRvAdapter;

import static com.doschool.ahu.configfile.CodeConfig.REPORT_COMMENT;

/**
 * Created by X on 2018/9/14
 */
public class DtChildAdapter extends BaseRvAdapter<MicroblogCommentVO,DtChildHolder> {

    private OnCommentListener onCommentListener;

    public void setOnCommentListenerChild(OnCommentListener onCommentListener) {
        this.onCommentListener = onCommentListener;
    }

    private OnCommentLong onCommentLong;

    public void setOnCommentLong(OnCommentLong onCommentLong) {
        this.onCommentLong = onCommentLong;
    }

    public DtChildAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.dt_child_layout;
    }

    @Override
    protected DtChildHolder onCreateHolder(ViewGroup parent, int viewType) {
        return DtChildHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(DtChildHolder holder, int position, final MicroblogCommentVO data) {
        holder.setHodler(context,data);

        //评论二级
        holder.child_lsit_tv.setOnClickListener(v -> {
            if (holder.isClick){//处理拦截点击事件
                holder.isClick=false;
                return;
            }
            if (onCommentListener!=null){
                onCommentListener.onListener(data.getMicroblogCommentDO().getHostBlogId(),
                        data.getUserVO().getUserId(),data.getMicroblogCommentDO().getId(),
                        data.getUserVO().getNickName());
            }
        });

        //评论举报  删除
        holder.child_lsit_tv.setOnLongClickListener(v -> {
            if (onCommentLong!=null){
                onCommentLong.onLongListener(REPORT_COMMENT,data.getMicroblogCommentDO().getId(),
                        data.getUserVO().getUserId(),position,-101);
            }
            return false;
        });
    }
}
