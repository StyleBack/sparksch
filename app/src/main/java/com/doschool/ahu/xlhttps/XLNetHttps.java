package com.doschool.ahu.xlhttps;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.base.BaseApplication;
import com.doschool.ahu.base.model.BaseModel;
import com.doschool.ahu.configfile.AppConfig;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.Map2String;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XLToast;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;

/**
 * Created by X on 2018/7/18
 * <p>
 * xutils简单请求封装
 */
public class XLNetHttps {

    private static final String AES_HEADER_A = "xlns21Dks901D92j";//固定的加密
    private static final String AES_TRANSFOR = "AES/ECB/PKCS5Padding";

    /**
     * 公共参数
     *
     * @return
     */
    public static ArrayMap<String, String> getBaseMap(Context context) {
        LoginDao loginDao = new LoginDao(context);
        ArrayMap<String, String> map = new ArrayMap<>();
        if (loginDao.getObject() != null) {
            if (loginDao.getObject().getUserDO() != null) {
                map.put("userId", String.valueOf(loginDao.getObject().getUserDO().getId()));// uid
            }
        }
        map.put("schoolId", String.valueOf(AppConfig.SCHOOL_ID));
        map.put("deviceType", "1");//区分设备系统  1---Android   2---ios
        map.put("deviceVersion", String.valueOf(AppConfig.getVersionCode()));//版本号
        return map;
    }

    //默认参数请求
    public static void requestNormal(String url, ArrayMap<String, String> maps,
                                     Class<? extends BaseModel> clazz,
                                     XLCallBack callBack) {
        //判断是否联网
        if (!NetworkUtils.isConnected()) {
            XLToast.showToast(BaseApplication.getContext().getResources().getString(R.string.unnetwork));
        }

        ArrayMap<String, String> asMap = new ArrayMap<>();
        for (String s : maps.keySet()) {
            asMap.put(s, maps.get(s));
        }

        byte[] data = null;
        byte[] keys = null;
        try {
            data = Map2String.map2HttpParams(asMap).getBytes("UTF-8");
            keys = AES_HEADER_A.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] values = EncryptUtils.encryptAES2Base64(data, keys, AES_TRANSFOR, null);
        asMap.clear();
        asMap.put("param", new String(values));
        RequestParams params = new RequestParams(AppConfig.APP_BASE_URL + url);
        params.addHeader("cert", "a");//加密
        if (!asMap.isEmpty()) {
            for (String key : asMap.keySet()) {
                params.addBodyParameter(key, asMap.get(key));
            }
        }
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseModel response = XLGson.fromJosn(result, clazz);
                if (response.getCode() != 0) {
                    XLToast.showToast(response.getMessage());
                }
                callBack.XLSucc(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (!XLException.ex(ex)) {
                    callBack.XLError(ex, isOnCallback);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                callBack.XLCancle(cex);
            }

            @Override
            public void onFinished() {
                callBack.XLFinish();
            }
        });
    }


    /**
     * post请求
     */
    public static void request(final String url,
                               final ArrayMap<String, String> maps,
                               final Class<? extends BaseModel> clazz,
                               final XLCallBack callBack) {

        //判断是否联网
        if (!NetworkUtils.isConnected()) {
            XLToast.showToast(BaseApplication.getContext().getResources().getString(R.string.unnetwork));
        }

        ArrayMap<String, String> asMap = new ArrayMap<>();
        for (String s : maps.keySet()) {
            asMap.put(s, maps.get(s));
        }

        //aes+base64加密 maps取得asMap
        LoginDao loginDao = new LoginDao(BaseApplication.getContext());
        byte[] data = null;
        byte[] keys = null;
        try {
            data = Map2String.map2HttpParams(asMap).getBytes("UTF-8");
            keys = loginDao.getObject().getUserAccountDO().getKey().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] values = EncryptUtils.encryptAES2Base64(data, keys, AES_TRANSFOR, null);
        asMap.clear();
        asMap.put("param", new String(values));
        RequestParams params = new RequestParams(AppConfig.APP_BASE_URL + url);
        params.addHeader("cert", "aaa");//加密
        params.addHeader("t", loginDao.getObject().getUserAccountDO().getToken());
        if (!asMap.isEmpty()) {
            for (String key : asMap.keySet()) {
                params.addBodyParameter(key, asMap.get(key));
            }
        }

        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseModel response = XLGson.fromJosn(result, clazz);
                if (response.getCode() != 0) {
                    XLToast.showToast(response.getMessage());
                }
                callBack.XLSucc(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (!XLException.ex(ex)) {
                    callBack.XLError(ex, isOnCallback);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                callBack.XLCancle(cex);
            }

            @Override
            public void onFinished() {
                callBack.XLFinish();
            }
        });
    }


    /**
     * post或get的缓存
     */
    public static void requestCache(final String url,
                                    final ArrayMap<String, String> maps,
                                    final Class<? extends BaseModel> clazz,
                                    final XLCacheCallBack cacheCallBack) {

        //判断是否联网
        if (!NetworkUtils.isConnected()) {
            XLToast.showToast(BaseApplication.getContext().getResources().getString(R.string.unnetwork));
        }

        ArrayMap<String, String> asMap = new ArrayMap<>();
        for (String s : maps.keySet()) {
            asMap.put(s, maps.get(s));
        }
        //aes+base64加密 maps取得asMap
        LoginDao loginDao = new LoginDao(BaseApplication.getContext());
        byte[] data = null;
        byte[] keys = null;
        try {
            data = Map2String.map2HttpParams(asMap).getBytes("UTF-8");
            keys = loginDao.getObject().getUserAccountDO().getKey().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] values = EncryptUtils.encryptAES2Base64(data, keys, AES_TRANSFOR, null);
        asMap.clear();
        asMap.put("param", new String(values));

        RequestParams params = new RequestParams(AppConfig.APP_BASE_URL + url);
        params.addHeader("cert", "aaa");//加密
        params.addHeader("t", loginDao.getObject().getUserAccountDO().getToken());
        if (!asMap.isEmpty()) {
            for (String key : asMap.keySet()) {
                params.addBodyParameter(key, asMap.get(key));
            }
        }
        x.http().request(HttpMethod.POST, params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    BaseModel response = XLGson.fromJosn(result, clazz);
                    if (response.getCode() != 0) {
                        XLToast.showToast(response.getMessage());
                    }
                    cacheCallBack.XLSucc(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (!XLException.ex(ex)) {
                    cacheCallBack.XLError(ex, isOnCallback);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                cacheCallBack.XLCancle(cex);
            }

            @Override
            public void onFinished() {
                cacheCallBack.XLFinish();
            }

            @Override
            public boolean onCache(String result) {
                return cacheCallBack.XLCache(result);
            }
        });
    }
}
