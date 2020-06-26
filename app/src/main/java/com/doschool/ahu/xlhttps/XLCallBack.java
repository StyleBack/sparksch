package com.doschool.ahu.xlhttps;

import org.xutils.common.Callback;

/**
 * Created by X on 2018/7/18
 * 请求回调
 */
public interface XLCallBack {
    void XLSucc(String result);
    void XLError(Throwable ex, boolean isOnCallback);
    void XLCancle(Callback.CancelledException cex);
    void XLFinish();
}
