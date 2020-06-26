package com.doschool.ahu.appui.mine.ui.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.mine.ui.adapter.JoinAdapter;
import com.doschool.ahu.appui.mine.ui.bean.MineTopicBean;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XRvUtils;
import com.doschool.ahu.widget.RvDivider;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;


/**
 * Created by X on 2018/9/25
 */
public class MineTopicActivity extends BaseActivity {

    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;

    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.jrv)
    private XRecyclerView jrv;


    private RecyclerView mine_trv;
    private LinearLayoutManager layoutManager;

    private boolean isRef=false;
    private ArrayMap<String,String> joinMap=new ArrayMap<>();
    private int joinId=0;
    private JoinAdapter joinAdapter;

    private JoinAdapter hotAdapter;
    private ArrayMap<String,String> hot=new ArrayMap<>();

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_minetopic_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        tool_back_iv.setVisibility(View.VISIBLE);
        tool_title_tv.setVisibility(View.VISIBLE);
        tool_title_tv.setText("我的话题");

        joinMap= XLNetHttps.getBaseMap(this);
        hot=XLNetHttps.getBaseMap(this);
        initJ();
        addView();
        initData();
        initHot();
    }

    private void initJ(){
        jrv.addItemDecoration(new RvDivider(this,LinearLayoutManager.VERTICAL,R.drawable.divider_gray));
        XRvUtils.initRv(jrv,new LinearLayoutManager(this),LinearLayoutManager.VERTICAL,true,true,false);

        hotAdapter=new JoinAdapter(this);
        jrv.setAdapter(hotAdapter);

        jrv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRef=true;
                initData();
                initHot();
            }

            @Override
            public void onLoadMore() {

            }
        });

    }

    private void addView(){
        View view= LayoutInflater.from(this).inflate(R.layout.mine_topic_head_lay,null);
        jrv.addHeaderView(view);
        mine_trv=view.findViewById(R.id.mine_trv);

        layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mine_trv.setLayoutManager(layoutManager);
        mine_trv.addItemDecoration(new RvDivider(this,LinearLayoutManager.VERTICAL,R.drawable.divider_gray));
        joinAdapter=new JoinAdapter(this);
        mine_trv.setAdapter(joinAdapter);
    }

    //参与
    private void initData(){
        joinMap.put("lastId",String.valueOf(joinId));
        joinMap.put("size","10");
        XLNetHttps.request(ApiConfig.API_JOIN_TOPIC, joinMap, MineTopicBean.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                MineTopicBean bean= XLGson.fromJosn(result,MineTopicBean.class);
                if (bean.getCode()==0){
                    joinAdapter.setDatas(bean.getData());
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

    private void initHot(){
        XLNetHttps.request(ApiConfig.API_TOPIC_CARD, hot, MineTopicBean.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                MineTopicBean m=XLGson.fromJosn(result,MineTopicBean.class);
                if (m.getCode()==0){
                    hotAdapter.setDatas(m.getData());
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
                    jrv.refreshComplete();
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
        XRvUtils.destroyRv(jrv);
    }
}
