package com.doschool.ahu.appui.main.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.adapter.SearchWrapAdapter;
import com.doschool.ahu.appui.main.ui.bean.SearchWrap;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.utils.XRvUtils;
import com.doschool.ahu.widget.RvDivider;
import com.doschool.ahu.widget.WrapEditText;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by X on 2018/9/27
 *
 * 一级搜索界面
 */
public class SearchActivity extends BaseActivity {

    @ViewInject(R.id.bar_wrap_ex)
    private WrapEditText bar_wrap_ex;

    @ViewInject(R.id.bar_rv)
    private XRecyclerView bar_rv;

    private LinearLayoutManager manager;

    private List<SearchWrap> wrapList=new ArrayList<>();
    private SearchWrapAdapter searchWrapAdapter;

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_search_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        initRv();

        initDta();
    }

    private void initRv(){
        bar_rv.addItemDecoration(new RvDivider(this,LinearLayoutManager.VERTICAL,R.drawable.divider_hight));
        manager=new LinearLayoutManager(this);
        XRvUtils.initRv(bar_rv,manager,LinearLayoutManager.VERTICAL,false,false,false);
        searchWrapAdapter=new SearchWrapAdapter(this);
        bar_rv.setAdapter(searchWrapAdapter);
    }

    @SuppressLint("CheckResult")
    private void initDta(){
        RxTextView.textChanges(bar_wrap_ex)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(CharSequence::toString)
                .subscribe(s->{
                    if (!TextUtils.isEmpty(s)){
                        wrapList.clear();
                        SearchWrap user=new SearchWrap();
                        user.setSearchType(1);
                        user.setKeyword(s);
                        wrapList.add(user);
                        SearchWrap blog=new SearchWrap();
                        blog.setSearchType(2);
                        blog.setKeyword(s);
                        wrapList.add(blog);
                        SearchWrap topic=new SearchWrap();
                        topic.setSearchType(3);
                        topic.setKeyword(s);
                        wrapList.add(topic);
                        searchWrapAdapter.setDatas(wrapList);
                    }else {
                        searchWrapAdapter.getList().clear();
                        searchWrapAdapter.notifyDataSetChanged();
                    }
                });
    }


    @Event({R.id.search_bar_tx})
    private void clicks(View view){
        switch (view.getId()){
            case R.id.search_bar_tx:
                finish();
                break;
        }
    }

    @Override
    protected void DetoryViewAndThing() {
        XRvUtils.destroyRv(bar_rv);
    }
}
