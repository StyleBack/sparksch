package com.doschool.ahu.appui.writeblog.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.writeblog.ui.bean.PhotoBean;
import com.doschool.ahu.appui.writeblog.ui.holderlogic.PhotoHolder;
import com.doschool.ahu.base.adapter.BaseRvAdapter;
import com.orhanobut.logger.Logger;

/**
 * Created by X on 2018/9/19
 */
public class PhotoAdapter extends BaseRvAdapter<PhotoBean,PhotoHolder> {
    public PhotoAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.photo_item_lay;
    }

    @Override
    protected PhotoHolder onCreateHolder(ViewGroup parent, int viewType) {
        return PhotoHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(PhotoHolder holder, final int position, PhotoBean data) {
        holder.setPho(context,data);

        holder.pil_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClicks!=null){
                    onItemClicks.onItemClicks(position);
                }
            }
        });
    }

    private OnItemClicks onItemClicks;

    public void setOnItemClicks(OnItemClicks onItemClicks){
        this.onItemClicks=onItemClicks;
    }

    public interface OnItemClicks{
        void onItemClicks(int position);
    }
}
