package com.doschool.ahu.appui.mine.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.event.XMessageEvent;
import com.doschool.ahu.appui.reglogin.bean.LoginVO;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.base.model.BaseModel;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.EventUtils;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.widget.VerificationCodeView;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.doschool.ahu.appui.mine.ui.activity.ChangePhoneValActivity.CHANGE_PHONE;
import static com.doschool.ahu.configfile.ApiConfig.API_CHANGE_PHONE;
import static com.doschool.ahu.configfile.ApiConfig.API_PHONE_YZM;

/**
 * Created by X on 2018/11/27.
 *
 * 更换手机号
 */
public class ChangePhoneActivity extends BaseActivity {

    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;

    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.bind_exph)
    private EditText bind_exph;

    @ViewInject(R.id.bind_tvyzm)
    private TextView bind_tvyzm;

    @ViewInject(R.id.bind_tvgo)
    private TextView bind_tvgo;

    @ViewInject(R.id.codeview)
    private VerificationCodeView codeview;

    private boolean isRun=false;
    private Disposable disposable;
    private ArrayMap<String,String> yzmMap=new ArrayMap<>();
    private ArrayMap<String,String> bindMap=new ArrayMap<>();

    private String code;
    private LoginDao loginDao;

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_bind_phone;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        tool_back_iv.setVisibility(View.VISIBLE);
        tool_title_tv.setVisibility(View.VISIBLE);
        tool_title_tv.setText("更换手机号");
        bind_tvgo.setText("确定");
        yzmMap= XLNetHttps.getBaseMap(this);
        bindMap= XLNetHttps.getBaseMap(this);
        loginDao=new LoginDao(this);
        initBtn();
    }

    @SuppressLint("CheckResult")
    private void initBtn(){
        RxTextView.textChangeEvents(bind_exph)
                .subscribe(textViewTextChangeEvent -> {
                    if (textViewTextChangeEvent.text().length()!=11 && !isRun){
                        bind_tvyzm.setEnabled(false);
                        RxTextView.color(bind_tvyzm).accept(getResources().getColor(R.color.login_edit));
                        bind_tvyzm.setBackgroundResource(R.drawable.bg_unselect_btn_yzm);
                    }else {
                        bind_tvyzm.setEnabled(true);
                        RxTextView.color(bind_tvyzm).accept(getResources().getColor(R.color.new_color));
                        bind_tvyzm.setBackgroundResource(R.drawable.bg_select_btn_yzm);
                    }
                });
        RxView.clicks(bind_tvyzm)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    String phone=bind_exph.getText().toString();
                    if (!RegexUtils.isMobileExact(phone)){
                        XLToast.showToast("请输入正确的号码！");
                    }else {
                        runtime();
                        yzm();
                    }
                });
        codeview.setOnCodeFinishListener(content -> code=content);

        //验证成功页面
        RxView.clicks(bind_tvgo)
                .throttleFirst(2,TimeUnit.SECONDS)
                .subscribe(o -> bindGO());
    }

    //监听验证码按钮的倒计时及联动
    private void runtime(){
        disposable= Flowable.intervalRange(0,61,0,1,TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> {
                    bind_tvyzm.setEnabled(false);
                    isRun=false;
                    bind_tvyzm.setBackgroundResource(0);
                    RxTextView.color(bind_tvyzm).accept(getResources().getColor(R.color.login_edit));
                    bind_tvyzm.setText("（"+String.valueOf(60-aLong)+"S）");
                })
                .doOnCancel(() -> {
                    bind_tvyzm.setEnabled(true);
                    isRun=true;
                    bind_tvyzm.setText(getResources().getString(R.string.login_tx_click));
                    if (bind_exph.getText().toString().length()==11){
                        bind_tvyzm.setEnabled(true);
                        bind_tvyzm.setBackgroundResource(R.drawable.bg_select_btn_yzm);
                        RxTextView.color(bind_tvyzm).accept(getResources().getColor(R.color.new_color));
                    }else {
                        bind_tvyzm.setEnabled(false);
                        bind_tvyzm.setBackgroundResource(R.drawable.bg_unselect_btn_yzm);
                        RxTextView.color(bind_tvyzm).accept(getResources().getColor(R.color.login_edit));
                    }
                })
                .doOnComplete(() -> {
                    isRun=true;
                    bind_tvyzm.setText(getResources().getString(R.string.login_tx_click));
                    if (bind_exph.getText().toString().length()==11){
                        bind_tvyzm.setEnabled(true);
                        bind_tvyzm.setBackgroundResource(R.drawable.bg_select_btn_yzm);
                        RxTextView.color(bind_tvyzm).accept(getResources().getColor(R.color.new_color));
                    }else {
                        bind_tvyzm.setEnabled(false);
                        bind_tvyzm.setBackgroundResource(R.drawable.bg_unselect_btn_yzm);
                        RxTextView.color(bind_tvyzm).accept(getResources().getColor(R.color.login_edit));
                    }
                })
                .subscribe();
    }

    private void yzm(){
        yzmMap.put("phone",bind_exph.getText().toString());
        XLNetHttps.request(API_PHONE_YZM, yzmMap, BaseModel.class, new XLCallBack() {
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
    private boolean isYZ(){
        if (!RegexUtils.isMobileExact(bind_exph.getText().toString())){
            XLToast.showToast("请输入正确的号码！");
            return false;
        }
        if (TextUtils.isEmpty(code)){
            XLToast.showToast("请输入验证码！");
            return false;
        }
        return true;
    }

    private void bindGO(){
        if (!isYZ()){
            return;
        }
        bindMap.put("phone",bind_exph.getText().toString());
        bindMap.put("captcha",code);
        XLNetHttps.request(API_CHANGE_PHONE, bindMap, LoginVO.class,  new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                LoginVO loginV= XLGson.fromJosn(result,LoginVO.class);
                if (loginV.getCode()==0){
                    XLToast.showToast(loginV.getMessage());
                    loginDao.clearUserTable();
                    loginDao.saveObject(loginV.getData());
                    EventUtils.onPost(new XMessageEvent(CHANGE_PHONE));
                    ChangePhoneActivity.this.finish();
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


    @Event({R.id.tool_back_iv})
    private void clicks(View view){
        switch (view.getId()){
            case R.id.tool_back_iv:
                finish();
                break;
        }
    }

    @Override
    protected void DetoryViewAndThing() {
        if (disposable!=null){
            disposable.dispose();
        }
    }
}
