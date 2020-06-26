package com.doschool.ahu.widget.progress;

/**
 * Created by X on 2018/9/29
 */
public interface OnProgressListener {
    void onProgress(boolean isComplete, int percentage, long bytesRead, long totalBytes);
}
