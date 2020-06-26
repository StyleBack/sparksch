package com.doschool.ahu.appui.main.ui.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
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
 * Created by X on 2018/9/28
 *
 * 搜索话题结果
 */
public class SearchTopicActivity extends BaseActivity {

    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;

    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.strv)
    private XRecyclerView strv;

    private boolean isRef=false;
    private boolean isLoad=false;
    private ArrayMap<String,String> map=new ArrayMap<>();
    private LinearLayoutManager layoutManager;
    private JoinAdapter hotAdapter;
    private int lastId;
    private int type;
    private String keyWords;

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_search_topic_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        tool_back_iv.setVisibility(View.VISIBLE);
        tool_title_tv.setVisibility(View.VISIBLE);
        tool_title_tv.setText("话题");
        if (getIntent().getExtras()!=null){
            type=getIntent().getExtras().getInt("type");
            keyWords=getIntent().getExtras().getString("keywords");
        }
        map= XLNetHttps.getBaseMap(this);
        init();
        initData();
    }

    private void init(){
        strv.addItemDecoration(new RvDivider(this,LinearLayoutManager.VERTICAL,R.drawable.divider_hight));
        layoutManager=new LinearLayoutManager(this);
        XRvUtils.initRv(strv,layoutManager,LinearLayoutManager.VERTICAL,true,true,true);

        hotAdapter=new JoinAdapter(this);
        strv.setAdapter(hotAdapter);

        strv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRef=true;
                lastId=0;
                initData();
            }

            @Override
            public void onLoadMore() {
                isLoad=true;
                initData();
            }
        });
    }

    private void initData(){
        map.put("searchType", String.valueOf(type));
        map.put("keyword",keyWords);
        map.put("lastId",String.valueOf(lastId));
        map.put("size","10");
        XLNetHttps.request(ApiConfig.API_SEARCH_RESULT, map, MineTopicBean.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                MineTopicBean m= XLGson.fromJosn(result,MineTopicBean.class);
                if (m.getCode()==0){
                    if (lastId==0){
                        hotAdapter.getList().clear();
                    }
                    if (m.getData()!=null && m.getData().size()>0){
                        hotAdapter.addDatas(m.getData());
                    }
                    lastId=m.getData().get(m.getData().size()-1).getMicroblogTopicDO().getId();
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
                    strv.refreshComplete();
                    isRef=false;
                }
                if (isLoad){
                    strv.refreshComplete();
                    isLoad=false;
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
        XRvUtils.destroyRv(strv);
    }
}
