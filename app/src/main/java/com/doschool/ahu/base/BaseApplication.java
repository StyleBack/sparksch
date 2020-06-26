package com.doschool.ahu.base;


import android.content.Context;

import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;


import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;

import com.bugtags.library.Bugtags;
import com.doschool.ahu.MainActivity;
import com.doschool.ahu.R;

import com.doschool.ahu.WelActivity;
import com.doschool.ahu.appui.infors.chat.util.Foreground;

import com.doschool.ahu.configfile.AppConfig;
import com.doschool.ahu.factory.AppDoUrlFactory;


import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushListener;
import com.tencent.imsdk.TIMOfflinePushNotification;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;

import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;

import com.umeng.message.entity.UMessage;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import static com.doschool.ahu.configfile.AppConfig.BUGLY_APPID;

/**
 * Created by X on 2018/7/4.
 * <p>
 * app全局初始化
 */

public class BaseApplication extends MultiDexApplication {

    private static BaseApplication instance;

    public static BaseApplication getInstance() {
        return instance;
    }

    private static Context context;

    public static Context getContext() {
        return context;
    }

    PushAgent mPushAgent;

    public int getBadgeCount() {
        return badgeCount;
    }

    public void setBadgeCount(int badgeCount) {
        this.badgeCount = badgeCount;
    }

    private int badgeCount;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate() {
        super.onCreate();
        //跳过调相机权限问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        instance = this;
        context = getApplicationContext();
        Utils.init(this);
        initLog();
        intiXutil();
        initBugs();
        initIM();
        initBugly();
        initUmeng();
        clickNotification();
    }
    //bugtags
    private void initBugs(){
        Bugtags.start("22473196563051a1d102d79f0e637277", this, Bugtags.BTGInvocationEventBubble);
    }

    //log打印
    private void initLog() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("XLOGGER")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }

    //xutils
    private void intiXutil() {
        x.Ext.init(this);
        x.Ext.setDebug(false);// 是否输出debug日志, 开启debug会影响性能.
    }

    //初始化友盟
    private void initUmeng() {
        UMConfigure.init(context, UMConfigure.DEVICE_TYPE_PHONE, AppConfig.UMENG_MESSAGE_SECRET);
        mPushAgent = PushAgent.getInstance(context);
        mPushAgent.setDisplayNotificationNumber(0);
        mPushAgent.setResourcePackageName(AppUtils.getAppPackageName());
        //声音
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        //振动
        mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SERVER);
        //呼吸灯
        mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SERVER);
        mPushAgent.setNotificaitonOnForeground(true);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.d("__deviceToken", deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });

    }

    //腾讯im
    private void initIM() {
        Foreground.init(this);
        if (MsfSdkUtils.isMainProcess(this)) {
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {
                    if (notification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify) {
                        //消息被设置为需要提醒
                        notification.doNotify(getApplicationContext(), R.mipmap.app_launcher);
                    }
                }
            });
        }
    }

    //bugly
    private void initBugly() {
        // 获取当前包名
        String packageName = AppUtils.getAppPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setAppVersion(AppUtils.getAppVersionName());
        strategy.setAppPackageName(packageName);
//        strategy.setAppReportDelay(20000);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(context, BUGLY_APPID, true, strategy);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    //自定义友盟通知打开
    private void clickNotification() {
        //友盟通知
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            /**
             * 通知的回调方法
             * @param context
             * @param msg
             */
            @Override
            public void dealWithNotificationMessage(Context context, UMessage msg) {
                //调用super则会走通知展示流程，不调用super则不展示通知
                super.dealWithNotificationMessage(context, msg);
                if (msg!=null){
                    badgeCount++;
                    Intent intent=new Intent(context,BadgeService.class);
                    intent.putExtra("badgeCount",badgeCount);
                    startService(intent);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        //友盟通知打开
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void launchApp(Context context, UMessage uMessage) {
                super.launchApp(context, uMessage);
                if (uMessage != null) {
                    Logger.d("====" + uMessage.extra);
                    HashMap<String, String> extra = (HashMap<String, String>) uMessage.extra;
                    Iterator iterator = extra.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        String json = (String) entry.getValue();
                        if (ActivityUtils.isActivityExistsInStack(MainActivity.class)) {
                            ActivityUtils.finishOtherActivities(MainActivity.class);
                            AppDoUrlFactory.jumpActivity(context, json, "", "");
                        } else {
                            Intent welIntent = new Intent(context, WelActivity.class);
                            welIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            welIntent.putExtra("json", json);
                            startActivity(welIntent);
                        }
                    }
                    badgeCount=0;
                    Intent intent=new Intent(context,BadgeService.class);
                    intent.putExtra("badgeCount",0);
                    startService(intent);
                }
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }
}
