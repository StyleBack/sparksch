package com.doschool.ahu.appui.main.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.MicroblogCommentVO;
import com.doschool.ahu.appui.main.ui.holderlogic.BlogCommentHolder;
import com.doschool.ahu.base.adapter.BaseRvAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by X on 2018/9/7
 */
public class BlogCommentAdpter extends BaseRvAdapter<MicroblogCommentVO,BlogCommentHolder> {

    public BlogCommentAdpter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.item_blog_child_layout;
    }

    @Override
    protected void bindData(BlogCommentHolder holder, int position, MicroblogCommentVO data) {
        holder.initData(context,data);
    }

    @Override
    protected BlogCommentHolder onCreateHolder(ViewGroup parent, int viewType) {
        return BlogCommentHolder.newInstance(parent,getItemLayoutID(viewType));
    }
}
