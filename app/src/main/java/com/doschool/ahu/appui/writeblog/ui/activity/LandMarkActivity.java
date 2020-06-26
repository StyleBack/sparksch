package com.doschool.ahu.appui.writeblog.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.writeblog.ui.adapter.LandMarkAdapter;
import com.doschool.ahu.appui.writeblog.ui.bean.LandMarkBean;
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
import static com.doschool.ahu.appui.writeblog.ui.activity.ReleaseMicroBlogAct.CODE_ID;
import static com.doschool.ahu.configfile.ApiConfig.API_LANDMARK;
import static com.doschool.ahu.configfile.AppConfig.SCHOOL_ID;

/**
 * Created by X on 2018/11/7
 *
 * 地标
 */
public class LandMarkActivity extends BaseActivity {

    @ViewInject(R.id.tool_left_tv)
    private TextView tool_left_tv;

    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.land_rv)
    private XRecyclerView land_rv;
    private LinearLayoutManager linearLayoutManager;
    private boolean isRef=false;
    private boolean isLoad=false;
    private int lastId=0;
    private ArrayMap<String,String> landMap=new ArrayMap<>();
    private LandMarkAdapter landMarkAdapter;
    private int id;
    private Intent intents;

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_lanmark_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        tool_left_tv.setVisibility(VISIBLE);
        tool_title_tv.setVisibility(VISIBLE);
        tool_left_tv.setText("关闭");
        tool_title_tv.setText("请选择您的地点");
        landMap= XLNetHttps.getBaseMap(this);

        proessIntent();
        if (intents!=null){
            id=intents.getExtras().getInt("id");
        }
        initRv();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        proessIntent();
    }
    private void proessIntent(){
        intents=getIntent();
    }

    private void initRv(){
        land_rv.addItemDecoration(new RvDivider(this, LinearLayoutManager.VERTICAL,R.drawable.divider_hight));
        linearLayoutManager=new LinearLayoutManager(this);
        XRvUtils.initRv(land_rv,linearLayoutManager,LinearLayoutManager.VERTICAL,true,true,true);
        landMarkAdapter=new LandMarkAdapter(this,id);
        land_rv.setAdapter(landMarkAdapter);
        land_rv.setLoadingListener(new XRecyclerView.LoadingListener() {
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

        landMarkAdapter.setOnLandListener((id, landName) -> {
            Intent intent = new Intent();
            intent.putExtra("lid",id);
            intent.putExtra("name", landName);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private void initData(){
        landMap.put("lastId", String.valueOf(lastId));
        landMap.put("size","20");
        XLNetHttps.request(API_LANDMARK, landMap,LandMarkBean.class,  new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                LandMarkBean markBean= XLGson.fromJosn(result,LandMarkBean.class);
                if (markBean.getCode()==0){
                    if (lastId==0){
                        landMarkAdapter.getList().clear();
                        LandMarkBean.DataBean dataBean=new LandMarkBean.DataBean();
                        dataBean.setId(CODE_ID);
                        dataBean.setPlaceId("");
                        dataBean.setPlaceName("不显示位置");
                        dataBean.setSchoolId(SCHOOL_ID);
                        dataBean.setGmtCreate("");
                        dataBean.setGmtModified("");
                        dataBean.setIsDeleted(0);
                        markBean.getData().add(0,dataBean);
                    }
                    if (markBean.getData()!=null && markBean.getData().size()>0){
                        landMarkAdapter.addDatas(markBean.getData());
                    }
                    lastId=markBean.getData().get(markBean.getData().size()-1).getId();
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
                    land_rv.refreshComplete();
                    isRef=false;
                }

                if (isLoad){
                    land_rv.loadMoreComplete();
                    isLoad=false;
                }
            }
        });
    }

    @Event({R.id.tool_left_tv})
    private void clicks(View view){
        switch (view.getId()){
            case R.id.tool_left_tv:
                finish();
                break;
        }
    }

    @Override
    protected void DetoryViewAndThing() {
        XRvUtils.destroyRv(land_rv);
    }
}
