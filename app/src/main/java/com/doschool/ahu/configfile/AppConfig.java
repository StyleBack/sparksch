package com.doschool.ahu.configfile;

import android.os.Environment;

import com.blankj.utilcode.util.AppUtils;
import com.doschool.ahu.BuildConfig;

import java.io.File;

/**
 * Created by X on 2018/7/19
 *
 * app中请求地址  获取版本  设备号等。。。配置
 */
public class AppConfig {

    public static final int SCHOOL_ID=1;
    //bugly  appid
    public static final String BUGLY_APPID="86be264d23";
    //umeng key和secert
    public static final String UMENG_APPKEY="5aa0e4cdf43e4817c8000039";
    public static final String UMENG_MESSAGE_SECRET="996abdcecf437ec7460d9b6ddf9330c8";

    public static final String APP_UPDATE_ID="APP_Channel_01";
    public static final String APP_UPDATE_NAME="APP更新";
    
    public static final String APP_BASE_URL= BuildConfig.BASE_URL;//环境地址配置
    public static final String APP_FILE_SAVE=BuildConfig.APPLICATION_ID;//文件存储

    public static final String SDCARD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String DEFAULT_CACHE_DIR_PATH = SDCARD_DIR+File.separator+APP_FILE_SAVE+ File.separator+"player";
    public static final String SDK_IMG=SDCARD_DIR+File.separator+APP_FILE_SAVE+ File.separator+"videoImg";
    public static final String SDK_VIDEO=SDCARD_DIR+File.separator+APP_FILE_SAVE+ File.separator+"videos";
    public static final String DOWNLOAD_FILE=SDCARD_DIR+ File.separator+APP_FILE_SAVE+File.separator+"download"+File.separator;
    public static final String DOWNLOAD_FILE_APK=SDCARD_DIR+ File.separator+APP_FILE_SAVE+File.separator+"download"+File.separator+"apk"+File.separator;
    public static final String SAVE_SCREEN_IMG=SDCARD_DIR+File.separator+APP_FILE_SAVE+ File.separator+"screenshot"+ File.separator;//截屏存储

    public static final String getAppName() {//获取app名称
        return AppUtils.getAppName();//BaseApplication.getInstance().getResources().getString(R.string.app_name)
    }

    public static final String getVersionName()//获取版本名称
    {
        return AppUtils.getAppVersionName();
    }

    public static final int getVersionCode()//获取版本号(内部识别号)
    {
        return AppUtils.getAppVersionCode();
    }

}
