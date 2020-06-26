package com.doschool.ahu.appui.home.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.home.ui.fragment.DiaryFragment;
import com.doschool.ahu.appui.home.ui.fragment.OtherInfoFragment;
import com.doschool.ahu.appui.home.widget.SlideRemarkPop;
import com.doschool.ahu.appui.infors.chat.ui.ChatActivity;
import com.doschool.ahu.appui.infors.chat.ui.model.CustomMessage;
import com.doschool.ahu.appui.infors.chat.ui.model.CustomModel;
import com.doschool.ahu.appui.infors.chat.ui.model.Message;
import com.doschool.ahu.appui.infors.chat.ui.presenter.ChatPresenter;
import com.doschool.ahu.appui.infors.chat.ui.viewfeatures.ChatView;
import com.doschool.ahu.appui.main.event.UserComm;
import com.doschool.ahu.appui.main.event.XMessageEvent;
import com.doschool.ahu.appui.main.ui.activity.BrowseImageActivity;
import com.doschool.ahu.appui.main.ui.activity.ReportActivity;
import com.doschool.ahu.appui.reglogin.bean.LoginVO;
import com.doschool.ahu.appui.writeblog.widget.CompantDialog;
import com.doschool.ahu.base.model.BaseModel;
import com.doschool.ahu.base.model.DoUrlModel;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.factory.AppDoUrlFactory;
import com.doschool.ahu.utils.AlertUtils;
import com.doschool.ahu.utils.EventUtils;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.widget.xtablay.IndicatorAdapter;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.base.BaseFragment;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.widget.xtablay.TabFragmentAdapter;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jaeger.library.StatusBarUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.ext.message.TIMMessageDraft;
import com.tencent.imsdk.ext.message.TIMMessageLocator;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


import static com.doschool.ahu.appui.home.ui.fragment.OtherInfoFragment.OTH_CODE;
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
import static com.doschool.ahu.utils.ChangeAlphaUtils.changeAlpha;

/**
 * Created by X on 2019/1/8.
 *
 * 他人信息页
 */
public class OtherSingleActivity extends BaseActivity implements ChatView {

    private static final String[] CHANNELS = new String[]{"校园日记","资料"};
    private List<String> mDataList = Arrays.asList(CHANNELS);

    @ViewInject(R.id.single_back)
    private ImageView single_back;
    @ViewInject(R.id.single_title_tv)
    private TextView single_title_tv;
    @ViewInject(R.id.single_right)
    private ImageView single_right;

    @ViewInject(R.id.single_line)
    private View single_line;

    @ViewInject(R.id.single_appbar)
    private AppBarLayout single_appbar;
    @ViewInject(R.id.single_toolbar)
    private Toolbar single_toolbar;

    @ViewInject(R.id.single_vp)
    private ViewPager single_vp;

    @ViewInject(R.id.single_tab_magic)
    private MagicIndicator single_tab_magic;

    @ViewInject(R.id.single_zh_zero)
    private ImageView single_zh_zero;
    @ViewInject(R.id.single_tvz)
    private TextView single_tvz;

    @ViewInject(R.id.single_bbt_zero)
    private ImageView single_bbt_zero;
    @ViewInject(R.id.single_tvbt)
    private TextView single_tvbt;

    @ViewInject(R.id.single_bgiv)
    private ImageView single_bgiv;

    @ViewInject(R.id.single_head)
    private ImageView single_head;

    @ViewInject(R.id.single_sex)
    private ImageView single_sex;
    @ViewInject(R.id.single_org)
    private ImageView single_org;

    @ViewInject(R.id.single_fans)
    private TextView single_fans;

    @ViewInject(R.id.single_zgs)
    private TextView single_zgs;

    //加好友
    @ViewInject(R.id.single_gzli)
    private LinearLayout single_gzli;
    @ViewInject(R.id.single_gziv)
    private ImageView single_gziv;
    @ViewInject(R.id.single_gztv)
    private TextView single_gztv;
    //棒棒糖
    @ViewInject(R.id.single_bbt_icon)
    private ImageView single_bbt_icon;
    //聊一聊
    @ViewInject(R.id.single_msgll)
    private LinearLayout single_msgll;

    private TabFragmentAdapter adapter;
    private List<BaseFragment> fragments=new ArrayList<>();
    private CommonNavigator commonNavigator;


    private ArrayMap<String,String> userMap=new ArrayMap<>();
    private ArrayMap<String,String> markMap=new ArrayMap<>();
    private ArrayMap<String,String> addMap=new ArrayMap<>();
    private ArrayMap<String,String> bangMap=new ArrayMap<>();

    private int targetId;

    private List<String> hdList=new ArrayList<>();
    private List<String> hdThumList=new ArrayList<>();
    private List<String> bgList=new ArrayList<>();

    private LoginDao loginDao;
    private int state=-1;
    private String nickName;
    private ChatPresenter presenter;

    private AlertDialog bbtDialog;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this,0,null);
        StatusBarUtil.setLightMode(this);
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_other_single;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {

        loginDao=new LoginDao(this);
        if (getIntent().getExtras()!=null && getIntent().getExtras().containsKey("userid")){
            targetId=getIntent().getExtras().getInt("userid");
        }
        userMap=XLNetHttps.getBaseMap(this);
        markMap=XLNetHttps.getBaseMap(this);
        addMap=XLNetHttps.getBaseMap(this);
        bangMap=XLNetHttps.getBaseMap(this);
        presenter = new ChatPresenter(this, String.valueOf(targetId), TIMConversationType.C2C);
        initView();
        initUser();
        initTab();
    }

    private void initView(){

        single_title_tv.setVisibility(View.VISIBLE);
        single_appbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset<=-10){
                XLGlideLoader.loadImageById(single_back,R.mipmap.tool_back);
                XLGlideLoader.loadImageById(single_right,R.mipmap.icon_fun_black);
                single_title_tv.setTextColor(getResources().getColor(R.color.title_color));
            }else {
                XLGlideLoader.loadImageById(single_back,R.mipmap.back_white);
                XLGlideLoader.loadImageById(single_right,R.mipmap.icon_function_white);
                single_title_tv.setTextColor(getResources().getColor(R.color.white));
            }
            single_toolbar.setBackgroundColor(changeAlpha(getResources().getColor(R.color.white),Math.abs(verticalOffset*1.0f)/appBarLayout.getTotalScrollRange()));
            single_line.setBackgroundColor(changeAlpha(getResources().getColor(R.color.grey_link),Math.abs(verticalOffset*1.0f)/appBarLayout.getTotalScrollRange()));
        });
    }

    private void initUser(){
        userMap.put("objId",String.valueOf(targetId));
        XLNetHttps.request(ApiConfig.API_MINE, userMap, LoginVO.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                LoginVO loginVO= XLGson.fromJosn(result,LoginVO.class);
                if (loginVO.getCode()==0){
                    getUserInfo(loginVO.getData());
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

    private void initTab(){
        //tab
        fragments.add(DiaryFragment.newInstance(targetId));
        fragments.add(OtherInfoFragment.newInstance(targetId));
        adapter=new TabFragmentAdapter(getSupportFragmentManager(),fragments);
        single_vp.setAdapter(adapter);

        commonNavigator=new CommonNavigator(this);
        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new IndicatorAdapter(single_vp,mDataList)
                .setXTextSize(16)
                .setNorColor(getResources().getColor(R.color.title_color))
                .setSelectColor(getResources().getColor(R.color.now_txt_color))
                .setIndicMode(LinePagerIndicator.MODE_EXACTLY)
                .setIndicLineHeight(4)
                .setIndicLineWidth(20)
                .setIndicRadius(3)
                .setIndicColor(getResources().getColor(R.color.now_txt_color)));
        single_tab_magic.setNavigator(commonNavigator);
        ViewPagerHelper.bind(single_tab_magic, single_vp);
    }

    @SuppressLint("CheckResult")
    private void getUserInfo(LoginVO.LoginData data){
        //备注判断
        if (!TextUtils.isEmpty(data.getRemarkName())){
            nickName=data.getRemarkName();
            single_title_tv.setText(data.getRemarkName());
        }else {
            nickName=data.getUserDO().getNickName();
            single_title_tv.setText(data.getUserDO().getNickName());
        }
        //背景
        if (!TextUtils.isEmpty(data.getUserDO().getBackgroundImage())){
            XLGlideLoader.loadImageByUrl(single_bgiv,data.getUserDO().getBackgroundImage());
            bgList.add(data.getUserDO().getBackgroundImage());
        }
        //获赞
        if (data.getUgcContentLikeCount()>0){
            single_zh_zero.setVisibility(View.GONE);
            single_tvz.setText(String.valueOf(data.getUgcContentLikeCount()));
        }else {
            single_zh_zero.setVisibility(View.VISIBLE);
        }
        //棒棒糖
        if (data.getLollipopCount()>0){
            single_bbt_zero.setVisibility(View.GONE);
            single_tvbt.setText(String.valueOf(data.getLollipopCount()));
        }else {
            single_bbt_zero.setVisibility(View.VISIBLE);
        }
        //头像
        hdList.add(data.getUserDO().getHeadImage());
        hdThumList.add(data.getHeadThumbnail());
        XLGlideLoader.loadCircleImage(single_head,data.getUserDO().getHeadImage());
        //性别  组织标识
        if (data.getUserDO().getUserType()==2 || data.getUserDO().getUserType()==3){
            UserComm.updateIdentify(single_org,data.getUserDO());
        } else {
            UserComm.updateIdentify(single_sex,data.getUserDO());
        }
        //粉丝
        single_fans.setText(String.valueOf(data.getFollowersCount()));
        //关注
        single_zgs.setText(String.valueOf(data.getFollowingCount()));

        //查看头像
        RxView.clicks(single_head)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    Bundle bundle=new Bundle();
                    bundle.putInt("index",0);
                    bundle.putSerializable("images", (Serializable) hdList);
                    bundle.putSerializable("thumbs", (Serializable) hdThumList);
                    IntentUtil.toActivity(this,bundle, BrowseImageActivity.class);
                });
        //查看背景
        RxView.clicks(single_bgiv)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (bgList!=null && bgList.size()>0){
                        Bundle bundle=new Bundle();
                        bundle.putInt("index",0);
                        bundle.putSerializable("images", (Serializable) bgList);
                        bundle.putSerializable("thumbs", (Serializable) bgList);
                        IntentUtil.toActivity(this,bundle, BrowseImageActivity.class);
                    }
                });

        //关注
        state=data.getFollowState();
        if (state==FOLLOWSTATE_NULL || state==FOLLOWSTATE_FOLLOWED ){
            single_gztv.setText("加关注");
            XLGlideLoader.loadImageById(single_gziv,R.mipmap.add_guzu_icon);
        }else if (state==FOLLOWSTATE_FOLLOW){
            single_gztv.setText("已关注");
            XLGlideLoader.loadImageById(single_gziv,R.mipmap.has_guzu_icon);
        }else {
            single_gztv.setText("相互关注");
            XLGlideLoader.loadImageById(single_gziv,R.mipmap.has_guzu_icon);
        }
    }

    @Event({R.id.single_back,R.id.single_right,R.id.single_gzli,R.id.single_msgll,R.id.single_bbt_icon})
    private void clicks(View view){
        switch (view.getId()){
            case R.id.single_back:
            finish();
                break;
            case R.id.single_right://功能键
                if (AppDoUrlFactory.noneMember(this)){
                    AlertUtils.alertToVerify(this,loginDao.getObject().getHandleStatus());
                }else {
                    markPot();
                }
                break;
            case R.id.single_gzli://关注
                if (AppDoUrlFactory.noneMember(this)){
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
            case R.id.single_msgll://聊一聊
                if (AppDoUrlFactory.noneMember(this)){
                    AlertUtils.alertToVerify(this,loginDao.getObject().getHandleStatus());
                }else {
                    if (!TextUtils.isEmpty(nickName)){
                        ChatActivity.navToChat(this,String.valueOf(targetId),nickName, TIMConversationType.C2C);
                    }else {
                        XLToast.showToast("请稍后，服务器正加速奔跑中！");
                    }
                }
                break;
            case R.id.single_bbt_icon://棒棒糖
                if (AppDoUrlFactory.noneMember(this)){
                    AlertUtils.alertToVerify(this,loginDao.getObject().getHandleStatus());
                }else {
                   bbtShowDialog();
                }
                break;
        }
    }

    private void markPot(){
        SlideRemarkPop slideRemarkPop=new SlideRemarkPop(this);
        slideRemarkPop.showPopupWindow();
        slideRemarkPop.setOnMarkpotListener(new SlideRemarkPop.OnMarkpotListener() {
            @Override
            public void onRemark() {
                showReamrk();
            }

            @Override
            public void onReport() {
                Bundle bundle=new Bundle();
                bundle.putInt("type",REPORT_USER);
                bundle.putInt("Id",targetId);
                IntentUtil.toActivity(OtherSingleActivity.this,bundle, ReportActivity.class);
            }
        });
    }

    private void showReamrk(){
        new CompantDialog(this, (dialog, content) -> {
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
                    single_title_tv.setText(markName);
                }
                initUser();
                EventUtils.onPost(new XMessageEvent(OTH_CODE));
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

    private void addFollow(int type){
        addMap.put("objId",String.valueOf(targetId));
        addMap.put("type",String.valueOf(type));
        XLNetHttps.request(API_ADDFOLLOW, addMap, BaseModel.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                BaseModel baseModel=XLGson.fromJosn(result,BaseModel.class);
                if (baseModel.getCode()==0){
                    initUser();
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

    private void bbtShowDialog(){
        if (bbtDialog==null){
            bbtDialog = new AlertDialog.Builder(this)
                    .setMessage("确定要送ta一个棒棒糖吗?")
                    .setNegativeButton("不送", (dialog, which) -> {
                        if (bbtDialog != null) {
                            bbtDialog.dismiss();
                        }
                    })
                    .setPositiveButton("是的", (dialog, which) -> {
                        if (bbtDialog != null) {
                            bbtDialog.dismiss();
                        }
                        sengBangT();
                    }).create();
        }
        bbtDialog.show();
        bbtDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.title_color));
        bbtDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.now_txt_color));
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
