package com.doschool.ahu.appui.main.ui.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.adapter.ImagesAdapter;
import com.doschool.ahu.appui.main.widget.FixedViewPager;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.utils.ColorUtil;
import com.doschool.ahu.utils.Utils;
import com.doschool.ahu.widget.nine.ImageData;
import com.github.chrisbanes.photoview.PhotoView;
import com.jaeger.library.StatusBarUtil;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by X on 2018/9/29
 */
public class PhotoImageActivity extends BaseActivity implements ViewTreeObserver.OnPreDrawListener {


    public static final String IMAGE_ATTR = "image_attr";
    public static final String CUR_POSITION = "cur_position";
    public static final int ANIM_DURATION = 300; // ms

    @ViewInject(R.id.fix_vp)
    private FixedViewPager viewPager;

    @ViewInject(R.id.tv_tip)
    private TextView tv_tip;

    @ViewInject(R.id.rootView)
    private RelativeLayout rootView;
    private ImagesAdapter mAdapter;
    private List<ImageData> imageAttrs;
    private boolean isAnimating;

    private int curPosition;
    private int screenWidth;
    private int screenHeight;
    private float scaleX;
    private float scaleY;
    private float translationX;
    private float translationY;

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_photo_layout;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this,0,null);
        StatusBarUtil.setDarkMode(this);
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        init();
    }

    private void init(){
        screenWidth = Utils.getWindowWidth(this);
        screenHeight = Utils.getWindowHeight(this);
        Intent intent = getIntent();
        imageAttrs = (List<ImageData>) intent.getSerializableExtra(IMAGE_ATTR);
        curPosition = intent.getIntExtra(CUR_POSITION, 0);
        tv_tip.setText(String.format(getString(R.string.image_index), (curPosition + 1), imageAttrs.size()));

        mAdapter = new ImagesAdapter(this, imageAttrs);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(curPosition);
        viewPager.getViewTreeObserver().addOnPreDrawListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                curPosition = position;
                tv_tip.setText(String.format(getString(R.string.image_index), (curPosition + 1), imageAttrs.size()));
            }
        });
    }

    private void initImageAttr(PhotoView photoView, ImageData attr, boolean isFinish) {
        int originalWidth = attr.width;
        int originalHeight = attr.height;
        int originalCenterX = attr.startX + originalWidth / 2;
        int originalCenterY = attr.startY + originalHeight / 2;

        float widthRatio = screenWidth * 1.0f / attr.realWidth;
        float heightRatio = screenHeight * 1.0f / attr.realHeight;
        int finalHeight = (int) (attr.realHeight * widthRatio);
        int finalWidth = screenWidth; //imageAttrs.size() == 1 ? screenWidth : finalHeight;

        scaleX = originalWidth * 1.0f / finalWidth;
        scaleY = originalHeight * 1.0f / finalHeight;
        translationX = originalCenterX - screenWidth / 2;
        translationY = originalCenterY - screenHeight / 2;
    }

    @Override
    public boolean onPreDraw() {
        if (isAnimating) return true;
        rootView.getViewTreeObserver().removeOnPreDrawListener(this);
        PhotoView photoView = mAdapter.getPhotoView(curPosition);
        ImageData attr = imageAttrs.get(curPosition);
        initImageAttr(photoView, attr, false);

        translateXAnim(photoView, translationX, 0);
        translateYAnim(photoView, translationY, 0);
        scaleXAnim(photoView, scaleX, 1);
        scaleYAnim(photoView, scaleY, 1);
        setBackgroundColor(0f, 1f, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        return true;
    }

    @Override
    protected void DetoryViewAndThing() {

    }

    @Override
    public void onBackPressed() {
        finishWithAnim();
    }

    public void finishWithAnim() {
        if (isAnimating) return;
        PhotoView photoView = mAdapter.getPhotoView(curPosition);
        photoView.setScale(1f);
        ImageData attr = imageAttrs.get(curPosition);
        initImageAttr(photoView, attr, true);

        translateXAnim(photoView, 0, translationX);
        translateYAnim(photoView, 0, translationY);
        scaleXAnim(photoView, 1, scaleX);
        scaleYAnim(photoView, 1, scaleY);
        setBackgroundColor(1f, 0f, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
                finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private void translateXAnim(PhotoView photoView, float from, float to) {
        ValueAnimator anim = ValueAnimator.ofFloat(from, to);
        anim.addUpdateListener(it -> photoView.setX((Float) it.getAnimatedValue()));
        anim.setDuration(ANIM_DURATION);
        anim.start();
    }

    private void translateYAnim(PhotoView photoView, float from, float to) {
        ValueAnimator anim = ValueAnimator.ofFloat(from, to);
        anim.addUpdateListener(it -> photoView.setY((Float) it.getAnimatedValue()));
        anim.setDuration(ANIM_DURATION);
        anim.start();
    }

    private void scaleXAnim(PhotoView photoView, float from, float to) {
        ValueAnimator anim = ValueAnimator.ofFloat(from, to);
        anim.addUpdateListener(it -> photoView.setScaleX((Float) it.getAnimatedValue()));
        anim.setDuration(ANIM_DURATION);
        anim.start();
    }

    private void scaleYAnim(PhotoView photoView, float from, float to) {
        ValueAnimator anim = ValueAnimator.ofFloat(from, to);
        anim.addUpdateListener(it -> photoView.setScaleY((Float) it.getAnimatedValue()));
        anim.setDuration(ANIM_DURATION);
        anim.start();
    }

    private void setBackgroundColor(float from, float to, Animator.AnimatorListener listener) {
        ValueAnimator anim = ValueAnimator.ofFloat(from, to);
        anim.addUpdateListener(it -> rootView.setBackgroundColor(ColorUtil.evaluate((Float) it.getAnimatedValue(), Color.TRANSPARENT, Color.BLACK)));
        anim.setDuration(ANIM_DURATION);
        if (listener != null) {
            anim.addListener(listener);
        }
        anim.start();
    }
}
