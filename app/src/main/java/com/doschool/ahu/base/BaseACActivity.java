package com.doschool.ahu.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.blankj.utilcode.util.ActivityUtils;
import com.bugtags.library.Bugtags;
import com.doschool.ahu.utils.XLToast;
import com.umeng.message.PushAgent;

/**
 * Created by X on 2018/7/4.
 *
 * 基类
 */

public abstract class BaseACActivity extends AppCompatActivity {


    protected long exitTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWindow();

        if (getContentLayoutID()!=0){
            setContentView(getContentLayoutID());
        }else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }
//        ActivityUtil.addActivity(this);
        initViewAndEvents(savedInstanceState);
        //初始化友盟应用
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注：回调 1
        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2
        Bugtags.onPause(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        DetoryViewAndThing();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
        Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isExitBack()){
            if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
                if((System.currentTimeMillis()-exitTime) > 2000){
                    XLToast.showToast("再按一次返回键退出程序");
                    exitTime = System.currentTimeMillis();
                } else {
//                    ActivityUtil.finishAllActivity();
                    ActivityUtils.finishAllActivities();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //资源布局
    protected abstract int getContentLayoutID();
    //初始化、数据处理。。。
    protected abstract void initViewAndEvents(Bundle savedInstanceState);
    //onDestroy实现
    protected abstract void DetoryViewAndThing();

    //窗口设置
    protected void initWindow(){

    }

    //是否退出程序
    protected boolean isExitBack(){
        return  false;
    }
}
