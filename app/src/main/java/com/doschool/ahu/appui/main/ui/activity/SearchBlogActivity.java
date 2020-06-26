package com.doschool.ahu.appui.main.ui.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.home.widget.SlideFunPop;
import com.doschool.ahu.appui.main.ui.adapter.MicroBlogAdapter;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVO;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.base.model.BaseModel;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XRvUtils;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import static com.doschool.ahu.configfile.CodeConfig.REPORT_BLOG;

/**
 * Created by X on 2018/9/28
 *
 * 搜索微博结果
 */
public class SearchBlogActivity extends BaseActivity {


    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;

    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.sbrv)
    private XRecyclerView sbrv;

    private int type;
    private String keyWords;
    private int lastId=0;
    private LinearLayoutManager manager;
    private boolean isRef=false;
    private boolean isLoad=false;
    private ArrayMap<String,String> maps=new ArrayMap<>();
    private MicroBlogAdapter microBlogAdapter;
    private LoginDao loginDao;


    @Override
    protected int getContentLayoutID() {
        return R.layout.act_search_blog_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        tool_back_iv.setVisibility(View.VISIBLE);
        tool_title_tv.setVisibility(View.VISIBLE);
        tool_title_tv.setText("日记");

        if (getIntent().getExtras()!=null){
            type=getIntent().getExtras().getInt("type");
            keyWords=getIntent().getExtras().getString("keywords");
        }
        loginDao=new LoginDao(this);
        maps= XLNetHttps.getBaseMap(this);
        initC();
        getBlogList();
    }

    private void initC(){
        manager=new LinearLayoutManager(this);
        XRvUtils.initRv(sbrv,manager,LinearLayoutManager.VERTICAL,true,true,true);

        microBlogAdapter=new MicroBlogAdapter(this,"mic");
        sbrv.setAdapter(microBlogAdapter);
        sbrv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRef=true;
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

            SlideFunPop slideFunPop=new SlideFunPop(SearchBlogActivity.this);
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
                        IntentUtil.toActivity(SearchBlogActivity.this,bundle, ReportActivity.class);
                    }
                }
            });
        });
    }

    private void getBlogList(){
        maps.put("searchType", String.valueOf(type));
        maps.put("keyword",keyWords);
        maps.put("lastId",String.valueOf(lastId));
        maps.put("size","10");
        XLNetHttps.request(ApiConfig.API_SEARCH_RESULT, maps, MicroblogVO.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                MicroblogVO microblogVO= XLGson.fromJosn(result,MicroblogVO.class);
                if (microblogVO.getCode()==0){
                    if (lastId==0){
                        microBlogAdapter.getList().clear();
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
                if (isRef){
                    sbrv.refreshComplete();
                    isRef=false;
                }

                if (isLoad){
                    sbrv.loadMoreComplete();
                    isLoad=false;
                }
            }
        });
    }

    //删除微博
    private void deleteBlog(final int position, int blogId){
        ArrayMap<String,String> delMap=XLNetHttps.getBaseMap(this);
        delMap.put("blogId",String.valueOf(blogId));
        XLNetHttps.request(ApiConfig.API_DELETEBLOG, delMap, BaseModel.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                BaseModel baseModel=XLGson.fromJosn(result,BaseModel.class);
                if (baseModel.getCode()==0){
                    microBlogAdapter.getList().remove(position);
                    microBlogAdapter.notifyItemRemoved(position);
                    microBlogAdapter.notifyDataSetChanged();
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
        XRvUtils.destroyRv(sbrv);
    }
}
