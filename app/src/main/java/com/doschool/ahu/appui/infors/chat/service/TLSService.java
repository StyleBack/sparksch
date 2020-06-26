package com.doschool.ahu.appui.infors.chat.service;

import android.content.Context;

import tencent.tls.platform.TLSLoginHelper;
import tencent.tls.platform.TLSUserInfo;

/**
 * Created by X on 2018/8/25
 */
public class TLSService {

    private TLSLoginHelper loginHelper;
    private static TLSService tlsService = null;
    private static int lastErrno = -1;
    private TLSService(){}

    public static TLSService getInstance() {
        if (tlsService == null) {
            tlsService = new TLSService();
        }
        return tlsService;
    }

    public static void setLastErrno(int errno) {
        lastErrno = errno;
    }

    public static int getLastErrno() {
        return lastErrno;
    }

    /**
     * @function: 初始化TLS SDK, 必须在使用TLS SDK相关服务之前调用
     * @param context: 关联的activity
     * */
    public void initTlsSdk(Context context) {
//        QALSDKManager.getInstance().init(context.getApplicationContext(), (int)TLSConfiguration.SDK_APPID);
//        QALSDKManager.getInstance().setEnv(0);     // 0: sso正式环境 1: sso测试环境, 即beta环境

//        TIMManager.getInstance().init(context.getApplicationContext());
//        TIMManager.getInstance().setEnv(1);

        loginHelper = TLSLoginHelper.getInstance().init(context.getApplicationContext(),
                TLSConfiguration.SDK_APPID, TLSConfiguration.ACCOUNT_TYPE, TLSConfiguration.APP_VERSION);
        loginHelper.setTimeOut(TLSConfiguration.TIMEOUT);
//        loginHelper.setLocalId(TLSConfiguration.LANGUAGE_CODE);
//        loginHelper.setTestHost("", true);                   // 走sso
//        loginHelper.setTestHost("113.108.64.238", false);      // 不走sso

    }

    public void clearUserInfo(String identifier) {
        loginHelper.clearUserInfo(identifier);
        lastErrno = -1;
    }

    public String getUserSig(String identify) {
        return loginHelper.getUserSig(identify);
    }

    public TLSUserInfo getLastUserInfo() {
        return loginHelper.getLastUserInfo();
    }

    public String getLastUserIdentifier() {
        TLSUserInfo userInfo = getLastUserInfo();
        if (userInfo != null)
            return userInfo.identifier;
        else
            return null;
    }

    public boolean needLogin(String identifier) {
        if (identifier == null)
            return true;
        return loginHelper.needLogin(identifier);
    }
}
