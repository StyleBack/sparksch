package com.doschool.ahu.appui.writeblog.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.writeblog.ui.adapter.AtUserAdapter;
import com.doschool.ahu.appui.writeblog.ui.adapter.AtUserAdapter2;
import com.doschool.ahu.appui.writeblog.ui.bean.AtBean;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.configfile.CodeConfig;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XRvUtils;
import com.doschool.ahu.widget.RvDivider;
import com.doschool.ahu.widget.WrapEditText;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by X on 2018/11/5
 *
 * at用户界面
 */
public class AtUserActivity extends BaseActivity {

    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;

    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.atuser_ex)
    private WrapEditText atuser_ex;

    @ViewInject(R.id.atuser_tv)
    private TextView atuser_tv;

    @ViewInject(R.id.at_rview)
    private XRecyclerView at_rview;
    private LinearLayoutManager linearLayoutManager;
    private boolean isRef=false;
    private boolean isLoad=false;
    private int lastId=0;
    private AtUserAdapter atUserAdapter;
    private ArrayMap<String,String> map=new ArrayMap<>();

    @ViewInject(R.id.hint_rv)
    private XRecyclerView hint_rv;
    private boolean isRef2=false;
    private boolean isLoad2=false;
    private int lastId2=0;
    private AtUserAdapter2 adapter2;
    private ArrayMap<String,String> hintMap=new ArrayMap<>();
    private String keyWords;

    public static Intent creatIntent(Context context){
        Intent intent=new Intent(context,AtUserActivity.class);
        return intent;
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_atuser_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        tool_back_iv.setVisibility(VISIBLE);
        tool_title_tv.setVisibility(VISIBLE);
        tool_title_tv.setText("选择联系人");
        map= XLNetHttps.getBaseMap(this);
        hintMap=XLNetHttps.getBaseMap(this);
        initRV();
        initData();
        getWrap();
    }

    private void initRV(){
        at_rview.addItemDecoration(new RvDivider(this,LinearLayoutManager.VERTICAL,R.drawable.divider_hight));
        linearLayoutManager=new LinearLayoutManager(this);
        XRvUtils.initRv(at_rview,linearLayoutManager,LinearLayoutManager.VERTICAL,true,true,true);
        atUserAdapter=new AtUserAdapter(this);
        at_rview.setAdapter(atUserAdapter);
        at_rview.setLoadingListener(new XRecyclerView.LoadingListener() {
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

        //选择用户返回
        atUserAdapter.setOnAtListener((userid, atUser) -> {

            if (KeyboardUtils.isSoftInputVisible(this)){
                KeyboardUtils.hideSoftInput(this);
            }

            Intent intent = new Intent();
            intent.putExtra("userId",userid);
            intent.putExtra("atUser", atUser);
            setResult(RESULT_OK, intent);
            finish();
        });

        //hint隐藏rv哟
        hint_rv.addItemDecoration(new RvDivider(this,LinearLayoutManager.VERTICAL,R.drawable.divider_hight));
        linearLayoutManager=new LinearLayoutManager(this);
        XRvUtils.initRv(hint_rv,linearLayoutManager,LinearLayoutManager.VERTICAL,true,true,true);
        adapter2=new AtUserAdapter2(this);
        hint_rv.setAdapter(adapter2);
        hint_rv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRef2=true;
                lastId2=0;
                hintData();
            }

            @Override
            public void onLoadMore() {
                isLoad2=true;
                hintData();
            }
        });
        adapter2.setOnAtListener((userid, atUser) -> {

            if (KeyboardUtils.isSoftInputVisible(this)){
                KeyboardUtils.hideSoftInput(this);
            }

            Intent intent = new Intent();
            intent.putExtra("userId",userid);
            intent.putExtra("atUser", atUser);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private void initData(){
        map.put("followType", String.valueOf(CodeConfig.FOLLOW_GZ));
        map.put("lastId",String.valueOf(lastId));
        map.put("size","20");
        XLNetHttps.request(ApiConfig.API_FOLLWERS, map,  AtBean.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                AtBean atBean= XLGson.fromJosn(result,AtBean.class);
                if (atBean.getCode()==0){
                    if (lastId==0){
                        atUserAdapter.getList().clear();
                    }
                    if (atBean.getData()!=null && atBean.getData().size()>0){
                        atUserAdapter.addDatas(atBean.getData());
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
                    at_rview.refreshComplete();
                    isRef=false;
                }

                if (isLoad){
                    at_rview.loadMoreComplete();
                    isLoad=false;
                }
            }
        });
    }

    @SuppressLint("CheckResult")
    private void getWrap(){
        RxTextView.textChanges(atuser_ex)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(CharSequence::toString)
                .subscribe(s -> {
                    if (!TextUtils.isEmpty(s)){
                        at_rview.setVisibility(GONE);
                        hint_rv.setVisibility(VISIBLE);
                        keyWords=s;
                        hintData();
                    }else {
                        adapter2.getList().clear();
                        adapter2.notifyDataSetChanged();
                        hint_rv.setVisibility(GONE);
                        at_rview.setVisibility(VISIBLE);
                    }
                });

        //取消操作
        RxView.clicks(atuser_tv)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (!TextUtils.isEmpty(keyWords)){
                        atuser_ex.setText("");
                        adapter2.getList().clear();
                        adapter2.notifyDataSetChanged();
                        hint_rv.setVisibility(GONE);
                        at_rview.setVisibility(VISIBLE);
                    }
                });
    }

    private void hintData(){
        hintMap.put("searchType", String.valueOf(1));
        hintMap.put("keyword",keyWords);
        hintMap.put("lastId",String.valueOf(lastId2));
        hintMap.put("size","20");
        XLNetHttps.request(ApiConfig.API_SEARCH_RESULT, hintMap,  AtBean.class,  new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                AtBean atBean= XLGson.fromJosn(result,AtBean.class);
                if (atBean.getCode()==0){
                    if (lastId==0){
                        adapter2.getList().clear();
                    }
                    if (atBean.getData()!=null && atBean.getData().size()>0){
                        adapter2.addDatas(atBean.getData());
                    }
                    lastId2=atBean.getData().get(atBean.getData().size()-1).getUserId();
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
                if (isRef2){
                    hint_rv.refreshComplete();
                    isRef2=false;
                }

                if (isLoad2){
                    hint_rv.loadMoreComplete();
                    isLoad2=false;
                }
            }
        });
    }

    @Override
    protected void DetoryViewAndThing() {
        XRvUtils.destroyRv(at_rview);
        XRvUtils.destroyRv(hint_rv);
    }

    @Event({R.id.tool_back_iv})
    private void click(View view){
        switch (view.getId()){
            case R.id.tool_back_iv:
                if (KeyboardUtils.isSoftInputVisible(this)){
                    KeyboardUtils.hideSoftInput(this);
                }
                finish();
                break;
        }
    }
}
