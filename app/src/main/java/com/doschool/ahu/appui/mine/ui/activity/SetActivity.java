package com.doschool.ahu.appui.mine.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.activity.PersonalDataActivity;
import com.doschool.ahu.appui.main.ui.activity.WebActivity;
import com.doschool.ahu.appui.main.ui.bean.UpDateApp;
import com.doschool.ahu.appui.reglogin.ui.LoginActivity;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.configfile.AppConfig;
import com.doschool.ahu.db.AppConfigDao;
import com.doschool.ahu.db.ConversationDao;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.factory.AppDoUrlFactory;
import com.doschool.ahu.utils.AlertUtils;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.LocalApkUtils;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLDownLoad;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jakewharton.rxbinding2.view.RxView;
import com.just.agentweb.AgentWebConfig;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;

import static android.graphics.Color.TRANSPARENT;
import static com.doschool.ahu.configfile.ApiConfig.API_CHECK_UPDATE;
import static com.doschool.ahu.configfile.ApiConfig.INS_AGREEMENT;
import static com.doschool.ahu.configfile.AppConfig.APP_FILE_SAVE;
import static com.doschool.ahu.configfile.AppConfig.APP_UPDATE_ID;
import static com.doschool.ahu.configfile.AppConfig.APP_UPDATE_NAME;
import static com.doschool.ahu.configfile.AppConfig.DOWNLOAD_FILE_APK;

/**
 * Created by X on 2018/9/13
 */
public class SetActivity extends BaseActivity {


    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;
    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.ch_tv_version)
    private TextView ch_tv_version;

    private LoginDao loginDao;
    private ConversationDao conversationDao;
    private AppConfigDao appConfigDao;
    private ArrayMap<String, String> appMap = new ArrayMap<>();

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_set_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        tool_back_iv.setVisibility(View.VISIBLE);
        tool_title_tv.setVisibility(View.VISIBLE);
        tool_title_tv.setText("设置");
        loginDao = new LoginDao(this);
        appMap = XLNetHttps.getBaseMap(this);
        conversationDao = new ConversationDao(this);
        appConfigDao = new AppConfigDao(this);

        ch_tv_version.setText("当前版本：V"+AppConfig.getVersionName());
    }

    @Event({R.id.tool_back_iv, R.id.tv_loginout, R.id.resert_pwd_rl, R.id.simple_rl1,
            R.id.simple_rl2, R.id.simple_rl3, R.id.message_notify, R.id.check_new_version,R.id.simple_rl4,R.id.simple_rl0,R.id.simple_rlinfo})
    private void onClicks(View view) {
        switch (view.getId()) {
            case R.id.tool_back_iv:
                finish();
                break;
            case R.id.tv_loginout://退出登录
                loginDao.clearUserTable();
                conversationDao.clearConTable();
                appConfigDao.clearUserTable();
                IntentUtil.toActivity(this, null, LoginActivity.class);
                break;
            case R.id.simple_rlinfo://个人信息
                if (loginDao.getObject()!=null){
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("gtag",loginDao.getObject());
                    IntentUtil.toActivity(this,bundle,PersonalDataActivity.class);
                }
                break;
            case R.id.simple_rl0://更换手机号
                if (AppDoUrlFactory.noneMember(this)){
                    if (loginDao.getObject()!=null){
                        AlertUtils.alertToVerify(this,loginDao.getObject().getHandleStatus());
                    }
                }else {
                    IntentUtil.toActivity(this,null,ChangePhoneValActivity.class);
                }
                break;
            case R.id.simple_rl1:
            case R.id.resert_pwd_rl://重置密码
                IntentUtil.toActivity(this, null, ResertPwdActivity.class);
                break;
            case R.id.simple_rl2://清缓存
//                if (FileUtils.isFileExists(DOWNLOAD_FILE)){
//                    FileUtils.deleteAllInDir(DOWNLOAD_FILE);
//                }
//                if (FileUtils.isFileExists(SAVE_SCREEN_IMG)){
//                    FileUtils.deleteAllInDir(SAVE_SCREEN_IMG);
//                }
                //清理web得缓存
                AgentWebConfig.clearDiskCache(this);
                XLToast.showToast("清理成功！");
                break;
            case R.id.simple_rl3://关于我们
                if (appConfigDao.getAppCinfigDO() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", appConfigDao.getAppCinfigDO().getAboutusUrl());
                    IntentUtil.toActivity(this, bundle, WebActivity.class);
                }
                break;
            case R.id.message_notify://消息提醒
                IntentUtil.toActivity(this, null, MessageNotifyActivity.class);
                break;
            case R.id.check_new_version://检查更新
                initUpDateApp();
                break;
            case R.id.simple_rl4://隐私政策
                Bundle bundle=new Bundle();
                bundle.putString("URL", INS_AGREEMENT);
                IntentUtil.toActivity(this,bundle, WebActivity.class);
                break;
        }
    }

    //更新app
    private void initUpDateApp() {
        appMap.put("version", AppConfig.getVersionName());
        XLNetHttps.request(API_CHECK_UPDATE, appMap,  UpDateApp.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                UpDateApp upDateApp = XLGson.fromJosn(result, UpDateApp.class);
                if (upDateApp.getCode() == 0) {
                    if (upDateApp.getData().getStatus() == 1) {
                        XLToast.showToast(upDateApp.getMessage());
                    } else {
                        //status=2后台已处理是否首次登录，等于3必须下载
                        String path = DOWNLOAD_FILE_APK + APP_FILE_SAVE + ".apk";
                        File file = new File(path);
                        if (!file.exists()) {
                            updateApp(upDateApp);
                        } else {
                            if (!TextUtils.equals(LocalApkUtils.getVersionName(SetActivity.this, path), upDateApp.getData().getAppVersionDO().getVersion())//比对本地文件夹下apk的版本名称和后台的是否一致
                                    && !TextUtils.equals(upDateApp.getData().getAppVersionDO().getVersion(), AppConfig.getVersionName())) {//应用版本名和后台对比
                                updateApp(upDateApp);
                            }else if (TextUtils.equals(LocalApkUtils.getVersionName(SetActivity.this, path),upDateApp.getData().getAppVersionDO().getVersion())){//本地对比
                                checkLocal(file);
                            }
                        }
                    }
                }
            }

            @Override
            public void XLError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void XLCancle(Callback.CancelledException cex) {

            }

            @Override
            public void XLFinish() {

            }
        });
    }


    private TextView check_tv_cancle,check_tv_az;
    private AlertDialog checkDialog;
    private AlertDialog.Builder checkBuild;
    @SuppressLint("CheckResult")
    private void checkLocal(File file){
        View checkView=LayoutInflater.from(this).inflate(R.layout.check_update_win,null);
        checkBuild=new AlertDialog.Builder(this);
        checkBuild.setView(checkView);
        check_tv_cancle=checkView.findViewById(R.id.check_tv_cancle);
        check_tv_az=checkView.findViewById(R.id.check_tv_az);
        checkDialog=checkBuild.create();
//        checkDialog.setCancelable(true);
        //背景透明 避免圆角边框的同时会出现白色直角部分
        Window window = checkDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        checkDialog.show();

        check_tv_cancle.setOnClickListener(view -> checkDialog.dismiss());
        RxView.clicks(check_tv_az)
                .throttleFirst(2,TimeUnit.SECONDS)
                .subscribe(o -> {
                    checkDialog.dismiss();
                    AppUtils.installApp(file);
                });
    }

    private Disposable disposable;
    private TextView update_title, update_vername, update_tvcontent, update_cancle, update_btn, update_ha_tv, update_load;
    private LinearLayout update_ll;

    private void updateApp(UpDateApp upDateApp) {
        View view = LayoutInflater.from(this).inflate(R.layout.xupdate_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        update_title = view.findViewById(R.id.update_title);
        update_vername = view.findViewById(R.id.update_vername);
        update_tvcontent = view.findViewById(R.id.update_tvcontent);
        update_cancle = view.findViewById(R.id.update_cancle);
        update_btn = view.findViewById(R.id.update_btn);
        update_ha_tv = view.findViewById(R.id.update_ha_tv);
        update_load = view.findViewById(R.id.update_load);
        update_ll = view.findViewById(R.id.update_ll);
        //txt内容滚动
        update_tvcontent.setMovementMethod(ScrollingMovementMethod.getInstance());
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        //背景透明 避免圆角边框的同时会出现白色直角部分
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        dialog.show();

        update_vername.setText(upDateApp.getData().getAppVersionDO().getTitle());
        update_tvcontent.setText(upDateApp.getData().getAppVersionDO().getUpdateInfo());
        if (upDateApp.getData().getStatus() == 2) {
            update_title.setText(R.string.xupdate_title);
            update_ll.setVisibility(View.VISIBLE);
            update_ha_tv.setVisibility(View.GONE);

            update_cancle.setOnClickListener(view1 -> dialog.dismiss());
            update_btn.setOnClickListener(view12 -> {
                RxPermissions permissions=new RxPermissions(SetActivity.this);
                permissions.requestEachCombined(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(permission -> {
                            if (permission.granted){//同意后调用
                                update_ll.setVisibility(View.GONE);
                                update_load.setVisibility(View.VISIBLE);
                                if (!TextUtils.isEmpty(upDateApp.getData().getAppVersionDO().getDownloadUrl())) {
                                    SetActivity.this.downloadApp(upDateApp.getData().getAppVersionDO().getDownloadUrl(), dialog);
                                }
                            }else if (permission.shouldShowRequestPermissionRationale){//禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示
                            }else {//禁止，但选择“以后不再询问”，以后申请权限，不会继续弹出提示
                                if (!permissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                                    XLToast.showToast("您的存储权限未打开！");
                                }
                                if (!permissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)){
                                    XLToast.showToast("您的存储权限未打开！");
                                }
                            }
                        });
            });
        } else {
            update_title.setText(R.string.upate_ha);
            update_ll.setVisibility(View.GONE);
            update_ha_tv.setVisibility(View.VISIBLE);
            update_ha_tv.setOnClickListener(view13 -> {
                RxPermissions permissions=new RxPermissions(SetActivity.this);
                permissions.requestEachCombined(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(permission -> {
                            if (permission.granted){//同意后调用
                                update_ha_tv.setVisibility(View.GONE);
                                update_load.setVisibility(View.VISIBLE);
                                if (!TextUtils.isEmpty(upDateApp.getData().getAppVersionDO().getDownloadUrl())) {
                                    SetActivity.this.downloadApp(upDateApp.getData().getAppVersionDO().getDownloadUrl(), dialog);
                                }
                            }else if (permission.shouldShowRequestPermissionRationale){//禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示
                            }else {//禁止，但选择“以后不再询问”，以后申请权限，不会继续弹出提示
                                if (!permissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                                    XLToast.showToast("您的存储权限未打开！");
                                }
                                if (!permissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)){
                                    XLToast.showToast("您的存储权限未打开！");
                                }
                            }
                        });
            });
        }
    }

    /**
     * 创建通知栏进度条
     */
    private NotificationManager mNotifyManager;
    private Notification.Builder nBuilder;
    private Notification notification; //下载通知进度提示

    private void createNotification() {
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(APP_UPDATE_ID, APP_UPDATE_NAME, NotificationManager.IMPORTANCE_LOW);
            mNotifyManager.createNotificationChannel(mChannel);
            nBuilder = new Notification.Builder(this,APP_UPDATE_ID);
            nBuilder.setContentTitle("版本更新")
                    .setContentText("下载进度:" + "0%")
                    .setProgress(100, 0, false)
                    .setAutoCancel(false)
                    .setSmallIcon(R.mipmap.app_launcher);
            notification = nBuilder.build();
        } else {
            nBuilder = new Notification.Builder(this);
            nBuilder.setSmallIcon(R.mipmap.app_launcher)
                    .setContentTitle("版本更新")
                    .setContentText("下载进度:" + "0%")
                    .setProgress(100, 0, false)
                    .setAutoCancel(false)//设置通知被点击一次是否自动取消
                    .setOngoing(true);
            notification = nBuilder.build();//构建通知对象
        }
    }

    private void downloadApp(String url, AlertDialog dialog) {
        String path = DOWNLOAD_FILE_APK + APP_FILE_SAVE + ".apk";
        XLDownLoad.dowanLoadFile(url, path, XLDownLoad.GETS, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
                createNotification();
                disposable = AlertUtils.interval(update_load);
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                //实时更新通知栏进度条
                nBuilder.setProgress((int) total, (int) current, false);
                nBuilder.setContentText("下载进度:" + (int) ((double) current / total * 100) + "%");
                notification = nBuilder.build();
                mNotifyManager.notify(1234, notification);
            }

            @Override
            public void onSuccess(File result) {
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                    update_load.setText("");
                    update_load.setVisibility(View.GONE);
                }
                XLToast.showToast("下载完成！");

                //点击安装PendingIntent
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");
                PendingIntent pi = PendingIntent.getActivity(SetActivity.this, 0, intent, 0);
                notification = nBuilder.setContentTitle("下载完成")
                            .setContentText("点击安装")
                            .setAutoCancel(true)//设置通知被点击一次是否自动取消
                            .setContentIntent(pi).build();
                mNotifyManager.notify(1234, notification);

                if (dialog != null) {
                    dialog.dismiss();
                }

                //安装app
                AppUtils.installApp(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    protected void DetoryViewAndThing() {

    }
}
