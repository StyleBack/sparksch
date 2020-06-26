package com.doschool.ahu.appui.main.ui.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.VerifyBean;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import static com.doschool.ahu.configfile.ApiConfig.API_CVI;
import static com.doschool.ahu.configfile.ApiConfig.API_WAITVER;

/**
 * Created by X on 2018/10/25
 *
 * 审核等待
 */
public class VerifingActivity extends BaseActivity {


    @ViewInject(R.id.ving_tvwt)
    private TextView ving_tvwt;

    private ArrayMap<String,String> maps=new ArrayMap<>();
    private ArrayMap<String,String> cviMap=new ArrayMap<>();

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_verfing_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        maps= XLNetHttps.getBaseMap(this);
        cviMap=XLNetHttps.getBaseMap(this);

        waitFor();
    }

    private void waitFor(){
        XLNetHttps.request(API_WAITVER, maps, VerifyBean.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                VerifyBean verifyBean= XLGson.fromJosn(result,VerifyBean.class);
                if (verifyBean.getCode()==0){
                    ving_tvwt.setText("已等待"+verifyBean.getData().getWaitTime());
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

    private void cvi(){
        XLNetHttps.request(API_CVI, cviMap, VerifyBean.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                VerifyBean verifyBean= XLGson.fromJosn(result,VerifyBean.class);
                if (verifyBean.getCode()==0){
                    XLToast.showToast(verifyBean.getMessage());
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

    @Event({R.id.ving_tvk,R.id.ving_tvcvi})
    private void clk(View view){
        switch (view.getId()){
            case R.id.ving_tvk:
                finish();
                break;
            case R.id.ving_tvcvi://催
                cvi();
                break;
        }
    }

    @Override
    protected void DetoryViewAndThing() {

    }
}
