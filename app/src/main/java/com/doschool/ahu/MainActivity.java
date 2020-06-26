package com.doschool.ahu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.bottomnavigation.BottomBar;
import com.bottomnavigation.BottomBarTab;
import com.doschool.ahu.appui.infors.chat.service.InitBusiness;
import com.doschool.ahu.appui.infors.chat.service.LoginBusiness;
import com.doschool.ahu.appui.infors.chat.service.TlsBusiness;
import com.doschool.ahu.appui.infors.chat.ui.event.FriendshipEvent;
import com.doschool.ahu.appui.infors.chat.ui.event.GroupEvent;
import com.doschool.ahu.appui.infors.chat.ui.event.MessageEvent;
import com.doschool.ahu.appui.infors.chat.ui.event.RefreshEvent;
import com.doschool.ahu.appui.infors.chat.ui.model.CustomMessage;
import com.doschool.ahu.appui.infors.chat.ui.model.FriendshipInfo;
import com.doschool.ahu.appui.infors.chat.ui.model.GroupInfo;
import com.doschool.ahu.appui.infors.chat.ui.model.Message;
import com.doschool.ahu.appui.infors.chat.ui.model.MessageFactory;
import com.doschool.ahu.appui.infors.chat.ui.model.UserInfo;
import com.doschool.ahu.appui.infors.chat.ui.viewfeatures.view.NotifyDialog;
import com.doschool.ahu.appui.infors.chat.util.PushUtil;
import com.doschool.ahu.appui.main.event.XMessageEvent;
import com.doschool.ahu.appui.main.ui.activity.BindPhoneActivity;
import com.doschool.ahu.appui.main.ui.bean.RecordBean;
import com.doschool.ahu.appui.main.ui.bean.SkipScreenDo;
import com.doschool.ahu.appui.main.ui.bean.UpDateApp;
import com.doschool.ahu.appui.main.ui.fragment.TabMeFragment;
import com.doschool.ahu.appui.main.ui.fragment.TabServiceFragment;
import com.doschool.ahu.appui.reglogin.ui.LoginActivity;
import com.doschool.ahu.appui.writeblog.ui.activity.ReleaseMicroBlogAct;
import com.doschool.ahu.appui.writeblog.ui.bean.PhotoBean;
import com.doschool.ahu.base.BadgeService;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.base.BaseApplication;
import com.doschool.ahu.base.BaseFragment;
import com.doschool.ahu.appui.main.ui.fragment.CommFragment;
import com.doschool.ahu.appui.main.ui.fragment.HomeFragment;
import com.doschool.ahu.appui.main.widget.dialog.TabBarDialog;
import com.doschool.ahu.base.model.BaseModel;
import com.doschool.ahu.base.model.DoUrlModel;
import com.doschool.ahu.configfile.AppConfig;
import com.doschool.ahu.configfile.CodeConfig;
import com.doschool.ahu.configfile.DoUrlConfig;
import com.doschool.ahu.db.AppConfigDao;
import com.doschool.ahu.db.ConversationDao;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.factory.AppDoUrlFactory;
import com.doschool.ahu.utils.AlertUtils;
import com.doschool.ahu.utils.EventUtils;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.widget.SkipScreenDialog;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLDownLoad;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jakewharton.rxbinding2.view.RxView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;

import static android.graphics.Color.TRANSPARENT;
import static com.doschool.ahu.appui.main.ui.activity.BindSuccActivity.BIND_SUCC;
import static com.doschool.ahu.configfile.ApiConfig.API_APP_UPDATE;
import static com.doschool.ahu.configfile.ApiConfig.API_CLICK_SKIP;
import static com.doschool.ahu.configfile.ApiConfig.API_LOGIN_RECORD;
import static com.doschool.ahu.configfile.ApiConfig.API_SKIPSCREEN;
import static com.doschool.ahu.configfile.AppConfig.APP_FILE_SAVE;
import static com.doschool.ahu.configfile.AppConfig.APP_UPDATE_ID;
import static com.doschool.ahu.configfile.AppConfig.APP_UPDATE_NAME;
import static com.doschool.ahu.configfile.AppConfig.DOWNLOAD_FILE_APK;
import static com.doschool.ahu.configfile.CodeConfig.ADDBLOG_IV;
import static com.doschool.ahu.configfile.CodeConfig.ADDBLOG_VD;
import static com.doschool.ahu.configfile.CodeConfig.REQUEST_CODE_PH;
import static com.doschool.ahu.configfile.CodeConfig.REQUEST_CODE_SHOOT;
import static com.doschool.ahu.configfile.CodeConfig.SCROLL_CODE;


/**
 * Created by X on 2018/7/3.
 *미련 의 수수께끼 를 풀다
 * 主界面
 */

public class MainActivity extends BaseActivity implements TIMCallBack {


    @ViewInject(R.id.main_tabbar)//tabbar
    private BottomBar main_tabbar;

    @ViewInject(R.id.rlmid_bar)
    private RelativeLayout rlmid_bar;

    private BaseFragment[] fragments;
    private BaseFragment lastFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private TabBarDialog tabBarDialog;
    private BottomBarTab cvsTab;
    private LoginDao loginDao;
    private List<PhotoBean> photoList=new ArrayList<>();
    private ConversationDao conversationDao;
    private AppConfigDao appConfigDao;
    private String old;
    private ArrayMap<String,String> appMap=new ArrayMap<>();
    private ArrayMap<String,String> dotMap=new ArrayMap<>();

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_main;
    }


    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        EventUtils.register(this);
        loginDao=new LoginDao(this);
        conversationDao=new ConversationDao(this);
        appConfigDao=new AppConfigDao(this);
        appMap=XLNetHttps.getBaseMap(this);
        dotMap=XLNetHttps.getBaseMap(this);
        if (getIntent().getExtras()!=null){
            old=getIntent().getExtras().getString("old");
            if (!TextUtils.isEmpty(old) && old.equals("old")){
                bindFinish();
            }
        }

        clearNotification();
        //初始化fragment
        initFragments();
        //im
        init();
        navInite();
        //umeng
        setAlias();
        //可赠送棒棒糖
        firstLogin();
        //app更新
        initUpDateApp();
        //跳屏
        skipSrceenUrl();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //解决界面被销毁或崩溃导致界面混乱
    }

    @Override
    protected void onResume() {
        //角标置空
        BaseApplication.getInstance().setBadgeCount(0);
        Intent intent=new Intent(this,BadgeService.class);
        intent.putExtra("badgeCount",0);
        startService(intent);
        super.onResume();
    }

    @SuppressLint("CheckResult")
    private void initFragments() {
        fragments = new BaseFragment[]{new HomeFragment(), new CommFragment(), new TabServiceFragment(), new TabMeFragment()};
        changeFragments(fragments[0]);
        main_tabbar.addItem(new BottomBarTab(this, R.mipmap.tab_unsquare_icon, R.mipmap.tab_square_icon,
                R.color.tab_ubfocus, R.color.tab_focus, getResources().getString(R.string.tab_gc)))
                .addItem(new BottomBarTab(this, R.mipmap.tab_unmsg_icon, R.mipmap.tab_msg_icon,
                        R.color.tab_ubfocus, R.color.tab_focus, getResources().getString(R.string.tab_gt)))
                .addItem(new BottomBarTab(this, 0, 0, 0, 0, ""))
                .addItem(new BottomBarTab(this, R.mipmap.tab_uncover_icon, R.mipmap.tab_cover_icon,
                        R.color.tab_ubfocus, R.color.tab_focus, getResources().getString(R.string.tab_fw)))
                .addItem(new BottomBarTab(this, R.mipmap.tab_uncenter_icon, R.mipmap.tab_center_icon,
                        R.color.tab_ubfocus, R.color.tab_focus, getResources().getString(R.string.tab_me)));

        cvsTab = main_tabbar.getItem(1);
        main_tabbar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                if (position>=3){
                    changeFragments(fragments[position-1]);
                }else {
                    changeFragments(fragments[position]);
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                if (position==0){
                    EventUtils.onPost(new XMessageEvent(SCROLL_CODE));
                }
            }
        });


        //中间tabbar点击事件
        RxView.clicks(rlmid_bar)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    type= SPUtils.getInstance().getInt("phtype");
                    if (type==-1 && loginDao.getObject()!=null && loginDao.getObject().getUserDO().getStatus()==0){
                        AlertUtils.alertToVerify(MainActivity.this,loginDao.getObject().getHandleStatus());
                    }else {
                        if (tabBarDialog==null){
                            tabBarDialog=new TabBarDialog(MainActivity.this);

                            //记忆
                            tabBarDialog.setArticleBtnClickListener(ReleaseMicroBlogAct.class,null);
                            //相机
                            tabBarDialog.setMiniCaneraBtnClickListener(MainActivity.this);
                            //视频
                            tabBarDialog.setPhotoBtnClickListener(MainActivity.this);
                        }
                        tabBarDialog.show();
                    }
                });
    }


    public void changeFragments(BaseFragment fragment){
        if (fragment==null){
            fragment=fragments[0];
        }

        if (fragment.equals(lastFragment)){
            return;
        }

        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();

        if (lastFragment!=null){
            fragmentTransaction.hide(lastFragment);
        }

        if (!fragment.isAdded()){
            fragmentTransaction.add(R.id.main_fl,fragment);
        }else {
            fragmentTransaction.show(fragment);
            fragment.onResume();
        }

        fragmentTransaction.commitAllowingStateLoss();
        lastFragment=fragment;
    }


    //回话消息未读及一些弹窗操作
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cvsEvent(XMessageEvent event){
        if (event.getCode()== CodeConfig.CVS_UNRED_CODE){
            cvsTab.setUnreadCount((int) event.getData());
        }else if (event.getCode()==BIND_SUCC){
            alertDialog.dismiss();
            bindFinish();
        }
    }


    //更新app
    private void initUpDateApp(){
        appMap.put("version",AppConfig.getVersionName());
        XLNetHttps.request(API_APP_UPDATE, appMap, UpDateApp.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                UpDateApp upDateApp=XLGson.fromJosn(result,UpDateApp.class);
                if (upDateApp.getCode()==0){
                    if (upDateApp.getData().getStatus()==1){
                        initPhone();
                        loginPH();
                    }else {
                        //status=2后台已处理是否首次登录，等于3必须下载
                        updateApp(upDateApp);
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
    private Disposable disposable;
    private TextView update_title,update_vername,update_tvcontent,update_cancle,update_btn,update_ha_tv,update_load;
    private LinearLayout update_ll;
    private  void updateApp(UpDateApp upDateApp){
        View view=LayoutInflater.from(this).inflate(R.layout.xupdate_layout,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(view);
         update_title=view.findViewById(R.id.update_title);
         update_vername=view.findViewById(R.id.update_vername);
         update_tvcontent=view.findViewById(R.id.update_tvcontent);
         update_cancle=view.findViewById(R.id.update_cancle);
         update_btn=view.findViewById(R.id.update_btn);
         update_ha_tv=view.findViewById(R.id.update_ha_tv);
         update_load=view.findViewById(R.id.update_load);
         update_ll=view.findViewById(R.id.update_ll);
        //txt内容滚动
        update_tvcontent.setMovementMethod(ScrollingMovementMethod.getInstance());
        AlertDialog dialog= builder.create();
        dialog.setCancelable(false);
        //背景透明 避免圆角边框的同时会出现白色直角部分
        Window window=dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        dialog.show();

        update_vername.setText(upDateApp.getData().getAppVersionDO().getTitle());
        update_tvcontent.setText(upDateApp.getData().getAppVersionDO().getUpdateInfo());
        if (upDateApp.getData().getStatus()==2){
            update_title.setText(R.string.xupdate_title);
            update_ll.setVisibility(View.VISIBLE);
            update_ha_tv.setVisibility(View.GONE);

            update_cancle.setOnClickListener(view1 -> {
                dialog.dismiss();
                initPhone();
                loginPH();
            });
            update_btn.setOnClickListener(view12 -> {
                RxPermissions permissions=new RxPermissions(MainActivity.this);
                permissions.requestEachCombined(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(permission -> {
                            if (permission.granted){//同意后调用
                                update_ll.setVisibility(View.GONE);
                                update_load.setVisibility(View.VISIBLE);
                                if (!TextUtils.isEmpty(upDateApp.getData().getAppVersionDO().getDownloadUrl())) {
                                    MainActivity.this.downloadApp(upDateApp.getData().getAppVersionDO().getDownloadUrl(), dialog);
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
        }else {
            update_title.setText(R.string.upate_ha);
            update_ll.setVisibility(View.GONE);
            update_ha_tv.setVisibility(View.VISIBLE);
            update_ha_tv.setOnClickListener(view13 -> {
                RxPermissions permissions=new RxPermissions(MainActivity.this);
                permissions.requestEachCombined(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(permission -> {
                            if (permission.granted){//同意后调用
                                update_ha_tv.setVisibility(View.GONE);
                                update_load.setVisibility(View.VISIBLE);
                                if (!TextUtils.isEmpty(upDateApp.getData().getAppVersionDO().getDownloadUrl())) {
                                    MainActivity.this.downloadApp(upDateApp.getData().getAppVersionDO().getDownloadUrl(), dialog);
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
            notification=nBuilder.build();
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

    private void downloadApp(String url,AlertDialog dialog){
        String path=DOWNLOAD_FILE_APK+ APP_FILE_SAVE+".apk";
        XLDownLoad.dowanLoadFile(url, path, XLDownLoad.GETS, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
                createNotification();
                disposable=AlertUtils.interval(update_load);
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                //实时更新通知栏进度条
                nBuilder.setProgress((int) total, (int) current, false);
                nBuilder.setContentText("下载进度:" + (int) ((double)current/total* 100) + "%");
                notification=nBuilder.build();
                mNotifyManager.notify(1234, notification);
            }

            @Override
            public void onSuccess(File result) {
                if (disposable!=null && !disposable.isDisposed()){
                    disposable.dispose();
                    update_load.setText("");
                    update_load.setVisibility(View.GONE);
                }
                XLToast.showToast("下载完成！");

                //点击安装PendingIntent
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");
                PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
                notification = nBuilder.setContentTitle("下载完成")
                            .setContentText("点击安装")
                            .setAutoCancel(true)//设置通知被点击一次是否自动取消
                            .setContentIntent(pi).build();
                mNotifyManager.notify(1234, notification);

                if (dialog!=null){
                    dialog.dismiss();
                }
                //新老用户验证
                initPhone();
                loginPH();

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

    private AlertDialog.Builder bindBuilder;
    private AlertDialog dialog;
    private ImageView iv_bind_msg;
    private void bindFinish(){
        bindBuilder=new AlertDialog.Builder(this);
        View view=LayoutInflater.from(this).inflate(R.layout.alert_bind_lay,null);
        bindBuilder.setView(view);
        iv_bind_msg=view.findViewById(R.id.iv_bind_msg);
        bindBuilder.setCancelable(true);
        dialog=bindBuilder.create();
        //背景透明 避免圆角边框的同时会出现白色直角部分
        Window window=dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        dialog.show();
        iv_bind_msg.setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    protected void DetoryViewAndThing() {
        EventUtils.unRegister(this);
        if (alertDialog!=null){
            alertDialog.dismiss();
        }
    }


    @Override
    protected boolean isExitBack() {
        return true;
    }

    private int type;
    private void loginPH(){
        type= SPUtils.getInstance().getInt("phtype");
        if (type==-1 && loginDao.getObject()!=null && loginDao.getObject().getUserDO().getStatus()==0){
            AlertUtils.alertToVerify(MainActivity.this,loginDao.getObject().getHandleStatus());
        }
    }

    private AlertDialog.Builder builder;
    private ImageView alert_ph_iv;
    private AlertDialog alertDialog;
    private void initPhone(){
        if (loginDao.getObject()!=null){
            if (TextUtils.isEmpty(loginDao.getObject().getUserDO().getPhone())){
                builder=new AlertDialog.Builder(this);
                View view= LayoutInflater.from(this).inflate(R.layout.alert_phone_lay,null);
                builder.setView(view);
                alert_ph_iv=view.findViewById(R.id.alert_ph_iv);
                builder.setCancelable(false);
                alertDialog=builder.create();
                //背景透明 避免圆角边框的同时会出现白色直角部分
                Window window=alertDialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
                alertDialog.show();
                alert_ph_iv.setOnClickListener(v -> IntentUtil.toActivity(MainActivity.this,null, BindPhoneActivity.class));
            }
        }
    }

    private void firstLogin(){
        XLNetHttps.request(API_LOGIN_RECORD, XLNetHttps.getBaseMap(this), RecordBean.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                RecordBean recordBean= XLGson.fromJosn(result,RecordBean.class);
                if (recordBean.getCode()==0){
                    if (loginDao.getObject().getUserDO().getStatus()==1 && recordBean.getData().getIsFirstLogin()==1){
                        toasts();
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

    private void toasts(){
        View view=LayoutInflater.from(this).inflate(R.layout.first_login_toast,null);
        Toast toast=new Toast(this);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }


    private String id;
    private String sig;
    private void init(){
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        int loglvl = pref.getInt("loglvl", TIMLogLevel.DEBUG.ordinal());
        //初始化IMSDK
        InitBusiness.start(getApplicationContext(),loglvl);
        //初始化TLS
        TlsBusiness.init(getApplicationContext());
//        String id =  TLSService.getInstance().getLastUserIdentifier();
        if (loginDao.getObject()!=null && loginDao.getObject().getUserDO()!=null){
             id=String.valueOf(loginDao.getObject().getUserDO().getId());
             sig=loginDao.getObject().getUserDO().getUserSig();
        }
        UserInfo.getInstance().setId(id);
        UserInfo.getInstance().setUserSig(sig);//TLSService.getInstance().getUserSig(id)
    }
    //获取友盟用户别名
    private void setAlias(){
        PushAgent pushAgent = PushAgent.getInstance(this);
        if(loginDao.getObject()!=null&&loginDao.getObject().getUserDO()!=null) {
            pushAgent.addAlias(loginDao.getObject().getUserDO().getId()+"", "customizedcast", new UTrack.ICallBack() {

                @Override
                public void onMessage(boolean isSuccess, String message) {

                }

            });
        }
    }


    private void navInite() {
        //登录之前要初始化群和好友关系链缓存
        TIMUserConfig userConfig = new TIMUserConfig();
        userConfig.setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                NotifyDialog dialog = new NotifyDialog();
                dialog.show(getString(R.string.kick_logout), getSupportFragmentManager(), (dialog1, which) -> loginout());
            }

            @Override
            public void onUserSigExpired() {
                //票据过期，需要重新登录
                new NotifyDialog().show(getString(R.string.tls_expire), getSupportFragmentManager(), (dialog, which) -> loginout());
            }
        }).setConnectionListener(new TIMConnListener() {
                    @Override
                    public void onConnected() {
                    }

                    @Override
                    public void onDisconnected(int code, String desc) {
                    }

                    @Override
                    public void onWifiNeedAuth(String name) {
                    }
                });

        //设置刷新监听
        RefreshEvent.getInstance().init(userConfig);
        userConfig = FriendshipEvent.getInstance().init(userConfig);
        userConfig = GroupEvent.getInstance().init(userConfig);
        userConfig = MessageEvent.getInstance().init(userConfig);
        TIMManager.getInstance().setUserConfig(userConfig);
        if (loginDao.getObject()!=null){
            LoginBusiness.loginIm(String.valueOf(loginDao.getObject().getUserDO().getId()),
                    loginDao.getObject().getUserDO().getUserSig(), this);
        }

        getMessageListener();
    }

    //新消息通知 设置消息监听器，收到新消息时，通过此监听器回调
    private void getMessageListener(){
        //消息监听器
        TIMManager.getInstance().addMessageListener(list -> {//收到新消息
            for (int i=0;i<list.size();i++){
                Message mMessage = MessageFactory.getMessage(list.get(i));
                if (mMessage != null) {
                    if (mMessage instanceof CustomMessage){
                        TIMCustomElem elem = (TIMCustomElem) list.get(i).getElement(0);
                        try {
                            String str = new String(elem.getData(), "UTF-8");
                            JSONObject jsonObject = new JSONObject(str);
                            int extType=jsonObject.optInt("extType");
                            if (extType==99){
                                list.remove(mMessage);
                                mvLogin();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return false;//返回true将终止回调链，不再调用下一个新消息监听器
        });
    }
    private AlertDialog.Builder mvBuilder;
    private AlertDialog mvDialog;
    private TextView tv_mvlogin;
    private void mvLogin(){
        try {
            View view=LayoutInflater.from(this).inflate(R.layout.alert_mv_login,null);
            mvBuilder=new AlertDialog.Builder(this);
            mvBuilder.setView(view);
            tv_mvlogin=view.findViewById(R.id.tv_mvlogin);
            mvDialog=mvBuilder.create();
            mvDialog.setCancelable(false);
            //背景透明 避免圆角边框的同时会出现白色直角部分
            Window window=mvDialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
            mvDialog.show();

            loginDao.clearUserTable();
            conversationDao.clearConTable();
            tv_mvlogin.setOnClickListener(v -> {
                appConfigDao.clearUserTable();
                if (mvDialog!=null){
                    mvDialog.dismiss();
                }
                IntentUtil.toActivity(MainActivity.this,null, LoginActivity.class);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(int i, String s) {
        switch (i) {
            case 6208:
                //离线状态下被其他终端踢下线
                NotifyDialog dialog = new NotifyDialog();
                dialog.show(getString(R.string.kick_logout), getSupportFragmentManager(), (dialog1, which) -> loginout());
                break;
            case 6200:
                Toast.makeText(this,getString(R.string.login_error_timeout),Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this,getString(R.string.login_error),Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private void loginout(){
            TlsBusiness.logout(UserInfo.getInstance().getId());
            UserInfo.getInstance().setId(null);
            FriendshipInfo.getInstance().clear();
            GroupInfo.getInstance().clear();
            loginDao.clearUserTable();
            conversationDao.clearConTable();
            Intent intent = new Intent(this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
    }

    @Override
    public void onSuccess() {
        //初始化程序后台后消息推送
        PushUtil.getInstance();
        //初始化消息监听
        MessageEvent.getInstance();
    }

    /**
     * 清楚所有通知栏通知
     */
    private void clearNotification(){
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        MiPushClient.clearNotification(getApplicationContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode){
                case REQUEST_CODE_PH://从相册选择视频
                    if (data!=null){
                        for (int i = 0; i< Matisse.obtainPathResult(data).size(); i++){
                            PhotoBean photoBean=new PhotoBean();
                            photoBean.setType(ADDBLOG_VD);
                            photoBean.setPath(Matisse.obtainPathResult(data).get(i));
                            photoList.add(photoBean);
                        }
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("list", (Serializable) photoList);
                        IntentUtil.toActivity(this,bundle,ReleaseMicroBlogAct.class);
                        photoList.clear();
                    }
                    break;
                case REQUEST_CODE_SHOOT:
                    if (data!=null){
                        if (data.getExtras().containsKey("url")){//视频
                            PhotoBean photoBean=new PhotoBean();
                            photoBean.setType(ADDBLOG_VD);
                            photoBean.setPath(data.getExtras().getString("url"));
                            photoList.add(photoBean);
                        }

                        if (data.getExtras().containsKey("path")){//图片
                            PhotoBean bean=new PhotoBean();
                            bean.setType(ADDBLOG_IV);
                            bean.setPath(data.getExtras().getString("path"));
                            photoList.add(bean);
                        }
                        Bundle bun=new Bundle();
                        bun.putSerializable("list", (Serializable) photoList);
                        IntentUtil.toActivity(this,bun,ReleaseMicroBlogAct.class);
                        photoList.clear();
                    }
                    break;
            }
    }

    //跳屏
    private void skipSrceenUrl(){
        XLNetHttps.request(API_SKIPSCREEN, XLNetHttps.getBaseMap(this), SkipScreenDo.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                SkipScreenDo skipScreenDo=XLGson.fromJosn(result,SkipScreenDo.class);
                if (skipScreenDo.getCode()==0){
                    if (skipScreenDo.getData().getStatus()==1){//展示跳屏
                        skipDat(skipScreenDo);
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
    //跳屏数据显示
    private void skipDat(SkipScreenDo screenDo){
        new SkipScreenDialog(this, new SkipScreenDialog.OnSkipDoListener() {
            @Override
            public void onLeftSkip(Dialog dialog, String leftUrl) {
                if (!TextUtils.isEmpty(leftUrl)){
                    //点击记录
                    getSkipClick(screenDo.getData().getJumpScreenRecordDO().getId());

                    DoUrlModel model = XLGson.fromJosn(leftUrl, DoUrlModel.class);
                    if (TextUtils.equals(model.getAction(), DoUrlConfig.ACTION_TOPIC)){//判断是否是话题
                        if (model.getParamList()!=null && model.getParamList().size()>1){
                            AppDoUrlFactory.gotoAway(MainActivity.this,model,model.getParamList().get(0),model.getParamList().get(1));
                        }
                    }else {
                        AppDoUrlFactory.gotoAway(MainActivity.this,model,"","");
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onRightSkip(Dialog dialog, String rightUrl) {
                if (!TextUtils.isEmpty(rightUrl)){
                    //点击记录
                    getSkipClick(screenDo.getData().getJumpScreenRecordDO().getId());

                    DoUrlModel model = XLGson.fromJosn(rightUrl, DoUrlModel.class);
                    if (TextUtils.equals(model.getAction(), DoUrlConfig.ACTION_TOPIC)){//判断是否是话题
                        if (model.getParamList()!=null && model.getParamList().size()>1){
                            AppDoUrlFactory.gotoAway(MainActivity.this,model,model.getParamList().get(0),model.getParamList().get(1));
                        }
                    }else {
                        AppDoUrlFactory.gotoAway(MainActivity.this,model,"","");
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onSkip(Dialog dialog, String doUrl) {
                if (!TextUtils.isEmpty(doUrl)){
                    //点击记录
                    getSkipClick(screenDo.getData().getJumpScreenRecordDO().getId());

                    DoUrlModel model = XLGson.fromJosn(doUrl, DoUrlModel.class);
                    if (TextUtils.equals(model.getAction(), DoUrlConfig.ACTION_TOPIC)){//判断是否是话题
                        if (model.getParamList()!=null && model.getParamList().size()>1){
                            AppDoUrlFactory.gotoAway(MainActivity.this,model,model.getParamList().get(0),model.getParamList().get(1));
                        }
                    }else {
                        AppDoUrlFactory.gotoAway(MainActivity.this,model,"","");
                    }
                }
                dialog.dismiss();
            }
        }).setImgUrl(screenDo.getData().getJumpScreenRecordDO().getImageUrl())
                .setTypes(screenDo.getData().getJumpScreenRecordDO().getSplashType())
                .setDoUrl(screenDo.getData().getJumpScreenRecordDO().getImageDoUrl())
                .setLeftDoUrl(screenDo.getData().getJumpScreenRecordDO().getButtonLeftDoUrl())
                .setRightDoUrl(screenDo.getData().getJumpScreenRecordDO().getButtonRightDoUrl())
                .show();
    }
    //跳屏--添加点击记录
    private ArrayMap<String,String> clickMap=new ArrayMap<>();
    private void getSkipClick(int id){
        clickMap=XLNetHttps.getBaseMap(this);
        clickMap.put("splashId",String.valueOf(id));
        XLNetHttps.request(API_CLICK_SKIP, clickMap, BaseModel.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
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
}
