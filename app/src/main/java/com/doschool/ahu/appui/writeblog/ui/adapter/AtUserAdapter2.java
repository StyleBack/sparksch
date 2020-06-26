package com.doschool.ahu.appui.writeblog.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.SingleUserVO;
import com.doschool.ahu.appui.writeblog.event.OnAtListener;
import com.doschool.ahu.appui.writeblog.ui.holderlogic.AtUserHolder;
import com.doschool.ahu.base.adapter.BaseRvAdapter;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

/**
 * Created by X on 2018/11/5
 */
public class AtUserAdapter2 extends BaseRvAdapter<SingleUserVO,AtUserHolder> {

    public AtUserAdapter2(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.atuser_item_layout;
    }

    @Override
    protected AtUserHolder onCreateHolder(ViewGroup parent, int viewType) {
        return AtUserHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void bindData(AtUserHolder holder, int position, SingleUserVO data) {
        holder.setAtHolder(context,data);

        //选择at用户
        RxView.clicks(holder.atuser_rl)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (onAtListener!=null){
                        String str = "[a=" + data.getUserId() + "]@" + data.getNickName() + "[/a]";
                        onAtListener.onAt(String.valueOf(data.getUserId()),str);
                    }
                });
    }

    private OnAtListener onAtListener;

    public void setOnAtListener(OnAtListener onAtListener) {
        this.onAtListener = onAtListener;
    }
}
