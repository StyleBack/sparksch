package com.doschool.ahu.widget.xtablay;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.List;

/**
 * Created by X on 2019/1/10.
 */
public class IndicatorAdapter extends CommonNavigatorAdapter {


    private ViewPager viewPager;
    private List<String> mList;
    private float size;
    private int norColor;
    private int selectColor;
    private int indicColor;
    private int mode;
    private int height;
    private int width;
    private int radius;

    public IndicatorAdapter(ViewPager viewPager, List<String> mList) {
        this.viewPager = viewPager;
        this.mList = mList;
    }

    public IndicatorAdapter setXTextSize(float size){
        this.size=size;
        return this;
    }

    public IndicatorAdapter setNorColor(int norColor){
        this.norColor=norColor;
        return this;
    }

    public IndicatorAdapter setSelectColor(int selectColor){
        this.selectColor=selectColor;
        return this;
    }

    public IndicatorAdapter setIndicColor(int indicColor){
        this.indicColor=indicColor;
        return this;
    }

    public IndicatorAdapter setIndicMode(int mode){
        this.mode=mode;
        return this;
    }

    public IndicatorAdapter setIndicLineHeight(int height){
        this.height=height;
        return this;
    }

    public IndicatorAdapter setIndicLineWidth(int width){
        this.width=width;
        return this;
    }

    public IndicatorAdapter setIndicRadius(int radius){
        this.radius=radius;
        return this;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, int index) {
        SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
        simplePagerTitleView.setText(mList.get(index));
        simplePagerTitleView.setTextSize(size);
        simplePagerTitleView.setNormalColor(norColor);
        simplePagerTitleView.setSelectedColor(selectColor);
        simplePagerTitleView.setOnClickListener(v -> viewPager.setCurrentItem(index));
        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(mode);
        indicator.setLineHeight(UIUtil.dip2px(context, height));
        indicator.setLineWidth(UIUtil.dip2px(context, width));
        indicator.setRoundRadius(UIUtil.dip2px(context, radius));
        indicator.setStartInterpolator(new AccelerateInterpolator());
        indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
        indicator.setColors(indicColor);
        return indicator;
    }
}
