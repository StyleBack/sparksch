package com.doschool.ahu.appui.main.ui.fragment;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.home.widget.SlideFunPop;
import com.doschool.ahu.appui.main.event.XMessageEvent;
import com.doschool.ahu.appui.main.ui.activity.ReportActivity;
import com.doschool.ahu.appui.main.ui.adapter.MicroBlogAdapter;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVO;
import com.doschool.ahu.base.BaseFragment;
import com.doschool.ahu.base.model.BaseModel;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.configfile.CodeConfig;
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
import static com.doschool.ahu.configfile.CodeConfig.REPORT_BLOG;
import static com.doschool.ahu.configfile.CodeConfig.SCROLL_CODE_FOLLOW;

/**
 * Created by X on 2018/8/22
 *
 * 关注
 */
public class ConcernFragment extends BaseFragment {


    @ViewInject(R.id.hfm_xrv)
    private XRecyclerView hfm_xrv;

    private LinearLayoutManager linearLayoutManager;

    private MicroBlogAdapter microBlogAdapter;
    private boolean mNeedRestart;
    private boolean isRefrsh=false;
    private boolean isLoad=false;
    private int lastId=0;
    private ArrayMap<String,String> maps=new ArrayMap<>();
    private LoginDao loginDao;

    @ViewInject(R.id.blog_empt_ll)
    private LinearLayout blog_empt_ll;

    @Override
    protected int getContentLayoutID() {
        return R.layout.hfragment_layout;
    }

    @Override
    protected void initViewEvents(Bundle savedInstanceState) {
        EventUtils.register(this);
        loginDao=new LoginDao(getActivity());
        maps= XLNetHttps.getBaseMap(getActivity());
        initRv();
        getBlogList();
    }

    private void initRv(){
        linearLayoutManager=new LinearLayoutManager(getActivity());
        XRvUtils.initRv(hfm_xrv,linearLayoutManager,LinearLayoutManager.VERTICAL,true,true,true);

        microBlogAdapter=new MicroBlogAdapter(getActivity(),"mic");
        hfm_xrv.setAdapter(microBlogAdapter);

        hfm_xrv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRefrsh=true;
                lastId=0;
                getBlogList();
            }

            @Override
            public void onLoadMore() {
                isLoad=true;
                getBlogList();
            }
        });

        //功能键
        microBlogAdapter.setBlogFunctionListener((position, type, blogId, userId) -> {
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

    private void getBlogList(){
        maps.put("microblogType",String.valueOf(CodeConfig.BLOG_FOLLOW));
        maps.put("lastId",String.valueOf(lastId));
        maps.put("size","10");
        XLNetHttps.request(ApiConfig.API_BLOG_LIST, maps, MicroblogVO.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                MicroblogVO microblogVO= XLGson.fromJosn(result,MicroblogVO.class);
                if (microblogVO.getCode()==0){

                    if (microblogVO.getData()!=null && microblogVO.getData().size()==0){
                        if (lastId==0){
                            blog_empt_ll.setVisibility(View.VISIBLE);
                        }else {
                            blog_empt_ll.setVisibility(View.GONE);
                        }
                    }else {
                        blog_empt_ll.setVisibility(View.GONE);
                    }

                    if (lastId==0){
                        microBlogAdapter.clearAdapter();
                    }
                    if (microblogVO.getData()!=null && microblogVO.getData().size()>0){
                        microBlogAdapter.addDatas(microblogVO.getData());
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
                    hfm_xrv.refreshComplete();
                    isRefrsh=false;
                }

                if (isLoad){
                    hfm_xrv.loadMoreComplete();
                    isLoad=false;
                }
            }
        });
    }

    //删除微博
    private void deleteBlog(final int position, int blogId){
        ArrayMap<String,String> delMap=XLNetHttps.getBaseMap(getActivity());
        delMap.put("blogId",String.valueOf(blogId));
        XLNetHttps.request(ApiConfig.API_DELETEBLOG, delMap, BaseModel.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                BaseModel baseModel=XLGson.fromJosn(result,BaseModel.class);
                if (baseModel.getCode()==0){
                    microBlogAdapter.getList().remove(position);
                    microBlogAdapter.notifyItemRemoved(position);
                    microBlogAdapter.notifyDataSetChanged();
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
    public void scrollEvent2(XMessageEvent event){
        if (event.getCode()==SCROLL_CODE_FOLLOW ){
            LinearLayoutManager linearLayoutManager= (LinearLayoutManager) hfm_xrv.getLayoutManager();
            int firstPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            if (firstPosition==0){
                hfm_xrv.refresh();
            }else {
                hfm_xrv.scrollToPosition(0);
            }
        }else if (event.getCode()==VOTE_CLICK){//详情页完---->投票刷新
            hfm_xrv.refresh();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventUtils.unRegister(this);
        XRvUtils.destroyRv(hfm_xrv);
    }
}
