package com.doschool.ahu.appui.main.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.SearchWrap;
import com.doschool.ahu.appui.main.ui.holderlogic.SearchWrapHolder;
import com.doschool.ahu.base.adapter.BaseRvAdapter;

/**
 * Created by X on 2018/9/27
 */
public class SearchWrapAdapter extends BaseRvAdapter<SearchWrap,SearchWrapHolder> {
    public SearchWrapAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.searchwrap_item_lay;
    }

    @Override
    protected SearchWrapHolder onCreateHolder(ViewGroup parent, int viewType) {
        return SearchWrapHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(SearchWrapHolder holder, int position, SearchWrap data) {
        holder.serachHolder(context,data);
    }
}
