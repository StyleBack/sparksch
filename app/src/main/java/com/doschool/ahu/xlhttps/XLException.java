package com.doschool.ahu.xlhttps;

import android.util.Log;

import com.doschool.ahu.R;
import com.doschool.ahu.base.BaseApplication;
import com.doschool.ahu.utils.XLToast;

import org.xutils.ex.HttpException;

/**
 * Created by X on 2018/7/19
 */
public class XLException {

    public static boolean ex(Throwable ex){

        if (ex instanceof HttpException){
            HttpException httpEx = (HttpException) ex;
            int responseCode = httpEx.getCode();
            String responseMsg = httpEx.getMessage();
            String errorResult = httpEx.getResult();
            Log.i("https:", "=======---onError---网络错误======:"
                    + "返回码：" + responseCode
                    + "；---错误信息：" + responseMsg
                    + "；---错误结果：" + errorResult);
            XLToast.showToast(BaseApplication.getInstance().getResources().getString(R.string.error_network));
            return true;
        }
        return false;
    }
}
