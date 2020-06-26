package com.doschool.ahu.appui.reglogin.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.doschool.ahu.MainActivity;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.reglogin.bean.LoginVO;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.db.ConversationDao;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.widget.ActionButton2;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jakewharton.rxbinding2.view.RxView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.concurrent.TimeUnit;

import static com.doschool.ahu.configfile.ApiConfig.API_ACDMIC;

/**
 * Created by X on 2018/10/22
 *
 * 学工号登录
 */
public class LoginAcademicActivity extends BaseActivity {

    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;

    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.acd_ex_num)
    private EditText acd_ex_num;

    @ViewInject(R.id.acd_ex_pwd)
    private EditText acd_ex_pwd;

    @ViewInject(R.id.acd_btn_login)
    private ActionButton2 acd_btn_login;

    private ArrayMap<String,String> map=new ArrayMap<>();
    private LoginDao loginDao;
    private ConversationDao conversationDao;
    private String old;

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_academic_login;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        tool_back_iv.setVisibility(View.VISIBLE);
        tool_title_tv.setVisibility(View.VISIBLE);
        tool_title_tv.setText("学号登录");
        map= XLNetHttps.getBaseMap(this);
        loginDao=new LoginDao(this);
        conversationDao=new ConversationDao(this);
        if (getIntent().getExtras()!=null){
            old=getIntent().getExtras().getString("old");
        }
        click();
    }

    @Event({R.id.tool_back_iv})
    private void clicks(View view){
        finish();
    }

    @SuppressLint("CheckResult")
    private void click(){
        RxView.clicks(acd_btn_login)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> acdLogin());
    }

    private void acdLogin(){
        if (!isAcd()){
            return;
        }
        acd_btn_login.startAnim();
        map.put("funcId",acd_ex_num.getText().toString());
        map.put("password",acd_ex_pwd.getText().toString());
        if (loginDao.getObject()!=null && !TextUtils.isEmpty(loginDao.getObject().getUserDO().getPhone())){
            String phone = loginDao.getObject().getUserDO().getPhone();
            map.put("phone", phone);
        }
        XLNetHttps.requestNormal(API_ACDMIC, map, LoginVO.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                LoginVO loginV= XLGson.fromJosn(result,LoginVO.class);
                if (loginV.getCode()==0){
                    loginDao.clearUserTable();
                    conversationDao.clearConTable();

                    SPUtils.getInstance().put("iskill",false);
                    loginDao.saveObject(loginV.getData());
                    new Handler().postDelayed(() -> gotoNew(),500);
                }else {
                    new Handler().postDelayed(() -> acd_btn_login.regainBg(),500);
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

    private boolean isAcd(){
        if (TextUtils.isEmpty(acd_ex_num.getText().toString())){
            XLToast.showToast("请输入学号！");
            return false;
        }
        if (TextUtils.isEmpty(acd_ex_pwd.getText().toString())){
            XLToast.showToast("请输入密码！");
            return false;
        }
        return true;
    }

    private void gotoNew() {
        acd_btn_login.gotoNew();
        gotoMain();
    }

    private void gotoMain(){
        if (!TextUtils.isEmpty(old) && old.equals("old")){
            ActivityUtils.finishActivity(MainActivity.class);
        }
        Bundle bundle=new Bundle();
        bundle.putString("old",old);
        IntentUtil.toActivity(this,bundle,MainActivity.class);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        acd_btn_login.regainBg();
    }

    @Override
    protected void DetoryViewAndThing() {

    }
}
