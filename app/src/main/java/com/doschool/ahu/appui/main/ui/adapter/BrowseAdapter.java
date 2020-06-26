package com.doschool.ahu.appui.main.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.activity.BrowseImageActivity;
import com.doschool.ahu.utils.Utils;
import com.doschool.ahu.widget.GlideApp;
import com.doschool.ahu.widget.progress.CircleProgressView;
import com.doschool.ahu.widget.progress.match.OnGlideImageViewListener;
import com.doschool.ahu.widget.progress.match.GlideImageLoader;
import com.github.chrisbanes.photoview.OnOutsidePhotoTapListener;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by X on 2018/9/29
 */
public class BrowseAdapter extends PagerAdapter implements OnPhotoTapListener, OnOutsidePhotoTapListener {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> images;
    private List<String> thumbs;
    private SparseArray<PhotoView> photoViews = new SparseArray<>();

    public BrowseAdapter(Context mContext, List<String> images,List<String> thumbs) {
        this.mContext = mContext;
        this.images = images;
        this.thumbs=thumbs;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
    }

    public PhotoView getPhotoView(int index) {
        return photoViews.get(index);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.item_photoview, container, false);
        CircleProgressView progressView = (CircleProgressView) view.findViewById(R.id.progressView);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.photoView);
        photoView.setOnPhotoTapListener(this);
        photoView.setOnOutsidePhotoTapListener(this);
        photoViews.put(position, photoView);

        loadImage(thumbs.get(position),images.get(position),photoView,progressView);
        container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return view;
    }

    private void loadImage(String thumb,String img,PhotoView photoView,CircleProgressView progressView){
        GlideImageLoader imageLoader = GlideImageLoader.create(photoView);

        RequestOptions requestOptions = imageLoader.requestOptions(R.color.transparent)
                .centerCrop()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        final RequestBuilder<Drawable> requestBuilder = imageLoader.requestBuilder(img, requestOptions)
                .thumbnail(GlideApp.with(mContext) // 加载缩略图
                .load(thumb)
                .apply(requestOptions))
                .transition(DrawableTransitionOptions.withCrossFade());
        requestBuilder.into(new SimpleTarget<Drawable>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (resource.getIntrinsicHeight() > Utils.getWindowHeight(mContext)) {
                    photoView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }
                requestBuilder.into(photoView);
            }
        });
        imageLoader.setOnGlideImageViewListener(thumb, (percent, isDone, exception) -> {
            progressView.setProgress(percent);
            progressView.setVisibility(isDone ? View.GONE : View.VISIBLE);
        });
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public void onPhotoTap(ImageView view, float x, float y) {
        ((BrowseImageActivity) mContext).finish();
    }

    @Override
    public void onOutsidePhotoTap(ImageView imageView) {
        ((BrowseImageActivity) mContext).finish();
    }
}
