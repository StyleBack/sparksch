package com.doschool.ahu.appui.main.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.adapter.RmShareAdapter;
import com.doschool.ahu.appui.main.ui.adapter.SzShareAdapter;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVoteOptionsDto;
import com.doschool.ahu.appui.main.ui.bean.ShareDO;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.utils.XLGlideLoader;
import com.jaeger.library.StatusBarUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by X on 2019/1/22.
 */
public class ShareViewActivity extends BaseActivity {


    @ViewInject(R.id.parent_ll_share)
    private LinearLayout parent_ll_share;

    @ViewInject(R.id.share_ivhead)
    private ImageView share_ivhead;

    @ViewInject(R.id.share_erm)
    private ImageView share_erm;

    @ViewInject(R.id.share_tvname)
    private TextView share_tvname;

    @ViewInject(R.id.share_tvcon)
    private TextView share_tvcon;

    @ViewInject(R.id.sk_rv)
    private RecyclerView sk_rv;

    @ViewInject(R.id.rm_rv)
    private RecyclerView rm_rv;

    @ViewInject(R.id.share_llrm)
    private LinearLayout share_llrm;

    @ViewInject(R.id.share_tvbtn)
    private TextView share_tvbtn;

    private ShareDO shareDO;
    private LinearLayoutManager layoutManager,rmLayout;
    private RmShareAdapter rmShareAdapter;
    private SzShareAdapter szShareAdapter;
    private List<MicroblogVoteOptionsDto> list=new ArrayList<>();

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this,0,null);
        StatusBarUtil.setDarkMode(this);
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.share_vote_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        shareDO= (ShareDO) getIntent().getExtras().getSerializable("voteShare");
        init();
    }

    private void init(){

        XLGlideLoader.loadCircleImage(share_ivhead,shareDO.getData().getUserVO().getHeadImage());
        share_tvname.setText(shareDO.getData().getUserVO().getNickName());
        share_tvcon.setText(shareDO.getData().getMicroblogMainDO().getContent());

        layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        sk_rv.setLayoutManager(layoutManager);

        list=shareDO.getData().getMicroblogVoteOptionsDtoList();
        szShareAdapter=new SzShareAdapter(this);
        sk_rv.setAdapter(szShareAdapter);
        Collections.sort(list, (t0, t1) -> t1.getMicroblogVoteOptionsDO().getSelectNum()-t0.getMicroblogVoteOptionsDO().getSelectNum());
        szShareAdapter.setDatas(list);

        if (shareDO.getData().getHotCommentVOList()!=null && shareDO.getData().getHotCommentVOList().size()>0){
            share_llrm.setVisibility(View.VISIBLE);
            rmLayout=new LinearLayoutManager(this);
            rmLayout.setOrientation(LinearLayoutManager.VERTICAL);
            rm_rv.setLayoutManager(rmLayout);

            rmShareAdapter=new RmShareAdapter(this);
            rm_rv.setAdapter(rmShareAdapter);
            rmShareAdapter.setDatas(shareDO.getData().getHotCommentVOList());
        }else {
            share_llrm.setVisibility(View.GONE);
        }

    }

    @Event({R.id.parent_ll_share})
    private void clicks(View view){
        switch (view.getId()){
            case R.id.parent_ll_share:
                finish();
                break;
        }
    }

    @Override
    protected void DetoryViewAndThing() {

    }
}
