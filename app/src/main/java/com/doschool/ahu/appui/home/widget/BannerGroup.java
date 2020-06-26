package com.doschool.ahu.appui.home.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.AppBannerDo;
import com.doschool.ahu.widget.DotGroup;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by X on 2018/10/17
 */
public class BannerGroup extends FrameLayout {

    private final int DELAY_TIME = 5000;//自动轮播时间
    ViewPager viewpager;

    DotGroup dotgroup;

    private Handler handler;
    private MyPagerAdapter adapter;
    private int curPage;
    List<AppBannerDo.BannerData> list;

    public BannerGroup(Context context) {
        super(context);
        handler=new Handler();
        LayoutInflater.from(getContext()).inflate(R.layout.widget_bannergroup, this);
        viewpager=findViewById(R.id.bannerviewpager);
        dotgroup=findViewById(R.id.bannerdotGroup);
        setViewPagerScrollSpeed(viewpager);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void updateUI(List<AppBannerDo.BannerData> list) {
        if(this.list==null || this.list.size()!=list.size())
            curPage=0;
        this.list=list;
        try {
            if (list.size() == 0) {
                this.setVisibility(View.GONE);
            } else {
                this.setVisibility(View.VISIBLE);
                if (list.size() == 1) {
                    dotgroup.setVisibility(View.GONE);
                } else {
                    dotgroup.setVisibility(View.VISIBLE);
                }

//                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewpager.getLayoutParams();
//                float screenWidth= Utils.getWindowWidth(BaseApplication.getContext());
//                GlideApp.with(BaseApplication.getContext())
//                        .asBitmap()
//                        .load(list.get(0).getImage())
//                        .into(new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                               int width=resource.getWidth();
//                               int hight=resource.getHeight();
//                                //确定banner高度，按照图片等比例放大
//                                lp.height = (int) (screenWidth*width/hight);
//                            }
//                        });
//                viewpager.setLayoutParams(lp);
                if (adapter==null){
                    adapter = new MyPagerAdapter(list);
                    viewpager.setAdapter(adapter);
                    viewpager.setOffscreenPageLimit(4);
                    viewpager.setCurrentItem(list.size() * 100+curPage);

                    dotgroup.init(viewpager.getAdapter().getCount() / 200,curPage, ConvertUtils.dp2px(6),
                            R.drawable.dot_unfocus,R.drawable.dot_focus);

                    MyPageChangeListenter mc = new MyPageChangeListenter(list);
                    viewpager.addOnPageChangeListener(mc);
                }else {
                    adapter.notifyDataSetChanged();
                }

                setAuto();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置自动滚动
     */
    public void setAuto() {
        handler.postDelayed(mTask, DELAY_TIME);
    }

    /**
     * 取消自动轮播任务
     */
    private void stopAuto() {
        handler.removeCallbacks(mTask);//取消任务
    }

    //取消轮播任务
    public void cancel(){
        handler.removeCallbacks(mTask);
    }

    /**
     * 定时任务
     */
    Runnable mTask = new Runnable() {
        @Override
        public void run() {
            if (list.size()>1){
                int count = adapter.getCount();
                if (count > 1) {
                    int index = viewpager.getCurrentItem();
                    index = (index + 1) % count;
                    viewpager.setCurrentItem(index, true);
                }
                handler.postDelayed(this, DELAY_TIME);
            }
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            stopAuto();
        } else if (action == MotionEvent.ACTION_UP) {
            setAuto();
        }
        return super.dispatchTouchEvent(ev);
    }

    class MyPagerAdapter extends PagerAdapter {

        private List<AppBannerDo.BannerData> list;

        public MyPagerAdapter(List<AppBannerDo.BannerData> list) {
            this.list = list;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return list.size() == 1 ? 2 *200: list.size() * 200;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int p = position % list.size();
            AppBannerDo.BannerData banner = list.get(p);
            BannerItem bannerItem=new BannerItem(getContext());
            bannerItem.update(banner);
            container.addView(bannerItem);
            return bannerItem;
        }
    }

    class MyPageChangeListenter implements ViewPager.OnPageChangeListener {

        private List<AppBannerDo.BannerData> list;

        public MyPageChangeListenter(List<AppBannerDo.BannerData> list) {
            this.list = list;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            position %= (list.size());
            dotgroup.setCurrentItem(position);
            AppBannerDo.BannerData banner=list.get(position);
            curPage=position;

//            if(banner.getType()==3){
//                dotgroup.setVisibility(View.GONE);
//            }
//            else
            if (list.size()>1){
                dotgroup.setVisibility(View.VISIBLE);
            }else {
                dotgroup.setVisibility(View.GONE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void setViewPagerScrollSpeed(ViewPager viewpager){
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            SpeedController scroller = new SpeedController( viewpager.getContext( ) );
            mScroller.set( viewpager, scroller);
        }catch(NoSuchFieldException e){

        }catch (IllegalArgumentException e){

        }catch (IllegalAccessException e){

        }
    }

}
