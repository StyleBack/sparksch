package com.doschool.ahu.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.x;

/**
 * Created by X on 2018/7/4
 *
 * 基类的子类
 */
public abstract class BaseFragment extends BaseACFragment {

    public View childView;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getContentLayoutID()!=0){
            childView=inflater.inflate(getContentLayoutID(),container,false);
        }else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }
        setStatusBar();
        x.view().inject(this,childView);
        return childView;
    }

    protected abstract int getContentLayoutID();

    protected void setStatusBar(){

    }
}
