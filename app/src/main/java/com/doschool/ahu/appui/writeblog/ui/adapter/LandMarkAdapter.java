package com.doschool.ahu.appui.writeblog.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.writeblog.event.OnLandListener;
import com.doschool.ahu.appui.writeblog.ui.bean.LandMarkBean;
import com.doschool.ahu.appui.writeblog.ui.holderlogic.LandMarkHolder;
import com.doschool.ahu.base.adapter.BaseRvAdapter;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by X on 2018/11/7
 */
public class LandMarkAdapter extends BaseRvAdapter<LandMarkBean.DataBean,LandMarkHolder> {

    private int id;
    public LandMarkAdapter(Context context,int id) {
        super(context);
        this.id=id;
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.item_land_lay;
    }

    @Override
    protected LandMarkHolder onCreateHolder(ViewGroup parent, int viewType) {
        return LandMarkHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void bindData(LandMarkHolder holder, int position, LandMarkBean.DataBean data) {
        holder.setLand(context,id,position,data);

        RxView.clicks(holder.land_rool)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (onLandListener!=null){
                        onLandListener.onLand(data.getId(),data.getPlaceName());
                    }
                });
    }

    public void setOnLandListener(OnLandListener onLandListener) {
        this.onLandListener = onLandListener;
    }

    private OnLandListener onLandListener;

}
