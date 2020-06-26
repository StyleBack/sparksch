package com.doschool.ahu.xlhttps;

import com.blankj.utilcode.util.NetworkUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.base.BaseApplication;
import com.doschool.ahu.utils.XLToast;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by X on 2018/9/29
 */
public class XLDownLoad {

    public static final int GETS=0;
    public static final int POSTS=1;

    public static Callback.Cancelable dowanLoadFile(String url, String path, int method, Callback.ProgressCallback<File> callback){

        //判断是否联网
        if (!NetworkUtils.isConnected()){
            XLToast.showToast(BaseApplication.getContext().getResources().getString(R.string.unnetwork));
        }

        HttpMethod  httpMethod=null;
        if (method == GETS){
            httpMethod=HttpMethod.GET;
        }else {
            httpMethod=HttpMethod.POST;
        }

        RequestParams params=new RequestParams(url);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(path);
        Callback.Cancelable cancelable= x.http().request(httpMethod,params,callback);
        return cancelable;
    }
}
