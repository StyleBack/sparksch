package com.doschool.ahu.appui.main.ui.fragment;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.adapter.ToolAdapter;
import com.doschool.ahu.appui.main.ui.bean.ToolsBean;
import com.doschool.ahu.base.BaseFragment;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XRvUtils;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jaeger.library.StatusBarUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ViewInject;

import static com.doschool.ahu.configfile.ApiConfig.API_SERVICE_PRO;

/**
 * Created by X on 2019/1/12.
 */
public class TabServiceFragment extends BaseFragment {


    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.ts_rv)
    private XRecyclerView ts_rv;

    private ArrayMap<String,String> map=new ArrayMap<>();

    private LinearLayoutManager layout;
    private ToolAdapter toolAdapter;
    private boolean isRefresh=false;

    @Override
    public void onResume() {
        StatusBarUtil.setTranslucentForImageView(getActivity(),0,null);
        StatusBarUtil.setLightMode(getActivity());
        super.onResume();
        initService();
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.tab_service_fragment;
    }

    @Override
    protected void initViewEvents(Bundle savedInstanceState) {
        tool_title_tv.setVisibility(View.VISIBLE);
        tool_title_tv.setText("发现");
        map=XLNetHttps.getBaseMap(getActivity());

        initRv();
        initService();
    }

    private void initRv(){
        layout=new LinearLayoutManager(getActivity());
        XRvUtils.initRv(ts_rv,layout,LinearLayoutManager.VERTICAL,true,true,false);
        toolAdapter=new ToolAdapter(getActivity());
        ts_rv.setAdapter(toolAdapter);

        ts_rv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRefresh=true;
                initService();
            }

            @Override
            public void onLoadMore() {

            }
        });
    }

    private void initService(){
        XLNetHttps.request(API_SERVICE_PRO, map,  ToolsBean.class,new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                ToolsBean toolsBean=XLGson.fromJosn(result,ToolsBean.class);
                if (toolsBean.getCode()==0){
                    toolAdapter.setDatas(toolsBean.getData());
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
                if (isRefresh){
                    ts_rv.refreshComplete();
                    isRefresh=false;
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XRvUtils.destroyRv(ts_rv);
    }
}
