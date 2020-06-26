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
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.base.model.BaseModel;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.EventUtils;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jakewharton.rxbinding2.view.RxView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.concurrent.TimeUnit;

import static com.doschool.ahu.configfile.ApiConfig.API_CHECK_ACCOUNT;

/**
 * Created by X on 2018/11/27
 *
 * 更换手机号验证
 */
public class ChangePhoneValActivity extends BaseActivity {

    public static final int CHANGE_PHONE=-1098;

    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;

    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.val_phone_tv)
    private EditText val_phone_tv;

    @ViewInject(R.id.val_xh_tv)
    private EditText val_xh_tv;

    @ViewInject(R.id.val_tv_next)
    private TextView val_tv_next;

    private LoginDao loginDao;
    private ArrayMap<String,String> checkMap=new ArrayMap<>();

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_phoneval_layout;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        tool_back_iv.setVisibility(View.VISIBLE);
        tool_title_tv.setVisibility(View.VISIBLE);
        tool_title_tv.setText("验证信息");

        loginDao=new LoginDao(this);
        checkMap=XLNetHttps.getBaseMap(this);

        EventUtils.register(this);

        RxView.clicks(val_tv_next)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> next());
    }

    private void next(){
        if (!isNext()){
            return;
        }
        checkMap.put("phone",val_phone_tv.getText().toString().trim());
        checkMap.put("funcOrNoticeId",val_xh_tv.getText().toString().trim());
        XLNetHttps.request(API_CHECK_ACCOUNT, checkMap,  BaseModel.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                BaseModel model=XLGson.fromJosn(result,BaseModel.class);
                if (model.getCode()==0){
                    IntentUtil.toActivity(ChangePhoneValActivity.this,null,ChangePhoneActivity.class);
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

    private boolean isNext(){
        if (!RegexUtils.isMobileExact(val_phone_tv.getText().toString())){
            XLToast.showToast("请填写正确的手机号！");
            return false;
        }
        if (TextUtils.isEmpty(val_xh_tv.getText().toString().trim())){
            XLToast.showToast("请填写学号或通知书编号！");
            return false;
        }
        return true;
    }

    @Event({R.id.tool_back_iv})
    private void clicks(View view){
        switch (view.getId()){
            case R.id.tool_back_iv:
                finish();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finishAct(XMessageEvent event){
        if (event.getCode()==CHANGE_PHONE){
            this.finish();
        }
    }

    @Override
    protected void DetoryViewAndThing() {
        EventUtils.unRegister(this);
    }
}
