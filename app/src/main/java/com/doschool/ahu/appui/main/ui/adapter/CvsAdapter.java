package com.doschool.ahu.appui.main.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.infors.chat.ui.model.Conversation;
import com.doschool.ahu.base.adapter.BaseRvAdapter;
import com.doschool.ahu.appui.main.event.SwipeItemClickListener;
import com.doschool.ahu.appui.main.ui.holderlogic.CvsHolder;
import com.doschool.ahu.db.ConversationDao;

/**
 * Created by X on 2018/8/23
 */
public class CvsAdapter extends BaseRvAdapter<Conversation,CvsHolder> {

    private SwipeItemClickListener swipeItemClickListener;

    private ConversationDao conversationDao;

    public CvsAdapter(Context context,ConversationDao conversationDao) {
        super(context);
        this.conversationDao = conversationDao;
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.item_swipe_layout;
    }

    @Override
    protected void bindData(final CvsHolder holder, final int position, final Conversation data) {
        holder.setUpDatas(context,data, conversationDao);

        holder.right_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.swipelayout.resetStatus();
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeItemClickListener!=null){
                            swipeItemClickListener.onTop( position,holder);
                        }
                    }
                },200);
            }
        });

        holder.right_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.swipelayout.resetStatus();
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeItemClickListener!=null){
                            swipeItemClickListener.onDelete( position);
                        }
                    }
                },100);
            }
        });

        holder.swipe_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.swipelayout.resetStatus();
                if (swipeItemClickListener!=null){
                    swipeItemClickListener.onItemListener(position);
                }
            }
        });
    }

    @Override
    protected CvsHolder onCreateHolder(ViewGroup parent, int viewType) {
        return CvsHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    public void setSwipeItemClickListener(SwipeItemClickListener swipeItemClickListener){
        this.swipeItemClickListener=swipeItemClickListener;
    }

}
