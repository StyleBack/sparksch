package com.doschool.ahu.widget.progress.match;

import com.bumptech.glide.load.engine.GlideException;

/**
 * Created by X on 2018/9/29
 */
public interface OnGlideImageViewListener {

    void onProgress(int percent, boolean isDone, GlideException exception);
}
