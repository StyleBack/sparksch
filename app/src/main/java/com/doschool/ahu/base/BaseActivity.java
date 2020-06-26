package com.doschool.ahu.base;


import com.doschool.ahu.R;
import com.jaeger.library.StatusBarUtil;

import org.xutils.x;

/**
 * Created by X on 2018/7/4.
 *
 * 基类的子类   继承此类，实现相应的方法
 */

public abstract class BaseActivity extends BaseACActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        x.view().inject(this);
        setStatusBar();
    }

    protected void setStatusBar(){
        StatusBarUtil.setColor(this,getResources().getColor(R.color.white),0);
        StatusBarUtil.setLightMode(this);
    }
}
