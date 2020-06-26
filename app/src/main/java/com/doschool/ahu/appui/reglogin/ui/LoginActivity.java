package com.doschool.ahu.appui.reglogin.ui;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.doschool.ahu.MainActivity;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.activity.WebActivity;
import com.doschool.ahu.appui.main.ui.bean.AppCofingDO;
import com.doschool.ahu.appui.reglogin.bean.LoginVO;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.base.model.BaseModel;
import com.doschool.ahu.configfile.AppConfig;
import com.doschool.ahu.db.AppConfigDao;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.widget.ActionButton;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jaeger.library.StatusBarUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.doschool.ahu.configfile.ApiConfig.API_APPCONFIG;
import static com.doschool.ahu.configfile.ApiConfig.API_PHONE_LOGIN;
import static com.doschool.ahu.configfile.ApiConfig.API_PHONE_YZM;
import static com.doschool.ahu.configfile.ApiConfig.INS_AGREEMENT;
import static com.doschool.ahu.configfile.ApiConfig.USER_SERVER_CLAUSE;
import static com.doschool.ahu.configfile.CodeConfig.QQ_LINE;

/**
 * Created by X on 2018/9/10
 *
 * 登录
 */
public class LoginActivity extends BaseActivity {


    @ViewInject(R.id.iv_head_top)
    private ImageView iv_head_top;

    @ViewInject(R.id.ex_name)
    private EditText ex_name;

    @ViewInject(R.id.ex_pwd)
    private EditText ex_pwd;

    @ViewInject(R.id.act_button)
    private ActionButton act_button;

    @ViewInject(R.id.tx_no_number)
    private TextView tx_no_number;

    @ViewInject(R.id.login_tx_yzm)//获取验证码
    private TextView login_tx_yzm;

    private Disposable disposable;


    private LoginDao loginDao;
    private ArrayMap<String,String> configMap=new ArrayMap<>();
    private AppConfigDao appConfigDao;

    private ArrayMap<String,String> phoneMap=new ArrayMap<>();
    private ArrayMap<String,String> yzmMap=new ArrayMap<>();

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_login;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        init();
        click();
    }

    private void init(){
        SPUtils.getInstance().put("verify",0);
        loginDao=new LoginDao(this);
        appConfigDao=new AppConfigDao(this);
        XLGlideLoader.loadCircleImage(iv_head_top,R.mipmap.app_launcher);

        yzmMap=XLNetHttps.getBaseMap(this);
        phoneMap=XLNetHttps.getBaseMap(this);
        configMap=XLNetHttps.getBaseMap(this);
        getAppConfig();
    }

    private boolean isLogin(){
        if (!RegexUtils.isMobileExact(ex_name.getText().toString())){
            XLToast.showToast("请输入正确的手机号！");
            return false;
        }
        if (TextUtils.isEmpty(ex_pwd.getText().toString())){
            XLToast.showToast("请输入验证码！");
            return false;
        }
        return true;
    }

    private void loginPhone(){
        if (KeyboardUtils.isSoftInputVisible(this)){
            KeyboardUtils.hideSoftInput(this);
        }
        if (!isLogin()){
            return;
        }
        //登录按钮动画开始
        act_button.startAnim();
        phoneMap.put("phone",ex_name.getText().toString());
        phoneMap.put("captcha",ex_pwd.getText().toString());
        XLNetHttps.requestNormal(API_PHONE_LOGIN, phoneMap, LoginVO.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                LoginVO loginV= XLGson.fromJosn(result,LoginVO.class);
                if (loginV.getCode()==0){
                    loginDao.saveObject(loginV.getData());
                    new Handler().postDelayed(() -> gotoNew(),500);
                }else {
                    new Handler().postDelayed(() -> act_button.regainBg(),500);
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

    @Override
    protected void DetoryViewAndThing() {
        if (disposable!=null){
            disposable.dispose();
        }
    }

    @SuppressLint("CheckResult")
    private void click(){
        RxView.clicks(act_button)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> loginPhone());


        //默认发送验证码不能点击
        RxTextView.textChangeEvents(ex_name)
                .subscribe(textViewTextChangeEvent -> {
                    if (textViewTextChangeEvent.text().length()!=11 && !isRun){
                        login_tx_yzm.setEnabled(false);
                        RxTextView.color(login_tx_yzm).accept(getResources().getColor(R.color.login_edit));
                    }else {
                        login_tx_yzm.setEnabled(true);
                        RxTextView.color(login_tx_yzm).accept(getResources().getColor(R.color.now_txt_color));
                    }
                });
        RxView.clicks(login_tx_yzm)
                .throttleFirst(2,TimeUnit.SECONDS)
                .subscribe(o -> {
                    String phone=ex_name.getText().toString();
                    if (!RegexUtils.isMobileExact(phone)){
                        XLToast.showToast("请输入正确的号码！");
                    }else {
                        if (tx_no_number.getVisibility()==View.GONE){
                            tx_no_number.setVisibility(View.VISIBLE);
                        }
                        runtime();
                        yzm();
                    }
                });

        //没收到验证码
        RxView.clicks(tx_no_number)
                .throttleFirst(2,TimeUnit.SECONDS)
                .subscribe(o->{
                    if (customAlert!=null && customAlert.isShowing()){
                        customAlert.show();
                    }else {
                        initDialog();
                    }
                });
    }

    private AlertDialog.Builder alertDialog;
    private AlertDialog customAlert;
    private ImageView dialog_xiv;
    private TextView dialog_xtx_st,dialog_xtv_qq,dialog_xcopy;
    private void initDialog(){
        alertDialog=new AlertDialog.Builder(this);
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_qq_line,null);
        alertDialog.setView(view);
        dialog_xiv=view.findViewById(R.id.dialog_xiv);
        dialog_xtx_st=view.findViewById(R.id.dialog_xtx_st);
        dialog_xtv_qq=view.findViewById(R.id.dialog_xtv_qq);
        dialog_xcopy=view.findViewById(R.id.dialog_xcopy);
        alertDialog.setCancelable(true);
        alertDialog.create();
        customAlert=alertDialog.show();
        dialog_xtx_st.setText("请联系"+ AppConfig.getAppName()+"官网QQ");
        dialog_xtv_qq.setText(QQ_LINE);
        dialog_xiv.setOnClickListener(v -> customAlert.dismiss());
        dialog_xcopy.setOnClickListener(v -> {
            //获取剪贴板管理器
            ClipboardManager cm= (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData clipData=ClipData.newPlainText("Lable",dialog_xtv_qq.getText().toString());
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(clipData);
            customAlert.dismiss();
            XLToast.showToast("复制成功！");
        });
    }

    //监听验证码按钮的倒计时及联动
    private boolean isRun=false;
    private void runtime(){
        disposable= Flowable.intervalRange(0,61,0,1,TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> {
                    login_tx_yzm.setEnabled(false);
                    isRun=false;
                    RxTextView.color(login_tx_yzm).accept(getResources().getColor(R.color.login_edit));
                    login_tx_yzm.setText("（"+String.valueOf(60-aLong)+"S）");
                })
                .doOnCancel(() -> {
                    login_tx_yzm.setEnabled(true);
                    isRun=true;
                    login_tx_yzm.setText(getResources().getString(R.string.login_tx_click));
                    if (ex_name.getText().toString().length()==11){
                        login_tx_yzm.setEnabled(true);
                        RxTextView.color(login_tx_yzm).accept(getResources().getColor(R.color.new_color));
                    }else {
                        login_tx_yzm.setEnabled(false);
                        RxTextView.color(login_tx_yzm).accept(getResources().getColor(R.color.login_edit));
                    }
                })
                .doOnComplete(() -> {
                    isRun=true;
                    login_tx_yzm.setText(getResources().getString(R.string.login_tx_click));
                    if (ex_name.getText().toString().length()==11){
                        login_tx_yzm.setEnabled(true);
                        RxTextView.color(login_tx_yzm).accept(getResources().getColor(R.color.new_color));
                    }else {
                        login_tx_yzm.setEnabled(false);
                        RxTextView.color(login_tx_yzm).accept(getResources().getColor(R.color.login_edit));
                    }
                })
                .subscribe();
    }
    //获取验证码
    private void yzm(){
        yzmMap.put("phone",ex_name.getText().toString());
        XLNetHttps.requestNormal(API_PHONE_YZM, yzmMap, BaseModel.class, new XLCallBack() {
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

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this,0,null);
        StatusBarUtil.setDarkMode(this);
    }

    private void gotoNew() {
        act_button.gotoNew();
        gotoMain();
    }

    private void gotoMain(){
        SPUtils.getInstance().put("phtype",-1);
        IntentUtil.toActivity(this,null,MainActivity.class);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            ActivityUtils.finishAllActivities();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //获取app配置
    private void getAppConfig(){
        XLNetHttps.requestNormal(API_APPCONFIG, configMap, AppCofingDO.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                AppCofingDO appCofingDO=XLGson.fromJosn(result,AppCofingDO.class);
                if (appCofingDO.getCode()==0){
                    appConfigDao.saveObject(appCofingDO.getData());
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

    @Event({R.id.tx_inxy,R.id.tx_user_sv,R.id.tx_number_go})
    private void clicks(View view){
        switch (view.getId()){
            case R.id.tx_inxy://协议
                toWeb(INS_AGREEMENT);
                break;
            case R.id.tx_user_sv://条款
                toWeb(USER_SERVER_CLAUSE);
                break;
            case R.id.tx_number_go://学工号登录
                IntentUtil.toActivity(this,null,LoginAcademicActivity.class);
                break;
        }
    }

    private void toWeb(String url){
        Bundle bundle=new Bundle();
        bundle.putString("URL", url);
        IntentUtil.toActivity(this,bundle, WebActivity.class);
    }

    @Override
    protected void onStop() {
        super.onStop();
        act_button.regainBg();
    }
}
