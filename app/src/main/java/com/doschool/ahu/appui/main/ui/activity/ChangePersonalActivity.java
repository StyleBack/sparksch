package com.doschool.ahu.appui.main.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.event.XMessageEvent;
import com.doschool.ahu.appui.reglogin.bean.LoginVO;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.EventUtils;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.widget.WrapEditText;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;


/**
 * Created by X on 2018/10/11
 */
public class ChangePersonalActivity extends BaseActivity {

    public static final int CHANGE_CODE=-1000;

    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;
    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;
    @ViewInject(R.id.tool_right_tv)
    private TextView tool_right_tv;

    @ViewInject(R.id.cha_wrap_ex)
    private WrapEditText cha_wrap_ex;


    private String change,id,title;
    private int code;
    private LoginDao loginDao;
    private ArrayMap<String,String> map=new ArrayMap<>();

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_change_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        tool_back_iv.setVisibility(View.VISIBLE);
        tool_title_tv.setVisibility(View.VISIBLE);
        tool_right_tv.setVisibility(View.VISIBLE);
        tool_right_tv.setText("保存");
        loginDao=new LoginDao(this);
        map= XLNetHttps.getBaseMap(this);

        if (getIntent().getExtras()!=null){
            change=getIntent().getExtras().getString("change");
            id=getIntent().getExtras().getString("id");
            code=getIntent().getExtras().getInt("code");
            title=getIntent().getExtras().getString("title");
        }
        tool_title_tv.setText("修改"+title);
        cha_wrap_ex.setText(change);

        if (code==1){
            cha_wrap_ex.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
        }
    }

    @Override
    protected void DetoryViewAndThing() {

    }

    @Event({R.id.tool_back_iv,R.id.tool_right_tv})
    private void clicks(View view){
        switch (view.getId()){
            case R.id.tool_back_iv:
                finish();
                break;
            case R.id.tool_right_tv:
                if (!TextUtils.isEmpty(cha_wrap_ex.getText().toString().trim())){
                    save(code);
                }else {
                    XLToast.showToast("信息不能为空哟！");
                }
                break;
        }
    }

    private void save(int code){
        map.put("id",id);
        switch (code){
            case 1:
                map.put("nickName",cha_wrap_ex.getText().toString().trim());
                break;
            case 6:
                map.put("loveStatus",cha_wrap_ex.getText().toString().trim());
                break;
            case 8:
                map.put("hobby",cha_wrap_ex.getText().toString().trim());
                break;
            case 9:
                map.put("selfIntro",cha_wrap_ex.getText().toString().trim());
                break;
        }

        XLNetHttps.request(ApiConfig.API_UPDATE_USER, map, LoginVO.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                LoginVO loginV= XLGson.fromJosn(result,LoginVO.class);
                if (loginV.getCode()==0){
                    loginDao.clearUserTable();
                    loginDao.saveObject(loginV.getData());

                    EventUtils.onPost(new XMessageEvent(CHANGE_CODE));

                    Intent intent=new Intent();
                    intent.putExtra("infos",cha_wrap_ex.getText().toString().trim());
                    setResult(RESULT_OK,intent);
                    finish();
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
}
