package com.doschool.ahu.appui.infors.ui.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.infors.ui.adapter.NotifyAssAdapter;
import com.doschool.ahu.appui.infors.ui.bean.PushRecordBean;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XRvUtils;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import static com.doschool.ahu.configfile.ApiConfig.API_PUSH_RECORD;

public class NotifyingAssistantAct extends BaseActivity {

    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;

    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.notify_rv)
    private XRecyclerView notify_rv;

    private ArrayMap<String,String> notiMap=new ArrayMap<>();
    private LinearLayoutManager layout;
    private NotifyAssAdapter notifyAssAdapter;
    private boolean isRh=false,isLoad=false;
    private int lastId=0;

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_notify_assistant;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        tool_back_iv.setVisibility(View.VISIBLE);
        tool_title_tv.setVisibility(View.VISIBLE);
        tool_title_tv.setText("通知小助手");
        notiMap=XLNetHttps.getBaseMap(this);
        initRV();
        initDta();
    }

    private void initRV(){
        layout=new LinearLayoutManager(this);
        XRvUtils.initRv(notify_rv,layout,LinearLayoutManager.VERTICAL,true,true,true);
        notifyAssAdapter=new NotifyAssAdapter(this);
        notify_rv.setAdapter(notifyAssAdapter);
        notify_rv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                lastId=0;
                isRh=true;
                initDta();
            }

            @Override
            public void onLoadMore() {
                isLoad=true;
                initDta();
            }
        });
    }

    private void initDta(){
        notiMap.put("size","20");
        notiMap.put("lastId",String.valueOf(lastId));
        XLNetHttps.request(API_PUSH_RECORD, notiMap, PushRecordBean.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                if (isRh){
                    notify_rv.refreshComplete();
                    isRh=false;
                }
                if (isLoad){
                    notify_rv.loadMoreComplete();
                    isLoad=false;
                }

                PushRecordBean push=XLGson.fromJosn(result,PushRecordBean.class);
                if (push.getCode()==0){
                    if (lastId==0){
                        notifyAssAdapter.getList().clear();
                    }
                    if (push.getData()!=null){
                        notifyAssAdapter.addDatas(push.getData());
                    }
                    lastId=push.getData().get(push.getData().size()-1).getId();
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
        XRvUtils.destroyRv(notify_rv);
    }
}
