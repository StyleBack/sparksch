package com.doschool.ahu.appui.infors.ui.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.infors.ui.adapter.PraiseAdapter;
import com.doschool.ahu.appui.infors.ui.bean.PraiseBean;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.configfile.CodeConfig;
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
 * Created by X on 2018/9/22
 *
 * 赞我的  评论我的
 */
public class PraiseMeActivity extends BaseActivity {


    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;

    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.rv)
    private XRecyclerView rv;

    private LinearLayoutManager manager;
    private boolean isRef=false;
    private boolean isLoad=false;

    private PraiseAdapter praiseAdapter;

    private ArrayMap<String,String> map=new ArrayMap<>();
    private int lastId=0;
    private String type;
    private String HTTP_URL;

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_praiseme_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {

        if (getIntent().getExtras()!=null){
            type=getIntent().getExtras().getString("type");
        }

        tool_back_iv.setVisibility(View.VISIBLE);
        tool_title_tv.setVisibility(View.VISIBLE);
        if (TextUtils.equals(type,CodeConfig.MSG_TYPE_PRAISE)){
            tool_title_tv.setText("赞我的");
            HTTP_URL= ApiConfig.API_INFOS_LIKE;
        }else {
            tool_title_tv.setText("评论我的");
            HTTP_URL= ApiConfig.API_INFOS_MSG;
        }

        map= XLNetHttps.getBaseMap(this);
        init();
        initData();
    }

    private void init(){
        rv.addItemDecoration(new RvDivider(this,LinearLayoutManager.VERTICAL,R.drawable.divider_gray));
        manager=new LinearLayoutManager(this);
        XRvUtils.initRv(rv,manager,LinearLayoutManager.VERTICAL,true,true,true);
        praiseAdapter=new PraiseAdapter(this);
        rv.setAdapter(praiseAdapter);
        rv.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        map.put("lastId",String.valueOf(lastId));
        map.put("size","10");
        XLNetHttps.request(HTTP_URL, map, PraiseBean.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                PraiseBean praiseBean= XLGson.fromJosn(result,PraiseBean.class);
                if (praiseBean.getCode()==0){
                    if (lastId==0){
                        praiseAdapter.getList().clear();
                    }
                    if (praiseBean.getData()!=null && praiseBean.getData().size()>0){
                        praiseAdapter.addDatas(praiseBean.getData());
                    }
                    lastId=praiseBean.getData().get(praiseBean.getData().size()-1).getMessageId();
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
                    rv.refreshComplete();
                    isRef=false;
                }

                if (isLoad){
                    rv.loadMoreComplete();
                    isLoad=false;
                }
            }
        });
    }

    @Event({R.id.tool_back_iv})
    private void click(View view){
        switch (view.getId()){
            case R.id.tool_back_iv:
                finish();
                break;
        }
    }

    @Override
    protected void DetoryViewAndThing() {
        XRvUtils.destroyRv(rv);
    }
}
