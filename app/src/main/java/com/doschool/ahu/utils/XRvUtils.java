package com.doschool.ahu.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.doschool.ahu.R;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

/**
 * Created by X on 2018/8/22
 */
public class XRvUtils {

    public static void initRv(XRecyclerView xRecyclerView, LinearLayoutManager layoutManager,
                              int orientation,boolean upTime,boolean isrefresh,boolean isloading){

        if (orientation== LinearLayoutManager.VERTICAL || orientation==LinearLayoutManager.HORIZONTAL){
            layoutManager.setOrientation(orientation);
        }
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        xRecyclerView.setItemAnimator(null);
        xRecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(upTime);
        xRecyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);
        xRecyclerView.setPullRefreshEnabled(isrefresh);
        xRecyclerView.setLoadingMoreEnabled(isloading);
        if (isrefresh){
            xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        }
        if (isloading){
            xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);
        }
    }

    public static void destroyRv(XRecyclerView xRecyclerView){
        if (xRecyclerView!=null){
            xRecyclerView.destroy();
            xRecyclerView=null;
        }
    }
}
