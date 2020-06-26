package com.doschool.ahu.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.doschool.ahu.widget.GlideApp;

/**
 * Created by X on 2018/7/18
 *
 * glide加载图片方法 ImageLoader
 */
public class XLGlideLoader {

    public static int RESULT_OK = 0x003;
    public static int RESULT_FAIL = 0x004;

    public XLGlideLoader() {
    }

    /**
     * 加载资源文件
     * @param iv
     * @param resId
     */
    public static void loadImageById(ImageView iv, int resId) {
        GlideApp.with(iv.getContext())
                .load(resId)
                .transition(new DrawableTransitionOptions().crossFade(200))
                .into(iv);
    }

    /**
     *加载url
     * @param iv
     * @param url
     */
    public static void loadImageByUrl(ImageView iv, String url) {
        GlideApp.with(iv.getContext())
                .load(url)
                .transition(new DrawableTransitionOptions().crossFade(200))
                .into(iv);
    }

    public static void loadGuide(ImageView iv, String url, int holderId){
        GlideApp.with(iv.getContext())
                .load(url)
                .centerCrop()
                .error(holderId)
                .placeholder(holderId)
                .transition(new DrawableTransitionOptions().crossFade(200))
                .into(iv);
    }


    /**
     * 加载图片  给默认加载图片
     * @param iv
     * @param url
     * @param errId
     * @param holderId
     */
    public static void loadWithHolder(ImageView iv, String url, int errId, int holderId) {
        GlideApp.with(iv.getContext())
                .load(url)
                .centerCrop()
                .error(errId)
                .placeholder(holderId)
                .transition(new DrawableTransitionOptions().crossFade(200))
                .into(iv);
    }

    /**
     *
     * @param iv
     * @param url
     */
    public static void loadImageByUrlNoType(ImageView iv, String url) {
        GlideApp.with(iv.getContext())
                .load(url)
                .transition(new DrawableTransitionOptions().crossFade(200))
                .into(iv);
    }

    /**
     * 加载圆形图
     * @param iv
     * @param url
     */
    public static void loadCircleImage(ImageView iv, String url) {
        GlideApp.with(iv.getContext())
                .load(url)
                .transform(new MultiTransformation<>(new CircleCrop()))
                .into(iv);
    }

    /**
     * 加载本地圆形图
     * @param iv
     * @param id
     */
    public static void loadCircleImage(ImageView iv, int id) {
        GlideApp.with(iv.getContext())
                .load(id)
                .transform(new MultiTransformation<>(new CircleCrop()))
                .into(iv);
    }

    /**
     * 默认圆形图
     * @param iv
     * @param url
     * @param errId
     * @param holderId
     */
    public static void circleWithHolder(ImageView iv, String url, int errId, int holderId) {
        GlideApp.with(iv.getContext())
                .load(url)
                .error(errId)
                .placeholder(holderId)
                .transform(new MultiTransformation<>(new CircleCrop()))
                .into(iv);
    }

    /**
     * 圆角图
     * @param iv
     * @param url
     */
    public static void loadCornerImage(ImageView iv, String url) {
        GlideApp.with(iv.getContext())
                .load(url)
                .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(10)))
                .into(iv);
    }

    /**
     * 默认圆角图
     * @param iv
     * @param url
     * @param errId
     * @param holderId
     */
    public static void cornerHolder(ImageView iv, String url, int errId, int holderId) {
        GlideApp.with(iv.getContext())
                .load(url)
                .error(errId)
                .placeholder(holderId)
                .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(10)))
                .into(iv);
    }

    /**
     * 自定义圆角的大小
     * @param iv
     * @param url
     * @param corner
     */
    public static void loadCornerImage(ImageView iv, String url, int corner) {
        GlideApp.with(iv.getContext())
                .load(url)
                .transition(new DrawableTransitionOptions().crossFade(200))
                .transform(new RoundedCorners(corner))
                .into(iv);
    }

    /**
     * 本地  自定义圆角大小
     * @param iv
     * @param d
     * @param corner
     */
    public static void loadCornerImage(ImageView iv, Drawable d, int corner) {
        GlideApp.with(iv.getContext())
                .load(d)
                .transition(new DrawableTransitionOptions().crossFade(200))
                .transform(new RoundedCorners(corner))
                .into(iv);
    }

    /**
     * 加载背景图
     * @param v
     * @param url
     */
    public static void loadViewBg(final View v, String url) {
        GlideApp.with(v).load(url).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                v.setBackground(resource);
            }
        });
    }

    /**
     * 预加载缩略图
     * @param view
     * @param url
     * @param thum
     */
    public static void loadThumb(ImageView view,String url,String thum){
        GlideApp.with(view.getContext())
                .load(url)
                .thumbnail(GlideApp.with(view.getContext()).load(thum))
                .transition(new DrawableTransitionOptions().crossFade(200))
                .into(view);
    }

    /**
     * 回调监听
     *
     * @param iv       iv
     * @param url      url
     * @param listener 监听回调
     */
    public static void loadWithListener(ImageView iv, String url, final LoadListener listener) {
        GlideApp.with(iv.getContext())
                .load(url)
                .transform(new MultiTransformation<>(new CenterCrop()))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        listener.onLoadFailed();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        listener.onResourceReady();
                        return false;
                    }
                })
                .into(iv);
    }

    public static void loadOriginalScaleImage(final ImageView iv, String url, final int width) {
        GlideApp.with(iv.getContext())
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(@NonNull final Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        final float scale = ((float) bitmap.getHeight()) / ((float) bitmap.getWidth());
                        iv.post(new Runnable() {
                            @Override
                            public void run() {
                                ViewGroup.LayoutParams lp = iv.getLayoutParams();
                                lp.width = width;
                                lp.height = (int) (scale * width);
                                iv.setLayoutParams(lp);
                                iv.setImageBitmap(bitmap);
                            }
                        });
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        final float scale = 1;
                        iv.post(new Runnable() {
                            @Override
                            public void run() {
                                ViewGroup.LayoutParams lp = iv.getLayoutParams();
                                lp.width = width;
                                lp.height = (int) (scale * width);
                                iv.setLayoutParams(lp);
                            }
                        });
                    }
                });
    }

    public static void loadScaleWithHolder(final ImageView iv, String url, final int width, final int errId, int holderId) {
        GlideApp.with(iv.getContext())
                .asBitmap()
                .load(url)
                .error(errId)
                .placeholder(holderId)
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(@NonNull final Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        final float scale = ((float) bitmap.getHeight()) / ((float) bitmap.getWidth());
                        iv.post(new Runnable() {
                            @Override
                            public void run() {
                                ViewGroup.LayoutParams lp = iv.getLayoutParams();
                                lp.width = width;
                                lp.height = (int) (scale * width);
                                iv.setLayoutParams(lp);
                                iv.setImageBitmap(bitmap);
                            }
                        });
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        final float scale = 1;
                        iv.post(new Runnable() {
                            @Override
                            public void run() {
                                ViewGroup.LayoutParams lp = iv.getLayoutParams();
                                lp.width = width;
                                lp.height = (int) (scale * width);
                                iv.setLayoutParams(lp);
                                iv.setImageResource(errId);
                            }
                        });
                    }
                });
    }

    public static void loadOriginalScaleImage(final ImageView iv, String url, final int width, final View otherView) {
        GlideApp.with(iv.getContext())
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(@NonNull final Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        final float scale = ((float) bitmap.getHeight()) / ((float) bitmap.getWidth());
                        iv.post(new Runnable() {
                            @Override
                            public void run() {
                                ViewGroup.LayoutParams lp = iv.getLayoutParams();
                                lp.width = width;
                                lp.height = (int) (scale * width);
                                iv.setLayoutParams(lp);
                                iv.setImageBitmap(bitmap);
                                if (!otherView.isShown()) {
                                    otherView.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                });
    }

    public interface LoadListener {

        void onLoadFailed();

        void onResourceReady();
    }

    public interface SaveListener {
        void onResult(int resultCode, String path);
    }
}
