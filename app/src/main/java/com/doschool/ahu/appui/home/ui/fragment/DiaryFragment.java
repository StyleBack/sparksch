package com.doschool.ahu.appui.home.ui.fragment;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.home.ui.adapter.OthAdapter;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVO;
import com.doschool.ahu.base.BaseFragment;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.configfile.CodeConfig;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XRvUtils;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ViewInject;


/**
 * Created by X on 2019/1/10.
 *
 * 日记界面
 */
public class DiaryFragment extends BaseFragment {

    private int tagId;

    @ViewInject(R.id.oth_rv)
    private XRecyclerView oth_rv;
    private LinearLayoutManager linearLayoutManager;
    private OthAdapter othAdapter;

    private int lastId=0;
    private boolean isRefrsh=false;
    private boolean isLoad=false;
    private ArrayMap<String,String> maps=new ArrayMap<>();

    public static DiaryFragment newInstance(int userId){
        DiaryFragment diaryFragment=new DiaryFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("taggetId",userId);
        diaryFragment.setArguments(bundle);
        return diaryFragment;
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_diary_layout;
    }

    @Override
    protected void initViewEvents(Bundle savedInstanceState) {
        if (getArguments()!=null){
            tagId=getArguments().getInt("taggetId");
        }
        maps=XLNetHttps.getBaseMap(getActivity());
        initRv();
        initSingle();
    }

    private void initRv(){
        linearLayoutManager=new LinearLayoutManager(getActivity());
        XRvUtils.initRv(oth_rv,linearLayoutManager,LinearLayoutManager.VERTICAL,true,true,true);
        othAdapter=new OthAdapter(getActivity());
        oth_rv.setAdapter(othAdapter);

        oth_rv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRefrsh=true;
                lastId=0;
                initSingle();
            }

            @Override
            public void onLoadMore() {
                isLoad=true;
                initSingle();
            }
        });
    }

    private void initSingle(){
        maps.put("microblogType",String.valueOf(CodeConfig.BLOG_SOMEONE));
        maps.put("lastId",String.valueOf(lastId));
        maps.put("size","15");
        maps.put("targetUserId",String.valueOf(tagId));
        XLNetHttps.request(ApiConfig.API_BLOG_LIST, maps, MicroblogVO.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                MicroblogVO microblogVO=XLGson.fromJosn(result,MicroblogVO.class);
                if (microblogVO.getCode()==0){
                    if (lastId==0){
                        othAdapter.clearAdapter();
                    }
                    if (microblogVO.getData()!=null && microblogVO.getData().size()>0){
                        othAdapter.addDatas(microblogVO.getData());
                    }
                    lastId=microblogVO.getData().get(microblogVO.getData().size()-1).getMicroblogMainDO().getId();
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
                if (isRefrsh){
                    oth_rv.refreshComplete();
                    isRefrsh=false;
                }

                if (isLoad){
                    oth_rv.refreshComplete();
                    isLoad=false;
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XRvUtils.destroyRv(oth_rv);
    }
}
