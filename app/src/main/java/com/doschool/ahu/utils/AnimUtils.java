package com.doschool.ahu.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by X on 2018/9/12
 */
public class AnimUtils {

    public static TranslateAnimation showC(){
        TranslateAnimation showAction= new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        showAction.setDuration(500);
        return showAction;
    }

    public static TranslateAnimation hideC(){
        TranslateAnimation hideAction= new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);
        hideAction.setDuration(500);
        return hideAction;
    }

    public enum AnimationState{
        STATE_SHOW,
        STATE_HIDDEN
    }
    /**
     * 渐隐渐现动画
     * @param view 需要实现动画的对象
     * @param state 需要实现的状态
     * @param duration 动画实现的时长（ms）
     */
    public static void showAndHiddenAnimation(final View view,AnimationState state,long duration){
        float start = 0f;
        float end = 0f;
        if(state == AnimationState.STATE_SHOW){
            end = 1f;
            view.setVisibility(View.VISIBLE);
        } else
        if(state == AnimationState.STATE_HIDDEN){
            start = 1f;
            view.setVisibility(View.GONE);
        }
        AlphaAnimation animation = new AlphaAnimation(start, end);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }
        });
        view.setAnimation(animation);
        animation.start();
    }
}
