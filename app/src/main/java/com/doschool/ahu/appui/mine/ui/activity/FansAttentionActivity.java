package com.doschool.ahu.appui.mine.ui.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.mine.ui.adapter.FansAttentAdapter;
import com.doschool.ahu.appui.writeblog.ui.bean.AtBean;
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
 * Created by X on 2018/9/24
 *
 * 粉丝  关注
 */
public class FansAttentionActivity extends BaseActivity {

    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;

    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.fans_rv)
    private XRecyclerView fans_rv;

    private int lop;
    private int lastId=0;
    private LinearLayoutManager layoutManager;
    private boolean isRef=false;
    private boolean isLoad=false;
    private ArrayMap<String,String> map=new ArrayMap<>();
    private FansAttentAdapter adapter;

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_fans_atten;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {

        if (getIntent().getExtras()!=null){
            lop=getIntent().getExtras().getInt("lop");
        }
        tool_back_iv.setVisibility(View.VISIBLE);
        tool_title_tv.setVisibility(View.VISIBLE);
        if (lop== CodeConfig.FOLLOW_FS){
            tool_title_tv.setText("粉丝");
        }else {
            tool_title_tv.setText("关注");
        }

        map= XLNetHttps.getBaseMap(this);
        initRV();
        initData();
    }

    private void initRV (){
        fans_rv.addItemDecoration(new RvDivider(this,LinearLayoutManager.VERTICAL,R.drawable.divider_hight));
        layoutManager=new LinearLayoutManager(this);
        XRvUtils.initRv(fans_rv,layoutManager,LinearLayoutManager.VERTICAL,true,true,true);

        adapter=new FansAttentAdapter(this);
        fans_rv.setAdapter(adapter);

        fans_rv.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        map.put("followType", String.valueOf(lop));
        map.put("lastId",String.valueOf(lastId));
        map.put("size","20");
        XLNetHttps.request(ApiConfig.API_FOLLWERS, map, AtBean.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                AtBean atBean= XLGson.fromJosn(result,AtBean.class);
                if (atBean.getCode()==0){
                    if (lastId==0){
                        adapter.getList().clear();
                    }
                    if (atBean.getData()!=null && atBean.getData().size()>0){
                        adapter.addDatas(atBean.getData());
                    }
                    lastId=atBean.getData().get(atBean.getData().size()-1).getUserId();
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
                    fans_rv.refreshComplete();
                    isRef=false;
                }

                if (isLoad){
                    fans_rv.loadMoreComplete();
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
        XRvUtils.destroyRv(fans_rv);
    }
}
