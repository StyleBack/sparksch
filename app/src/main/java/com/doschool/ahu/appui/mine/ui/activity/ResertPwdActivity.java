package com.doschool.ahu.appui.mine.ui.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.reglogin.ui.LoginActivity;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.base.model.BaseModel;
import com.doschool.ahu.db.AppConfigDao;
import com.doschool.ahu.db.ConversationDao;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.widget.ReLoginDialog;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import static com.doschool.ahu.configfile.ApiConfig.API_RES_PWD;

/**
 * Created by X on 2018/10/8
 * 修改密码
 */
public class ResertPwdActivity extends BaseActivity {

    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;
    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.resert_ex_or)
    private EditText resert_ex_or;

    @ViewInject(R.id.resert_ex_new)
    private EditText resert_ex_new;

    @ViewInject(R.id.resert_ex_re)
    private EditText resert_ex_re;

    private ArrayMap<String,String> map=new ArrayMap<>();

    private LoginDao loginDao;
    private ConversationDao conversationDao;
    private AppConfigDao appConfigDao;

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_resert_pwd;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        tool_back_iv.setVisibility(View.VISIBLE);
        tool_title_tv.setVisibility(View.VISIBLE);
        tool_title_tv.setText("修改密码");
        map= XLNetHttps.getBaseMap(this);
        loginDao=new LoginDao(this);
        conversationDao=new ConversationDao(this);
        appConfigDao=new AppConfigDao(this);
    }


    @Event({R.id.tool_back_iv,R.id.resert_tv})
    private void onClicks(View view){
        switch (view.getId()){
            case R.id.tool_back_iv:
                finish();
                break;
            case R.id.resert_tv://重置
                changePwd();
                break;
        }
    }

    private void changePwd(){
        if (!isOK()){
            return;
        }

        map.put("oldPassword",resert_ex_or.getText().toString().trim());
        map.put("password",resert_ex_new.getText().toString().trim());
        XLNetHttps.request(API_RES_PWD, map, BaseModel.class,  new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                BaseModel baseModel= XLGson.fromJosn(result,BaseModel.class);
                if (baseModel.getCode()==0){
                    loginDao.clearUserTable();
                    conversationDao.clearConTable();
                    appConfigDao.clearUserTable();
                    ReLoginDialog reLoginDialog=new ReLoginDialog(ResertPwdActivity.this, dialog -> {
                        IntentUtil.toActivity(ResertPwdActivity.this,null, LoginActivity.class);
                        dialog.dismiss();
                    });
                    reLoginDialog.setCancelable(false);
                    reLoginDialog.setCanceledOnTouchOutside(false);
                    reLoginDialog.show();
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

    private boolean isOK(){
        if (TextUtils.isEmpty(resert_ex_or.getText().toString().trim())){
            XLToast.showToast("请填写原密码！");
            return false;
        }

        if (TextUtils.isEmpty(resert_ex_new.getText().toString().trim())){
            XLToast.showToast("请填写新密码！");
            return false;
        }

        if (TextUtils.isEmpty(resert_ex_re.getText().toString().trim())){
            XLToast.showToast("请再次确认密码！");
            return false;
        }

        if (!TextUtils.equals(resert_ex_new.getText().toString().trim(),resert_ex_re.getText().toString().trim())){
            XLToast.showToast("两次密码不一致！");
            return false;
        }
        return true;
    }

    @Override
    protected void DetoryViewAndThing() {

    }
}
