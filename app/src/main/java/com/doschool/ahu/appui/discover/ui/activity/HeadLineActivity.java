package com.doschool.ahu.appui.discover.ui.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.discover.ui.adapter.LineAdapter;
import com.doschool.ahu.appui.discover.ui.bean.LineBean;
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

import static com.doschool.ahu.configfile.ApiConfig.API_ARTICLE;

/**
 * Created by X on 2018/9/27
 *
 * 头条新闻
 */
public class HeadLineActivity extends BaseActivity {


    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;

    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.lineRv)
    private XRecyclerView lineRv;
    private LinearLayoutManager manager;
    private boolean isRef=false;
    private boolean isLoad=false;
    private int page=1;

    private ArrayMap<String,String> map=new ArrayMap<>();
    private LineAdapter lineAdapter;

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_headline_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        tool_back_iv.setVisibility(View.VISIBLE);
        tool_title_tv.setVisibility(View.VISIBLE);
        tool_title_tv.setText("头条新闻");

        map= XLNetHttps.getBaseMap(this);
        initRl();
        initDat();
    }

    private void initRl(){
        lineRv.addItemDecoration(new RvDivider(this,LinearLayoutManager.VERTICAL,R.drawable.divider_hight));
        manager=new LinearLayoutManager(this);
        XRvUtils.initRv(lineRv,manager,LinearLayoutManager.VERTICAL,true,true,true);

        lineAdapter=new LineAdapter(this);
        lineRv.setAdapter(lineAdapter);
        lineRv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRef=true;
                page=1;
                initDat();
            }

            @Override
            public void onLoadMore() {
                isLoad=true;
                page++;
                initDat();
            }
        });
    }

    private void initDat(){
        map.put("page",String.valueOf(page));
        map.put("size","15");
        XLNetHttps.request(API_ARTICLE, map, LineBean.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                LineBean lineBean= XLGson.fromJosn(result,LineBean.class);
                if (lineBean.getCode()==0){
                    if (page==1){
                        lineAdapter.getList().clear();
                    }
                    lineAdapter.addDatas(lineBean.getData());
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
                    lineRv.refreshComplete();
                    isRef=false;
                }

                if (isLoad){
                    lineRv.loadMoreComplete();
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
        XRvUtils.destroyRv(lineRv);
    }
}
