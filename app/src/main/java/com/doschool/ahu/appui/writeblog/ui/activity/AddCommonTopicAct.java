package com.doschool.ahu.appui.writeblog.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.discover.ui.bean.MicroTopic;
import com.doschool.ahu.appui.writeblog.ui.adapter.CommTicAdapter;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XRvUtils;
import com.doschool.ahu.widget.RvDivider;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import static android.view.View.VISIBLE;
import static com.doschool.ahu.configfile.ApiConfig.API_TOPIC_CARD;

/**
 * Created by X on 2018/11/7
 *
 * 常用话题
 */
public class AddCommonTopicAct extends BaseActivity {

    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;

    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.comm_rv)
    private XRecyclerView comm_rv;
    private LinearLayoutManager linearLayoutManager;
    private boolean isRef=false;
    private int lastId=0;
    private CommTicAdapter commTicAdapter;
    private ArrayMap<String,String> cMap=new ArrayMap<>();

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_addcom_tic_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        tool_back_iv.setVisibility(VISIBLE);
        tool_title_tv.setVisibility(VISIBLE);
        tool_title_tv.setText("添加话题");
        cMap= XLNetHttps.getBaseMap(this);
        initV();
        initDta();
    }

    private void initV(){
        comm_rv.addItemDecoration(new RvDivider(this, LinearLayoutManager.VERTICAL,R.drawable.divider_hight));
        linearLayoutManager=new LinearLayoutManager(this);
        XRvUtils.initRv(comm_rv,linearLayoutManager,LinearLayoutManager.VERTICAL,true,true,false);
        commTicAdapter=new CommTicAdapter(this);
        comm_rv.setAdapter(commTicAdapter);
        comm_rv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRef=true;
                lastId=0;
                initDta();
            }

            @Override
            public void onLoadMore() {
            }
        });

        commTicAdapter.setOnTicListener(ticName -> {
            Intent intent = new Intent();
            intent.putExtra("ticname", ticName);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private void initDta(){
        XLNetHttps.request(API_TOPIC_CARD, cMap,  MicroTopic.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                MicroTopic microTopic= XLGson.fromJosn(result,MicroTopic.class);
                if (microTopic.getCode()==0){
                    if (lastId==0){
                        commTicAdapter.getList().clear();
                    }
                    if (microTopic.getData()!=null && microTopic.getData().size()>0){
                        commTicAdapter.addDatas(microTopic.getData());
                    }
                    lastId=microTopic.getData().get(microTopic.getData().size()-1).getMicroblogTopicDO().getId();
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
                if (isRef){
                    comm_rv.refreshComplete();
                    isRef=false;
                }
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
        XRvUtils.destroyRv(comm_rv);
    }
}
