package com.doschool.ahu.appui.home.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidubce.services.bos.BosClient;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.doschool.ahu.BuildConfig;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.home.ui.adapter.PersonalAdapter;
import com.doschool.ahu.appui.home.widget.PersonalPop;
import com.doschool.ahu.appui.infors.chat.ui.ChatActivity;
import com.doschool.ahu.appui.infors.chat.ui.model.CustomMessage;
import com.doschool.ahu.appui.infors.chat.ui.model.CustomModel;
import com.doschool.ahu.appui.infors.chat.ui.model.Message;
import com.doschool.ahu.appui.infors.chat.ui.presenter.ChatPresenter;
import com.doschool.ahu.appui.infors.chat.ui.viewfeatures.ChatView;
import com.doschool.ahu.appui.main.event.UserComm;
import com.doschool.ahu.appui.main.event.XMessageEvent;
import com.doschool.ahu.appui.main.ui.activity.BrowseImageActivity;
import com.doschool.ahu.appui.main.ui.activity.PersonalDataActivity;
import com.doschool.ahu.appui.main.ui.activity.ReportActivity;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVO;
import com.doschool.ahu.appui.reglogin.bean.LoginVO;
import com.doschool.ahu.appui.writeblog.widget.CompantDialog;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.base.model.BaseModel;
import com.doschool.ahu.base.model.DoUrlModel;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.configfile.CodeConfig;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.factory.BosFactory;
import com.doschool.ahu.utils.AlertUtils;
import com.doschool.ahu.utils.EventUtils;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.RandomUtils;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.utils.XRvUtils;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jaeger.library.StatusBarUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.matisseutil.GifFilter;
import com.matisseutil.Glide4Engine;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.ext.message.TIMMessageDraft;
import com.tencent.imsdk.ext.message.TIMMessageLocator;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.doschool.ahu.appui.main.ui.activity.ChangePersonalActivity.CHANGE_CODE;
import static com.doschool.ahu.configfile.ApiConfig.API_ADDFOLLOW;
import static com.doschool.ahu.configfile.ApiConfig.API_BANGBANGTANG;
import static com.doschool.ahu.configfile.ApiConfig.API_REMARK_NAME;
import static com.doschool.ahu.configfile.CodeConfig.FOLLOWSTATE_FOLLOW;
import static com.doschool.ahu.configfile.CodeConfig.FOLLOWSTATE_FOLLOWED;
import static com.doschool.ahu.configfile.CodeConfig.FOLLOWSTATE_NULL;
import static com.doschool.ahu.configfile.CodeConfig.IM_BBT_INFO;
import static com.doschool.ahu.configfile.CodeConfig.IM_BBT_MSG;
import static com.doschool.ahu.configfile.CodeConfig.IM_BBT_URL;
import static com.doschool.ahu.configfile.CodeConfig.IM_SEND_MSG;
import static com.doschool.ahu.configfile.CodeConfig.REPORT_USER;
import static com.doschool.ahu.configfile.DoUrlConfig.ACTION_BANGBANG;
import static com.doschool.ahu.configfile.DoUrlConfig.ACTION_GUNAZHU;


/**
 * Created by X on 2018/9/11
 *
 * 个人详情
 */
public class PersionalActivity extends BaseActivity implements ChatView {

    public static final int BG_NUM=17;
    public static final int HD_NUM=18;
    private static final int OTHER_NUM=19;
    private static final int OTHER_BG=20;
    public static final int FUN_NUM=21;
    public static final int PER_CODE_HEAD=531;

    @ViewInject(R.id.person_xrv)
    private XRecyclerView person_xrv;

    @ViewInject(R.id.per_rl)
    private RelativeLayout per_rl;

    @ViewInject(R.id.per_back_iv)
    private ImageView per_back_iv;

    @ViewInject(R.id.per_title_tv)
    private TextView per_title_tv;

    @ViewInject(R.id.per_right_iv)
    private ImageView per_right_iv;


    @ViewInject(R.id.person_ll)
    private LinearLayout person_ll;

    @ViewInject(R.id.too_per_back_iv)
    private ImageView too_per_back_iv;

    @ViewInject(R.id.too_per_title_tv)
    private TextView too_per_title_tv;

    @ViewInject(R.id.too_per_right_iv)
    private ImageView too_per_right_iv;

    @ViewInject(R.id.pa_ll)
    private LinearLayout pa_ll;

    @ViewInject(R.id.pa_tv_add)
    private TextView pa_tv_add;

    @ViewInject(R.id.add_guzu_iv)
    private ImageView add_guzu_iv;

    @ViewInject(R.id.add_ll_zg)
    private LinearLayout add_ll_zg;

    @ViewInject(R.id.pa_iv_bbt)
    private ImageView pa_iv_bbt;

    @ViewInject(R.id.pa_tv_go)
    private TextView pa_tv_go;

    private LinearLayoutManager linearLayoutManager;

    private PersonalAdapter personalAdapter;

    private ImageView pt_ivbg,pt_ivhead;
    private TextView pt_name,pt_txsig,pt_txxq,pt_txnj,pt_txgz,tp_zanshu,tp_bbt_tv;
    private ImageView ptt_ivsex,ptt_ivorg;
    private int targetId;
    private ArrayMap<String,String> map=new ArrayMap<>();
    private ArrayMap<String,String> maps=new ArrayMap<>();
    private int lastId=0;
    private boolean isRefrsh=false;
    private boolean isLoad=false;

    private LoginDao loginDao;
    private BosClient client;
    private ArrayMap<String,String> bangMap=new ArrayMap<>();
    private ChatPresenter presenter;

    private ArrayMap<String,String> addMap=new ArrayMap<>();

    private ArrayMap<String,String> markMap=new ArrayMap<>();

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_persional;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        loginDao=new LoginDao(this);
        if (getIntent().getExtras()!=null && getIntent().getExtras().containsKey("userid")){
            targetId=getIntent().getExtras().getInt("userid");
        }
        map= XLNetHttps.getBaseMap(this);
        maps=XLNetHttps.getBaseMap(this);
        bangMap=XLNetHttps.getBaseMap(this);
        addMap=XLNetHttps.getBaseMap(this);
        markMap=XLNetHttps.getBaseMap(this);
        presenter = new ChatPresenter(this, String.valueOf(targetId), TIMConversationType.C2C);
        client= BosFactory.getClient();
        if (loginDao.getObject()!=null){
            if (loginDao.getObject().getUserDO()!=null){
                if (targetId==loginDao.getObject().getUserDO().getId()){
                    pa_ll.setVisibility(View.GONE);
                }else {
                    pa_ll.setVisibility(View.VISIBLE);
                }
            }else {
                pa_ll.setVisibility(View.GONE);
            }
        }else {
            pa_ll.setVisibility(View.GONE);
        }
        initXR();
        addView();
        onScol();
        initData();
        initTop();
        EventUtils.register(this);
    }

    private void initXR(){
        linearLayoutManager=new LinearLayoutManager(this);
        XRvUtils.initRv(person_xrv,linearLayoutManager,LinearLayoutManager.VERTICAL,true,true,true);
        personalAdapter=new PersonalAdapter(this);
        person_xrv.setAdapter(personalAdapter);

        person_xrv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRefrsh=true;
                lastId=0;
                initData();
                initTop();
            }

            @Override
            public void onLoadMore() {
                isLoad=true;
                initData();
            }
        });
    }

    private void onScol(){
        person_xrv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager= (LinearLayoutManager) person_xrv.getLayoutManager();
                //获取第一个可见视图的position  不会获取头部的postion
                int pos=manager.findFirstVisibleItemPosition();
                //获取第一个完全可见视图的position
                int firstCompletelyVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();

                if (pos==2){
                    person_ll.setVisibility(View.VISIBLE);
                    StatusBarUtil.setColor(PersionalActivity.this,getResources().getColor(R.color.white),0);
                    StatusBarUtil.setLightMode(PersionalActivity.this);
                }

                if (firstCompletelyVisibleItemPosition==0){
                    person_ll.setVisibility(View.GONE);
                    StatusBarUtil.setTranslucentForImageView(PersionalActivity.this,0,null);
                    StatusBarUtil.setLightMode(PersionalActivity.this);
                }
            }
        });
    }

    private void initData(){
        maps.put("microblogType",String.valueOf(CodeConfig.BLOG_SOMEONE));
        maps.put("lastId",String.valueOf(lastId));
        maps.put("size","15");
        maps.put("targetUserId",String.valueOf(targetId));
        XLNetHttps.request(ApiConfig.API_BLOG_LIST, maps, MicroblogVO.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                MicroblogVO microblogVO=XLGson.fromJosn(result,MicroblogVO.class);
                if (microblogVO.getCode()==0){
                    if (lastId==0){
                        personalAdapter.clearAdapter();
                    }
                    if (microblogVO.getData()!=null && microblogVO.getData().size()>0){
                        personalAdapter.addDatas(microblogVO.getData());
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
                    person_xrv.refreshComplete();
                    isRefrsh=false;
                }

                if (isLoad){
                    person_xrv.refreshComplete();
                    isLoad=false;
                }
            }
        });
    }

    private void addView(){
        View view= LayoutInflater.from(this).inflate(R.layout.person_top_lay,null);
        pt_ivbg=view.findViewById(R.id.pt_ivbg);
        pt_ivhead=view.findViewById(R.id.pt_ivhead);
        pt_name=view.findViewById(R.id.pt_name);
        pt_txsig=view.findViewById(R.id.pt_txsig);
        pt_txxq=view.findViewById(R.id.pt_txxq);
        pt_txnj=view.findViewById(R.id.pt_txnj);
        pt_txgz=view.findViewById(R.id.pt_txgz);
        ptt_ivsex=view.findViewById(R.id.ptt_ivsex);
        ptt_ivorg=view.findViewById(R.id.ptt_ivorg);
        tp_zanshu=view.findViewById(R.id.tp_zanshu);
        tp_bbt_tv=view.findViewById(R.id.tp_bbt_tv);
        person_xrv.addHeaderView(view);
    }

    private void initTop(){
        map.put("objId",String.valueOf(targetId));
        XLNetHttps.request(ApiConfig.API_MINE, map, LoginVO.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                LoginVO loginVO= XLGson.fromJosn(result,LoginVO.class);
                if (loginVO.getCode()==0){
                    getDataTop(loginVO.getData());
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

    private List<String> bgList=new ArrayList<>();
    private List<String> hdList=new ArrayList<>();
    private List<String> hdThumList=new ArrayList<>();
    private String nickName;
    private int state=-1;
    private int personalId;
    private LoginVO.LoginData personalDta=new LoginVO.LoginData();
    private void getDataTop(LoginVO.LoginData loginData){
        personalDta=loginData;
        personalId=loginData.getUserDO().getId();
        //备注判断
        if (!TextUtils.isEmpty(loginData.getRemarkName())){
            nickName=loginData.getRemarkName();
            too_per_title_tv.setText(loginData.getRemarkName());
            pt_name.setText(loginData.getRemarkName());
        }else {
            nickName=loginData.getUserDO().getNickName();
            too_per_title_tv.setText(loginData.getUserDO().getNickName());
            pt_name.setText(loginData.getUserDO().getNickName());
        }
        if (!TextUtils.isEmpty(loginData.getUserDO().getBackgroundImage())){
            XLGlideLoader.loadImageByUrl(pt_ivbg,loginData.getUserDO().getBackgroundImage());
            bgList.add(loginData.getUserDO().getBackgroundImage());
        }
        hdList.add(loginData.getUserDO().getHeadImage());
        hdThumList.add(loginData.getHeadThumbnail());
        XLGlideLoader.loadCircleImage(pt_ivhead,loginData.getUserDO().getHeadImage());
        tp_zanshu.setText(String.valueOf(loginData.getUgcContentLikeCount()));
        tp_bbt_tv.setText(String.valueOf(loginData.getLollipopCount()));
        if (!TextUtils.isEmpty(loginData.getUserDO().getSelfIntro())){
            pt_txsig.setText("个性签名："+loginData.getUserDO().getSelfIntro());
        }else {
            pt_txsig.setText("个性签名：博主有点懒~~~，还没设置签名");
        }

        if (!TextUtils.isEmpty(loginData.getUserDO().getHobby())){
            pt_txxq.setText("兴趣爱好："+loginData.getUserDO().getHobby());
        }else {
            pt_txxq.setText("兴趣爱好：博主很宅~~~，毫无兴趣爱好");
        }

        if (loginData.getUserDO().getUserType()!=2 && loginData.getUserDO().getUserType()!=3){
            if (!TextUtils.isEmpty(loginData.getUserDO().getEnrYear())){
                pt_txnj.setText(loginData.getUserDO().getEnrYear()+"级\t\t"+loginData.getDepartName()+"\t"+loginData.getMajorName());
            }else {
                pt_txnj.setText(loginData.getDepartName()+"\t"+loginData.getMajorName());
            }
        }

        if (loginData.getFollowersCount()>0){
            pt_txgz.setText(loginData.getFollowersCount()+"人关注");
        }

        if (loginData.getUserDO().getUserType()==2 || loginData.getUserDO().getUserType()==3){
            UserComm.updateIdentify(ptt_ivorg,loginData.getUserDO());
        } else {
            UserComm.updateIdentify(ptt_ivsex,loginData.getUserDO());
        }

        //背景图更换--or--查看大图
        pt_ivbg.setOnClickListener(v->{
            if (loginData.getUserDO().getId()==loginDao.getObject().getUserDO().getId()){
                initPop(BG_NUM);
            }else {
                if (bgList.size()>0){
                    Bundle bundle=new Bundle();
                    bundle.putInt("index",0);
                    bundle.putSerializable("images", (Serializable) bgList);
                    bundle.putSerializable("thumbs", (Serializable) bgList);
                    IntentUtil.toActivity(this,bundle, BrowseImageActivity.class);
                }else {
                    XLToast.showToast("博主偷懒啦！没设置封面！");
                }
            }
        });

        //头像更换--or--查看大图
        pt_ivhead.setOnClickListener(v->{
            if (loginData.getUserDO().getId()==loginDao.getObject().getUserDO().getId()){
                initPop(HD_NUM);
            }else {
                Bundle bundle=new Bundle();
                bundle.putInt("index",0);
                bundle.putSerializable("images", (Serializable) hdList);
                bundle.putSerializable("thumbs", (Serializable) hdThumList);
                IntentUtil.toActivity(this,bundle, BrowseImageActivity.class);
            }
        });
        //关注  聊一聊  棒棒糖
        state=loginData.getFollowState();
        if (state==FOLLOWSTATE_NULL || state==FOLLOWSTATE_FOLLOWED ){
            pa_tv_add.setText("加关注");
//            add_ll_zg.setBackgroundResource(R.drawable.bg_login_btn);
            XLGlideLoader.loadImageById(add_guzu_iv,R.mipmap.add_guzu_icon);
        }else if (state==FOLLOWSTATE_FOLLOW){
            pa_tv_add.setText("已关注");
//            add_ll_zg.setBackgroundResource(R.drawable.bg_follow);
            XLGlideLoader.loadImageById(add_guzu_iv,R.mipmap.has_guzu_icon);
        }else {
            pa_tv_add.setText("相互关注");
//            add_ll_zg.setBackgroundResource(R.drawable.bg_follow);
            XLGlideLoader.loadImageById(add_guzu_iv,R.mipmap.has_guzu_icon);
        }
    }


    private void initPop(int code){
        PersonalPop personalPop=new PersonalPop(this);
        personalPop.onPalData(targetId,code).showPopupWindow();
        personalPop.setOnPersonalListener(new PersonalPop.OnPersonalListener() {
            @Override
            public void onRemark() {
                showReamrk();
            }

            @Override
            public void onChange() {
                if (code==FUN_NUM){//举报
                    Bundle bundle=new Bundle();
                    bundle.putInt("type",REPORT_USER);
                    bundle.putInt("Id",targetId);
                    IntentUtil.toActivity(PersionalActivity.this,bundle, ReportActivity.class);
                }else {
                    getPerssions(code);
                }
            }
        });
    }

    private CompantDialog compantDialog;
    private void showReamrk(){
        compantDialog=new CompantDialog(this, (dialog, content) -> {
            changeRemark(content);
            dialog.dismiss();
        }).setTitles("设置备注").setTxtLength(8).showCompant();
    }

    private void changeRemark(String markName){
        markMap.put("objId",String.valueOf(targetId));
        markMap.put("remarkName",markName);
        XLNetHttps.request(API_REMARK_NAME, markMap, BaseModel.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                if (!TextUtils.isEmpty(markName)){
                    pt_name.setText(markName);
                }
                initTop();
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

    //获取权限
    @SuppressLint("CheckResult")
    private void getPerssions(int code){
        RxPermissions permissions=new RxPermissions(this);
        permissions.requestEachCombined(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted){//同意后调用
                        exBg(code);
                    }else if (permission.shouldShowRequestPermissionRationale){//禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示
                    }else {//禁止，但选择“以后不再询问”，以后申请权限，不会继续弹出提示
                        if (!permissions.isGranted(Manifest.permission.CAMERA)){
                            XLToast.showToast("您的相机权限未打开！");
                        }
                        if (!permissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                            XLToast.showToast("您的存储权限未打开！");
                        }
                        if (!permissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)){
                            XLToast.showToast("您的存储权限未打开！");
                        }
                    }
                });
    }

    //自己的头像操作
    private void exBg(int requestCode){
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .showSingleMediaType(true)
                .countable(true)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, BuildConfig.APPLICATION_ID + ".fileprovider"))
                .maxSelectable(1)
                .addFilter(new GifFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .forResult(requestCode);
    }

    @Event({R.id.per_back_iv,R.id.per_right_iv,R.id.too_per_back_iv,R.id.too_per_right_iv,R.id.pa_tv_go,R.id.pa_tv_add,R.id.pa_iv_bbt,R.id.add_ll_zg,R.id.add_ly_il})
    private void click(View view){
        switch (view.getId()){
            case R.id.per_back_iv:
            case R.id.too_per_back_iv:
                finish();
                break;
            case R.id.per_right_iv://功能键
            case R.id.too_per_right_iv:
                if (personalId==loginDao.getObject().getUserDO().getId()){//自己的
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("gtag",personalDta);
                    IntentUtil.toActivity(this,bundle, PersonalDataActivity.class);
                }else {
                    if (noneMember()){
                        AlertUtils.alertToVerify(this,loginDao.getObject().getHandleStatus());
                    }else {
                        initPop(FUN_NUM);
                    }
                }
                break;
            case R.id.add_ly_il:
            case R.id.pa_tv_go://聊一聊
                if (noneMember()){
                    AlertUtils.alertToVerify(this,loginDao.getObject().getHandleStatus());
                }else {
                    if (!TextUtils.isEmpty(nickName)){
                        ChatActivity.navToChat(this,String.valueOf(targetId),nickName, TIMConversationType.C2C);
                    }else {
                        XLToast.showToast("请稍后，服务器正加速奔跑中！");
                    }
                }
                break;
            case R.id.add_ll_zg:
            case R.id.pa_tv_add://关注
                if (noneMember()){
                    AlertUtils.alertToVerify(this,loginDao.getObject().getHandleStatus());
                }else {
                    if (state!=-1){
                        if (state==FOLLOWSTATE_NULL || state==FOLLOWSTATE_FOLLOWED ){//进行关注
                            addFollow(1);
                        }else {//取消
                            addFollow(0);
                        }
                    }
                }
                break;
            case R.id.pa_iv_bbt://棒棒糖
                if (noneMember()){
                    AlertUtils.alertToVerify(this,loginDao.getObject().getHandleStatus());
                }else {
                    new AlertDialog.Builder(this).setMessage("确定要送ta一个棒棒糖吗")
                            .setPositiveButton("是的", (dialog, id) -> sengBangT())
                            .setNegativeButton("不送", (dialog, id) -> dialog.cancel()).create().show();
                }
                break;
        }
    }

    private void addFollow(int type){
        addMap.put("objId",String.valueOf(targetId));
        addMap.put("type",String.valueOf(type));
        XLNetHttps.request(API_ADDFOLLOW, addMap, BaseModel.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                BaseModel baseModel=XLGson.fromJosn(result,BaseModel.class);
                if (baseModel.getCode()==0){
                    initTop();
                    if (type==1){
                        XLToast.showToast("关注成功！");
                        sendMSG();
                    }else {
                        XLToast.showToast("取消关注成功！");
                    }
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

    private void sendMSG(){
        CustomModel customModel=new CustomModel();
        DoUrlModel doUrlModel=new DoUrlModel();
        doUrlModel.setAction(ACTION_GUNAZHU);
        customModel.setDoUrl(doUrlModel);
        customModel.setExtType(2);
        customModel.setImageUrl("");
        customModel.setTitle(IM_SEND_MSG);
        customModel.setVersion(String.valueOf(AppUtils.getAppVersionName()));

        Message message = new CustomMessage(CustomMessage.Type.BANGBANGTANG,XLGson.toJson(customModel));
        message.setDesc(IM_BBT_INFO);
        presenter.sendMessage(message.getMessage());
    }

    private void sengBangT(){
        bangMap.put("objId",String.valueOf(targetId));
        XLNetHttps.request(API_BANGBANGTANG, bangMap, BaseModel.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                BaseModel baseModel= XLGson.fromJosn(result,BaseModel.class);
                if (baseModel.getCode()==0){
                    XLToast.showToast(baseModel.getMessage());
                    CustomModel customModel=new CustomModel();
                    DoUrlModel doUrlModel=new DoUrlModel();
                    doUrlModel.setAction(ACTION_BANGBANG);
                    customModel.setDoUrl(doUrlModel);
                    customModel.setExtType(1);
                    customModel.setImageUrl(IM_BBT_URL);
                    customModel.setTitle(IM_BBT_MSG);
                    customModel.setVersion(String.valueOf(AppUtils.getAppVersionName()));

                    Message message = new CustomMessage(CustomMessage.Type.BANGBANGTANG,XLGson.toJson(customModel));
                    message.setDesc(IM_BBT_INFO);
                    presenter.sendMessage(message.getMessage());
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

    @Override
    protected void DetoryViewAndThing() {
        XRvUtils.destroyRv(person_xrv);
        EventUtils.unRegister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ChaEvent(XMessageEvent event){
        if (event.getCode()== CHANGE_CODE){
            initTop();
        }
    }

    //未验证用户
    private boolean noneMember(){
        int type= SPUtils.getInstance().getInt("phtype");
        if (type==-1 && loginDao.getObject()!=null &&  loginDao.getObject().getUserDO().getStatus()==0){
            return true;
        }
        return false;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this,0,null);
        StatusBarUtil.setLightMode(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case BG_NUM:
                    for (int i=0;i<Matisse.obtainPathResult(data).size();i++){
                        String path=Matisse.obtainPathResult(data).get(i);
                        final File files=new File(path);//上传文件的目录
                        final String imgRand= RandomUtils.getRandName(targetId,5)+".jpg";
                        new Thread(() -> BosFactory.getRequest(client, CodeConfig.BOS_BG + imgRand, files, (request, currentSize, totalSize) -> {
                            if (currentSize==totalSize){
                                perLoad(BG_NUM,imgRand,path);
                            }
                        })).start();
                    }
                    break;
                case HD_NUM:
                    for (int i=0;i<Matisse.obtainPathResult(data).size();i++){
                        String path=Matisse.obtainPathResult(data).get(i);
                        final File files=new File(path);//上传文件的目录
                        final String imgRand= RandomUtils.getRandName(targetId,5)+".jpg";
                        new Thread(() -> BosFactory.getRequest(client, CodeConfig.BOS_HEAD + imgRand, files, (request, currentSize, totalSize) -> {
                            if (currentSize==totalSize){
                                perLoad(HD_NUM,imgRand,path);
                            }
                        })).start();
                    }
                    break;
            }
        }
    }

    private void perLoad(int code,String file,String path){
        ArrayMap<String,String> arrayMap=XLNetHttps.getBaseMap(this);
        arrayMap.put("id",String.valueOf(targetId));
        if (code==BG_NUM){
            arrayMap.put("backgroundImage",file);
        }else {
            arrayMap.put("headImage",file);
        }
        XLNetHttps.request(ApiConfig.API_UPDATE_USER, arrayMap, LoginVO.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                LoginVO loginV= XLGson.fromJosn(result,LoginVO.class);
                if (loginV.getCode()==0){
                    if (code==BG_NUM){
                        XLGlideLoader.loadImageByUrl(pt_ivbg,loginV.getData().getUserDO().getBackgroundImage());
                        XLGlideLoader.loadImageByUrl(pt_ivbg,path);
                    }else {
                        XLGlideLoader.loadCircleImage(pt_ivhead,loginV.getData().getUserDO().getHeadImage());
                        XLGlideLoader.loadCircleImage(pt_ivhead,path);
                    }
                    EventUtils.onPost(new XMessageEvent(PER_CODE_HEAD));
                    loginDao.clearUserTable();
                    loginDao.saveObject(loginV.getData());
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

    @Override
    public void showMessage(TIMMessage message) {

    }

    @Override
    public void showMessage(List<TIMMessage> messages) {

    }

    @Override
    public void showRevokeMessage(TIMMessageLocator timMessageLocator) {

    }

    @Override
    public void clearAllMessage() {

    }

    @Override
    public void onSendMessageSuccess(TIMMessage message) {

    }

    @Override
    public void onSendMessageFail(int code, String desc, TIMMessage message) {

    }

    @Override
    public void sendImage() {

    }

    @Override
    public void sendPhoto() {

    }

    @Override
    public void sendText() {

    }

    @Override
    public void sendBbtText() {

    }

    @Override
    public void sendFile() {

    }

    @Override
    public void startSendVoice() {

    }

    @Override
    public void endSendVoice() {

    }

    @Override
    public void sendVideo(String fileName) {

    }

    @Override
    public void cancelSendVoice() {

    }

    @Override
    public void cancleMoveVoice() {

    }

    @Override
    public void sending() {

    }

    @Override
    public void showDraft(TIMMessageDraft draft) {

    }

    @Override
    public void videoAction() {

    }

    @Override
    public void showToast(String msg) {

    }
}
