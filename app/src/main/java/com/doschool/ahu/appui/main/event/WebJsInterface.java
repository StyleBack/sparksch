package com.doschool.ahu.appui.main.event;

import android.webkit.JavascriptInterface;

import com.doschool.ahu.appui.main.ui.activity.WebActivity;


/**
 * Created by X on 2018/12/7.
 */
public class WebJsInterface {

    private WebActivity webActivity;
    public WebJsInterface(WebActivity webActivity) {
        this.webActivity = webActivity;
    }

    @JavascriptInterface
    public void finishWindow(){
        webActivity.finish();
    }

    @JavascriptInterface
    public void jsbridge_qrscan(String menu){
        if (onQRCode!=null){
            onQRCode.onCode(menu);
        }
    }

    private OnQRCode onQRCode;

    public void setOnQRCode(OnQRCode onQRCode) {
        this.onQRCode = onQRCode;
    }

    public interface OnQRCode{
        void onCode(String menu);
    }
}
