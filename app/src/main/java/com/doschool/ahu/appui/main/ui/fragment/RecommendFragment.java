package com.doschool.ahu.appui.main.ui.fragment;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.home.widget.BannerGroup;
import com.doschool.ahu.appui.home.widget.SlideFunPop;
import com.doschool.ahu.appui.main.event.XMessageEvent;
import com.doschool.ahu.appui.main.ui.activity.ReportActivity;
import com.doschool.ahu.appui.main.ui.adapter.RecomAdapter;
import com.doschool.ahu.appui.main.ui.bean.AppBannerDo;
import com.doschool.ahu.appui.main.ui.bean.RecommendVO;
import com.doschool.ahu.base.BaseFragment;
import com.doschool.ahu.base.model.BaseModel;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.EventUtils;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.utils.XRvUtils;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ViewInject;

import static com.doschool.ahu.appui.home.ui.activity.BlogDetailActivity.VOTE_CLICK;
import static com.doschool.ahu.configfile.ApiConfig.API_RECOMMEND_LIST;
import static com.doschool.ahu.configfile.CodeConfig.REPORT_BLOG;
import static com.doschool.ahu.configfile.CodeConfig.SCROLL_CODE_RECOMM;

/**
 * Created by X on 2019/1/17.
 *
 * 推荐
 */
public class RecommendFragment extends BaseFragment {

    @ViewInject(R.id.recom_xrv)
    private XRecyclerView recom_xrv;

    private LinearLayoutManager layoutManager;
    private ArrayMap<String,String> map=new ArrayMap<>();
    private String message="";
    private int page=1;
    private boolean isRefrsh=false;
    private boolean isLoad=false;
    private RecomAdapter recomAdapter;
    private LoginDao loginDao;

    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_recommend_layout;
    }

    @Override
    protected void initViewEvents(Bundle savedInstanceState) {
        EventUtils.register(this);
        loginDao=new LoginDao(getActivity());
        map=XLNetHttps.getBaseMap(getActivity());
        initRv();
        initData();
        bannerData();
        getRecom();
    }

    private void initRv(){
        layoutManager=new LinearLayoutManager(getActivity());
        XRvUtils.initRv(recom_xrv,layoutManager,LinearLayoutManager.VERTICAL,true,true,true);

        recomAdapter=new RecomAdapter(getActivity(),"mic");
        recom_xrv.setAdapter(recomAdapter);

        recom_xrv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRefrsh=true;
                bannerGroup.cancel();
                bannerData();
                page=1;
                message="";
                getRecom();
            }

            @Override
            public void onLoadMore() {
                isLoad=true;
                page++;
                getRecom();
            }
        });

        //功能键
        recomAdapter.setBlogFunctionListener((position, type, blogId, userId) -> {
            SlideFunPop slideFunPop=new SlideFunPop(getActivity());
            slideFunPop.onData(userId)
                    .showPopupWindow();
            slideFunPop.setOnFunClick(() -> {
                if (loginDao!=null){
                    if (userId ==loginDao.getObject().getUserDO().getId()){
                        deleteBlog(position, blogId);
                    }else {
                        Bundle bundle=new Bundle();
                        bundle.putInt("type",REPORT_BLOG);
                        bundle.putInt("Id", blogId);
                        IntentUtil.toActivity(getActivity(),bundle, ReportActivity.class);
                    }
                }
            });
        });
    }

    private BannerGroup bannerGroup;
    private void initData(){
        bannerGroup=new BannerGroup(getActivity());
        recom_xrv.addHeaderView(bannerGroup);
    }

    private void bannerData(){
        XLNetHttps.request(ApiConfig.API_BANNER, XLNetHttps.getBaseMap(getActivity()),  AppBannerDo.class, new XLCallBack() {
                    @Override
                    public void XLSucc(String result) {
                        AppBannerDo appBannerDo=XLGson.fromJosn(result,AppBannerDo.class);
                        if (appBannerDo.getCode()==0){
                            if (appBannerDo.getData()!=null && appBannerDo.getData().size()>0){
                                bannerGroup.updateUI(appBannerDo.getData());
                            }else {
                                bannerGroup.removeAllViews();
                            }
                        }else {
                            bannerGroup.removeAllViews();
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

    private void getRecom(){
        map.put("page",String.valueOf(page));
        map.put("message",message);
        XLNetHttps.request(API_RECOMMEND_LIST, map,  RecommendVO.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                RecommendVO recommendVO=XLGson.fromJosn(result,RecommendVO.class);
                if (recommendVO.getCode()==0){
                    if (TextUtils.isEmpty(message)){
                        recomAdapter.clearAdapter();
                    }
                    if (recommendVO.getData().getMicroblogVOList()!=null && recommendVO.getData().getMicroblogVOList().size()>0){
                        recomAdapter.addDatas(recommendVO.getData().getMicroblogVOList());
                    }
                    message=recommendVO.getData().getMessage();
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
                    recom_xrv.refreshComplete();
                    isRefrsh=false;
                }

                if (isLoad){
                    recom_xrv.loadMoreComplete();
                    isLoad=false;
                }
            }
        });
    }


    //删除微博
    private void deleteBlog(final int position, int blogId){
        ArrayMap<String,String> delMap=XLNetHttps.getBaseMap(getActivity());
        delMap.put("blogId",String.valueOf(blogId));
        XLNetHttps.request(ApiConfig.API_DELETEBLOG, delMap, BaseModel.class,  new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                BaseModel baseModel=XLGson.fromJosn(result,BaseModel.class);
                if (baseModel.getCode()==0){
                    recomAdapter.getList().remove(position);
                    recomAdapter.notifyItemRemoved(position);
                    recomAdapter.notifyDataSetChanged();
                    XLToast.showToast("删除成功！");
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

    //实现回滚顶部   刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void scrollEvent1(XMessageEvent event){
        if (event.getCode()==SCROLL_CODE_RECOMM ){
            LinearLayoutManager linearLayoutManager= (LinearLayoutManager) recom_xrv.getLayoutManager();
            int firstPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            if (firstPosition==0){
                recom_xrv.refresh();
            }else {
                recom_xrv.scrollToPosition(0);
            }
        }else if (event.getCode()==VOTE_CLICK){//详情页完---->投票刷新
            recom_xrv.refresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventUtils.unRegister(this);
        XRvUtils.destroyRv(recom_xrv);
    }
}
