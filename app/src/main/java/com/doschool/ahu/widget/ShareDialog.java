package com.doschool.ahu.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.adapter.RmShareAdapter;
import com.doschool.ahu.appui.main.ui.adapter.SzShareAdapter;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVoteOptionsDto;
import com.doschool.ahu.appui.main.ui.bean.ShareDO;
import com.doschool.ahu.configfile.AppConfig;
import com.doschool.ahu.utils.EncodingUtils;
import com.doschool.ahu.utils.ScreenCaptureTool;
import com.doschool.ahu.utils.StringUtil;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.widget.component.SuperText;
import com.jakewharton.rxbinding2.view.RxView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by X on 2019/1/23.
 */
public class ShareDialog extends BasePopupWindow {

    private Context context;

    private LinearLayout parent_ll_share;
    private ImageView share_ivhead;
    private ImageView share_erm;
    private TextView share_tvname;
    private TextView share_tvcon;
    private RecyclerView sk_rv;
    private RecyclerView rm_rv;
    private LinearLayout share_llrm;
    private TextView share_tvbtn;
    private NestedScrollView nest_scroll;

    private LinearLayoutManager layoutManager,rmLayout;
    private RmShareAdapter rmShareAdapter;
    private SzShareAdapter szShareAdapter;
    private List<MicroblogVoteOptionsDto> list=new ArrayList<>();
    private Bitmap bitmap;


    public ShareDialog(Context context) {
        super(context);
        this.context=context;
        initView();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.share_vote_layout);
    }

    private void initView(){
        nest_scroll=findViewById(R.id.nest_scroll);
        parent_ll_share=findViewById(R.id.parent_ll_share);
        share_ivhead=findViewById(R.id.share_ivhead);
        share_erm=findViewById(R.id.share_erm);
        share_tvname=findViewById(R.id.share_tvname);
        share_tvcon=findViewById(R.id.share_tvcon);
        sk_rv=findViewById(R.id.sk_rv);
        rm_rv=findViewById(R.id.rm_rv);
        share_llrm=findViewById(R.id.share_llrm);
        share_tvbtn=findViewById(R.id.share_tvbtn);
    }

    public ShareDialog setUpdata(ShareDO shareDO){
        setContent(shareDO);
        return this;
    }

    @SuppressLint("CheckResult")
    private void setContent(ShareDO shareDO){

        parent_ll_share.setOnClickListener(view -> dismiss());

        XLGlideLoader.loadCircleImage(share_ivhead,shareDO.getData().getUserVO().getHeadImage());
        share_tvname.setText(shareDO.getData().getUserVO().getNickName());
        //处理@用户  表情的标识
        SpannableString spannableString= StringUtil.stringToSpannableString(shareDO.getData().getMicroblogMainDO().getContent(), context, 17);
        share_tvcon.setText(spannableString);
        SuperText.txtlink(context,share_tvcon);
//        share_tvcon.setText(shareDO.getData().getMicroblogMainDO().getContent());

        layoutManager=new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        sk_rv.setLayoutManager(layoutManager);

        list=shareDO.getData().getMicroblogVoteOptionsDtoList();
        szShareAdapter=new SzShareAdapter(context);
        sk_rv.setAdapter(szShareAdapter);
        Collections.sort(list, (t0, t1) -> t1.getMicroblogVoteOptionsDO().getSelectNum()-t0.getMicroblogVoteOptionsDO().getSelectNum());
        szShareAdapter.setDatas(list);
        szShareAdapter.setOnItemDis(() -> dismiss());

        if (shareDO.getData().getHotCommentVOList()!=null && shareDO.getData().getHotCommentVOList().size()>0){
            share_llrm.setVisibility(View.VISIBLE);
            rmLayout=new LinearLayoutManager(context);
            rmLayout.setOrientation(LinearLayoutManager.VERTICAL);
            rm_rv.setLayoutManager(rmLayout);

            rmShareAdapter=new RmShareAdapter(context);
            rm_rv.setAdapter(rmShareAdapter);
            rmShareAdapter.setDatas(shareDO.getData().getHotCommentVOList());

            rmShareAdapter.setOnItemDis(() -> dismiss());
        }else {
            share_llrm.setVisibility(View.GONE);
        }

        //二维码
        bitmap=EncodingUtils.createQRCode(shareDO.getData().getUrl()+"?schoolid="+AppConfig.SCHOOL_ID+"&blogid="+shareDO.getData().getMicroblogMainDO().getId(),
                300,300,1,
                BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_launcher));
        share_erm.setImageBitmap(bitmap);

        //生成图片
        RxView.clicks(share_tvbtn)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> ScreenCaptureTool.saveBitmap((Activity) context,
                        ScreenCaptureTool.drawBg4Bitmap(context.getResources().getColor(R.color.shareview_bgcolor),
                                ScreenCaptureTool.getScrollViewBitmap(nest_scroll))
                ));
    }
}
