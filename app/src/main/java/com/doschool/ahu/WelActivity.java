package com.doschool.ahu;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baidu.mobads.AdSettings;
import com.baidu.mobads.AppActivity;
import com.baidu.mobads.SplashAd;
import com.baidu.mobads.SplashAdListener;
import com.blankj.utilcode.util.SPUtils;
import com.doschool.ahu.appui.main.ui.bean.StartBean;
import com.doschool.ahu.appui.reglogin.bean.LoginVO;
import com.doschool.ahu.appui.reglogin.ui.LoginActivity;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.db.AppConfigDao;
import com.doschool.ahu.db.ConversationDao;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.factory.AppDoUrlFactory;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.widget.CircleProgressbar;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jaeger.library.StatusBarUtil;
import com.jakewharton.rxbinding2.view.RxView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ViewInject;

import java.util.concurrent.TimeUnit;


import io.reactivex.Observable;

import static com.doschool.ahu.configfile.ApiConfig.API_START;
import static com.doschool.ahu.configfile.CodeConfig.MSSP_ID;

/**
 * Created by X on 2018/7/12
 *
 * 欢迎页  인사말 부축
 */
public class WelActivity extends BaseActivity {

    @ViewInject(R.id.ivStartup)
    private ImageView ivStartup;


    @ViewInject(R.id.circle_togo)
    private CircleProgressbar circle_togo;

    private LoginDao loginDao;
    private LoginVO.LoginData loginData;
    private boolean isStart;
    private ConversationDao conversationDao;
    private AppConfigDao appConfigDao;

    private boolean isClick = false;
    String json;

    @ViewInject(R.id.mssp_rl)
    private RelativeLayout mssp_rl;

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_welcome_layout;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        loginDao = new LoginDao(this);
        conversationDao = new ConversationDao(this);
        appConfigDao = new AppConfigDao(this);
        loginData = loginDao.getObject();
        if (getIntent().getExtras() != null) {
            json = getIntent().getStringExtra("json");
        }
        isStart = SPUtils.getInstance().getBoolean("isstart", true);
        SPUtils.getInstance().put("verify", 0);
        SPUtils.getInstance().put("iskill", true);
//        sspSkip();
        Observable.timer(1,TimeUnit.SECONDS)
                .subscribe(aLong -> getDG());
        closeTogo();
    }

    //百度ssp广告位
    private void sspSkip(){
        //	设置'广告着陆页'动作栏的颜色主题
         AppActivity.setActionBarColorTheme(AppActivity.ActionBarColorTheme.ACTION_BAR_GREEN_THEME);
        // 默认请求http广告，若需要请求https广告，请设置AdSettings.setSupportHttps为true
         AdSettings.setSupportHttps(true);
        // 设置视频广告最大缓存占用空间(15MB~100MB),默认30MB,单位MB
        SplashAd.setMaxVideoCacheCapacityMb(30);
        SplashAdListener listener=new SplashAdListener() {
            @Override
            public void onAdPresent() {
            }

            @Override
            public void onAdDismissed() {
                // 跳转至您的应用主界面
                jumpWhenCanClick();
            }

            @Override
            public void onAdFailed(String s) {
//                jump();
                circle_togo.setVisibility(View.VISIBLE);
                getDG();
                closeTogo();
            }

            @Override
            public void onAdClick() {
                // 设置开屏可接受点击时，该回调可用
            }
        };
        new SplashAd(this,mssp_rl,listener,MSSP_ID,true);
    }

    //获取闪屏
    private void getDG(){
        XLNetHttps.requestNormal(API_START, XLNetHttps.getBaseMap(this), StartBean.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                StartBean startBean= XLGson.fromJosn(result,StartBean.class);
                if (startBean.getCode()==0){
                    if (startBean.getData()!=null){
                        XLGlideLoader.loadGuide(ivStartup,startBean.getData().getPictureUrl(),R.mipmap.ig_welcome);
                    }else {
                        XLGlideLoader.loadGuide(ivStartup,"",R.mipmap.ig_welcome);
                    }
                }else {
                    XLGlideLoader.loadGuide(ivStartup,"",R.mipmap.ig_welcome);
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

    @SuppressLint("CheckResult")
    private void closeTogo(){
        circle_togo.setOutLineColor(Color.TRANSPARENT);
        circle_togo.setInCircleColor(Color.parseColor("#50000000"));
        circle_togo.setProgressColor(getResources().getColor(R.color.now_txt_color));
        circle_togo.setProgressLineWidth(5);
        circle_togo.setProgressType(CircleProgressbar.ProgressType.COUNT);
        circle_togo.setTimeMillis(3000);
        circle_togo.reStart();

        circle_togo.setCountdownProgressListener(1, (what, progress) -> {
            if(what==1 && progress==100 && !isClick){
                if (isStart){
                    IntentUtil.toActivity(WelActivity.this,null,GuideActivity.class);
                }else {
                    if (loginData!=null){
                        gotoMain();
                    }else {
                        gotoLogin();
                    }
                }
            }
        });

        RxView.clicks(circle_togo)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    isClick=true;
                    if (isStart){
                        IntentUtil.toActivity(WelActivity.this,null,GuideActivity.class);
                    }else {
                        if (loginData!=null){
                            gotoMain();
                        }else {
                            gotoLogin();
                        }
                    }
                });
    }

    private void gotoMain(){
        if (SPUtils.getInstance().getBoolean("iskill") && TextUtils.isEmpty(loginData.getUserDO().getPhone())){
            loginDao.clearUserTable();
            conversationDao.clearConTable();
            appConfigDao.clearUserTable();
            gotoLogin();
        } else {
            IntentUtil.toActivity(this, null, MainActivity.class);
            if (json != null) {
                AppDoUrlFactory.jumpActivity(this, json, "", "");
            }
            this.finish();
        }
    }

    private void gotoLogin(){
        IntentUtil.toActivity(this,null,LoginActivity.class);
        this.finish();
    }

    /**
     * 当设置开屏可点击时，需要等待跳转页面关闭后，再切换至您的主窗口。故此时需要增加canJumpImmediately判断。 另外，点击开屏还需要在onResume中调用jumpWhenCanClick接口。
     */
    public boolean canJumpImmediately = false;

    private void jumpWhenCanClick() {
        if (canJumpImmediately) {
            if (isStart){
                IntentUtil.toActivity(WelActivity.this,null,GuideActivity.class);
            }else {
                if (loginData!=null){
                    gotoMain();
                }else {
                    gotoLogin();
                }
            }
        } else {
            canJumpImmediately = true;
        }
    }

    /**
     * 不可点击的开屏，使用该jump方法，而不是用jumpWhenCanClick
     */
    private void jump() {
        if (isStart){
            IntentUtil.toActivity(WelActivity.this,null,GuideActivity.class);
        }else {
            if (loginData!=null){
                gotoMain();
            }else {
                gotoLogin();
            }
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (canJumpImmediately) {
//            jumpWhenCanClick();
//        }
//        canJumpImmediately = true;
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        canJumpImmediately = false;
//    }

    @Override
    protected void DetoryViewAndThing() {
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this,0,null);
        StatusBarUtil.setLightMode(this);
    }
}
