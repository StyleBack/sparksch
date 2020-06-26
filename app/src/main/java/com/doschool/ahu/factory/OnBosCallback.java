package com.doschool.ahu.factory;

import com.baidubce.services.bos.model.PutObjectRequest;

/**
 * Created by X on 2018/9/20
 */
public interface OnBosCallback {
    void onProgress(PutObjectRequest request, long currentSize, long totalSize);
}
