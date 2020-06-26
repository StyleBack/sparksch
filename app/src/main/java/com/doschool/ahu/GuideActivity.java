package com.doschool.ahu;

import android.os.Bundle;

import com.blankj.utilcode.util.SPUtils;
import com.doschool.ahu.appui.reglogin.ui.LoginActivity;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.widget.BannerGlideImageLoader;
import com.jaeger.library.StatusBarUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X on 2018/7/13
 *뭐 공부 해요
 * 引导页
 * κατευθείαν στον γαλαξία
 */
public class GuideActivity extends BaseActivity {


    private List<Integer> list=new ArrayList<>();

    @ViewInject(R.id.guide_banner)
    private Banner guide_banner;


    @Override
    protected int getContentLayoutID() {
        return R.layout.guide_act_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        list.clear();
        list.add(R.mipmap.guide_f);
        list.add(R.mipmap.guide_s);
        list.add(R.mipmap.guide_t);
        list.add(R.mipmap.guide_z);

        gotoLog();
    }

    private void gotoLog(){
        guide_banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                //设置图片加载器
                .setImageLoader(new BannerGlideImageLoader())
                //设置图片集合
                .setImages(list)
                //设置banner动画效果
                .setBannerAnimation(Transformer.Default)
                //设置标题集合（当banner样式有显示title时）
//                .setBannerTitles(titles)
                //设置自动轮播，默认为true
                .isAutoPlay(false)
                //设置轮播时间
//                .setDelayTime(5000)
                //是否循环

                //设置指示器位置（当banner模式中有指示器时）
                .setIndicatorGravity(BannerConfig.CENTER)
                .setOnBannerListener(position -> {
                    SPUtils.getInstance().put("isstart",false);
                        gotoLogin();
                })
                .start();
    }

    private void gotoLogin(){
        IntentUtil.toActivity(this,null,LoginActivity.class);
        finish();
    }

    @Override
    protected void DetoryViewAndThing() {

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this,0,null);
        StatusBarUtil.setLightMode(this);
    }
}
