package com.doschool.ahu.appui.main.ui.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.ReportDo;
import com.doschool.ahu.appui.main.widget.ConsGradePop;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.base.model.BaseModel;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import static com.doschool.ahu.configfile.ApiConfig.API_REPORT;

/**
 * Created by X on 2018/10/15
 *
 * 举报
 */
public class ReportActivity extends BaseActivity {


    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;
    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;
    @ViewInject(R.id.tool_right_tv)
    private TextView tool_right_tv;


    @ViewInject(R.id.rep_rltype)
    private RelativeLayout rep_rltype;

    @ViewInject(R.id.rep_txtype)
    private TextView rep_txtype;

    @ViewInject(R.id.rep_ex)
    private EditText rep_ex;

    private int type;
    private int targetId;
    private ReportDo reportDo;
    private List<ReportDo> list=new ArrayList<>();
    private int typeID;
    private ArrayMap<String,String> map=new ArrayMap<>();

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_report_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        tool_back_iv.setVisibility(View.VISIBLE);
        tool_title_tv.setVisibility(View.VISIBLE);
        tool_title_tv.setText("举报");
        tool_right_tv.setVisibility(View.VISIBLE);
        tool_right_tv.setTextColor(getResources().getColor(R.color.new_color));
        tool_right_tv.setText("提交");

        map= XLNetHttps.getBaseMap(this);

        if (getIntent().getExtras()!=null){
            type=getIntent().getExtras().getInt("type");
            targetId=getIntent().getExtras().getInt("Id");
        }

        reportDo=new ReportDo();
        list=reportDo.getReportList();
    }

    private void init(){
        ConsGradePop consGradePop=new ConsGradePop(this);
        consGradePop.onReport(list).showPopupWindow();
        consGradePop.setOnReportListener((type, id) -> {
            typeID=id;
            rep_txtype.setText(type);
        });
    }

    private void gotoReport(){
        if (TextUtils.isEmpty(rep_txtype.getText())){
            XLToast.showToast("请选择举报类型！");
            return;
        }
        map.put("objType",String.valueOf(type));
        map.put("objId",String.valueOf(targetId));
        map.put("reasonType",String.valueOf(typeID));
        if (!TextUtils.isEmpty(rep_ex.getText().toString().trim())){
            map.put("reason",rep_ex.getText().toString().trim());
        }

        XLNetHttps.request(API_REPORT, map, BaseModel.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                BaseModel baseModel= XLGson.fromJosn(result,BaseModel.class);
                if (baseModel.getCode()==0){
                    XLToast.showToast("对于您的举报，一经核实，我们会对其进行相应的处理！");
                    ReportActivity.this.finish();
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

    }

    @Event({R.id.tool_back_iv,R.id.tool_right_tv,R.id.rep_rltype})
    private void clicks(View view){
        switch (view.getId()){
            case R.id.tool_back_iv:
                finish();
                break;
            case R.id.tool_right_tv://提交
                gotoReport();
                break;
            case R.id.rep_rltype:
                init();
                break;
        }
    }
}
