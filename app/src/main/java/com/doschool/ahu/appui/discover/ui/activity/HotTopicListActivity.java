package com.doschool.ahu.appui.discover.ui.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.home.widget.SlideFunPop;
import com.doschool.ahu.appui.main.ui.activity.ReportActivity;
import com.doschool.ahu.appui.main.ui.adapter.MicroBlogAdapter;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVO;
import com.doschool.ahu.appui.writeblog.ui.activity.ReleaseMicroBlogAct;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.base.model.BaseModel;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.configfile.CodeConfig;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.AlertUtils;
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
 * Created by X on 2018/9/27
 *
 * 各类型话题微博列表
 */
public class HotTopicListActivity extends BaseActivity {


    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;

    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.tool_right_iv)
    private ImageView tool_right_iv;

    @ViewInject(R.id.htrv)
    private XRecyclerView htrv;
    private String id,name;

    private LinearLayoutManager manager;
    private boolean isRef=false;
    private boolean isLoad=false;
    private MicroBlogAdapter microBlogAdapter;
    private int lastId=0;
    private ArrayMap<String,String> maps=new ArrayMap<>();
    private LoginDao loginDao;
    @Override
    protected int getContentLayoutID() {
        return R.layout.act_topiclist_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        if (getIntent().getExtras()!=null){
            id=getIntent().getExtras().getString("id");
            name=getIntent().getExtras().getString("name");
        }
        tool_back_iv.setVisibility(View.VISIBLE);
        tool_title_tv.setVisibility(View.VISIBLE);
        tool_title_tv.setText(name);
        tool_right_iv.setImageResource(R.mipmap.camera_icon);
        tool_right_iv.setVisibility(View.VISIBLE);

        loginDao=new LoginDao(this);
        maps= XLNetHttps.getBaseMap(this);

        initC();
        getBlogList();
    }

    private void initC(){
        manager=new LinearLayoutManager(this);
        XRvUtils.initRv(htrv,manager,LinearLayoutManager.VERTICAL,true,true,true);

        microBlogAdapter=new MicroBlogAdapter(this,"");
        htrv.setAdapter(microBlogAdapter);
        htrv.setLoadingListener(new XRecyclerView.LoadingListener() {
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
            SlideFunPop slideFunPop=new SlideFunPop(HotTopicListActivity.this);
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
                        IntentUtil.toActivity(HotTopicListActivity.this,bundle, ReportActivity.class);
                    }
                }
            });
        });
    }

    private void getBlogList(){
        maps.put("microblogType",String.valueOf(CodeConfig.BLOG_TOPIC));
        maps.put("lastId",String.valueOf(lastId));
        maps.put("size","10");
        maps.put("topicId",id);
        XLNetHttps.request(ApiConfig.API_BLOG_LIST, maps, MicroblogVO.class, new XLCallBack() {
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
                    htrv.refreshComplete();
                    isRef=false;
                }

                if (isLoad){
                    htrv.loadMoreComplete();
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

    @Event({R.id.tool_back_iv,R.id.tool_right_iv})
    private void clicks(View view){
        switch (view.getId()){
            case R.id.tool_back_iv:
                finish();
                break;
            case R.id.tool_right_iv:
                if (noneMember()){
                    AlertUtils.alertToVerify(this,loginDao.getObject().getHandleStatus());
                }else {
                    Bundle bundle=new Bundle();
                    bundle.putString("topicName",name);
                    IntentUtil.toActivity(this,bundle, ReleaseMicroBlogAct.class);
                }
                break;
        }
    }

    //未验证用户
    private boolean noneMember(){
        int type= SPUtils.getInstance().getInt("phtype");
        if (type==-1 && loginDao.getObject()!=null && loginDao.getObject().getUserDO().getStatus()==0){
            return true;
        }
        return false;
    }


    @Override
    protected void DetoryViewAndThing() {
        XRvUtils.destroyRv(htrv);
    }
}
