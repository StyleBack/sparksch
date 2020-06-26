package com.doschool.ahu.appui.mine.ui.fragment;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVO;
import com.doschool.ahu.appui.mine.ui.adapter.MeAdapter;
import com.doschool.ahu.base.BaseFragment;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.configfile.CodeConfig;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XRvUtils;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ViewInject;


/**
 * Created by X on 2019/1/12.
 *
 * 我的日记
 */
public class MeDiaryFragment extends BaseFragment {

    @ViewInject(R.id.me_xryv)
    private XRecyclerView me_xryv;

    private int lastId=0;
    private boolean isRefrsh=false;
    private boolean isLoad=false;
    private ArrayMap<String,String> maps=new ArrayMap<>();
    private LinearLayoutManager linearLayoutManager;
    private LoginDao loginDao;
    private MeAdapter meAdapter;

    @Override
    protected int getContentLayoutID() {
        return R.layout.me_diary_fragment;
    }

    @Override
    protected void initViewEvents(Bundle savedInstanceState) {
        loginDao=new LoginDao(getActivity());
        maps=XLNetHttps.getBaseMap(getActivity());
        initRv();
        initSingle();
    }

    private void initRv(){
        linearLayoutManager=new LinearLayoutManager(getActivity());
        XRvUtils.initRv(me_xryv,linearLayoutManager,LinearLayoutManager.VERTICAL,true,true,true);
        meAdapter=new MeAdapter(getActivity());
        me_xryv.setAdapter(meAdapter);

        me_xryv.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        if (loginDao.getObject()!=null && loginDao.getObject().getUserDO()!=null){
            maps.put("targetUserId",String.valueOf(loginDao.getObject().getUserDO().getId()));
        }
        XLNetHttps.request(ApiConfig.API_BLOG_LIST, maps,  MicroblogVO.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                MicroblogVO microblogVO=XLGson.fromJosn(result,MicroblogVO.class);
                if (microblogVO.getCode()==0){
                    if (lastId==0){
                        meAdapter.clearAdapter();
                    }
                    if (microblogVO.getData()!=null && microblogVO.getData().size()>0){
                        meAdapter.addDatas(microblogVO.getData());
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
                    me_xryv.refreshComplete();
                    isRefrsh=false;
                }

                if (isLoad){
                    me_xryv.refreshComplete();
                    isLoad=false;
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XRvUtils.destroyRv(me_xryv);
    }
}
