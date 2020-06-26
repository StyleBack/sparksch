package com.doschool.ahu.appui.main.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.event.UserComm;
import com.doschool.ahu.appui.main.event.XMessageEvent;
import com.doschool.ahu.appui.main.ui.activity.UpLoadValidationActivity;
import com.doschool.ahu.appui.main.ui.activity.VerifingActivity;
import com.doschool.ahu.appui.main.ui.activity.WebActivity;
import com.doschool.ahu.appui.mine.ui.activity.FansAttentionActivity;
import com.doschool.ahu.appui.mine.ui.activity.SetActivity;
import com.doschool.ahu.appui.mine.ui.fragment.MeDiaryFragment;
import com.doschool.ahu.appui.mine.ui.fragment.MeFootprintFragment;
import com.doschool.ahu.appui.reglogin.bean.LoginVO;
import com.doschool.ahu.base.BaseFragment;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.configfile.CodeConfig;
import com.doschool.ahu.db.AppConfigDao;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.factory.AppDoUrlFactory;
import com.doschool.ahu.utils.AlertUtils;
import com.doschool.ahu.utils.EventUtils;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.widget.xtablay.IndicatorAdapter;
import com.doschool.ahu.widget.xtablay.TabFragmentAdapter;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jaeger.library.StatusBarUtil;
import com.jakewharton.rxbinding2.view.RxView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


import static com.doschool.ahu.appui.main.ui.activity.ChangePersonalActivity.CHANGE_CODE;
import static com.doschool.ahu.utils.ChangeAlphaUtils.changeAlpha;

/**
 * Created by X on 2019/1/11.
 */
public class TabMeFragment extends BaseFragment {


    private static final String[] CHANNELS = new String[]{"校园日记","足迹"};
    private List<String> mDataList = Arrays.asList(CHANNELS);

    @ViewInject(R.id.tabme_title)
    private TextView tabme_title;
    @ViewInject(R.id.tabme_right)
    private ImageView tabme_right;
    @ViewInject(R.id.tabme_tool)
    private Toolbar tabme_tool;
    @ViewInject(R.id.tabme_line)
    private View tabme_line;
    @ViewInject(R.id.tabme_tab)
    private MagicIndicator tabme_tab;
    @ViewInject(R.id.tabme_vp)
    private ViewPager tabme_vp;

    @ViewInject(R.id.tabme_verrl)
    private RelativeLayout tabme_verrl;
    @ViewInject(R.id.tabme_veriv)
    private ImageView tabme_veriv;


    @ViewInject(R.id.tabme_appbar)
    private AppBarLayout tabme_appbar;
    @ViewInject(R.id.tabme_ivbg)
    private ImageView tabme_ivbg;
    @ViewInject(R.id.tabme_iv_head)
    private ImageView tabme_iv_head;
    @ViewInject(R.id.tabme_iv_sex)
    private ImageView tabme_iv_sex;
    @ViewInject(R.id.tabme_iv_org)
    private ImageView tabme_iv_org;
    @ViewInject(R.id.tabme_iv_zan)
    private ImageView tabme_iv_zan;
    @ViewInject(R.id.tabme_tvzn)
    private TextView tabme_tvzn;
    @ViewInject(R.id.tabme_llbbt)
    private LinearLayout tabme_llbbt;
    @ViewInject(R.id.tabme_v_bbt)
    private ImageView tabme_v_bbt;
    @ViewInject(R.id.tabme_tvbt)
    private TextView tabme_tvbt;
    @ViewInject(R.id.tabme_tv_fans)
    private TextView tabme_tv_fans;
    @ViewInject(R.id.tabme_lfans)
    private LinearLayout tabme_lfans;
    @ViewInject(R.id.tabme_tv_gz)
    private TextView tabme_tv_gz;
    @ViewInject(R.id.tabme_lgz)
    private LinearLayout tabme_lgz;

    private LoginDao loginDao;
    private AppConfigDao appConfigDao;

    private ArrayMap<String,String> infoMap=new ArrayMap<>();

    private int sp_ver;
    private int handStatus;

    private TabFragmentAdapter adapter;
    private List<BaseFragment> fragments=new ArrayList<>();
    private CommonNavigator commonNavigator;


    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_tabme_layout;
    }

    @Override
    public void onResume() {
        StatusBarUtil.setTranslucentForImageView(getActivity(),0,null);
        StatusBarUtil.setLightMode(getActivity());
        super.onResume();
        initInfo();
    }


    @Override
    protected void initViewEvents(Bundle savedInstanceState) {
        tabme_title.setVisibility(View.VISIBLE);
        EventUtils.register(this);
        loginDao=new LoginDao(getActivity());
        appConfigDao=new AppConfigDao(getActivity());
        infoMap=XLNetHttps.getBaseMap(getActivity());
        initAppbarChange();
        if (loginDao.getObject()!=null){
            getLocalDB();
        }else {
            initInfo();
        }
        initChildFragment();
        clicks();
    }

    private void initAppbarChange(){
        tabme_appbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset<=-10){
                XLGlideLoader.loadImageById(tabme_right,R.mipmap.tabme_set_icon);
                tabme_title.setTextColor(getResources().getColor(R.color.title_color));
            }else {
                XLGlideLoader.loadImageById(tabme_right,R.mipmap.tabme_set_icon_white);
                tabme_title.setTextColor(getResources().getColor(R.color.white));
            }
            tabme_tool.setBackgroundColor(changeAlpha(getResources().getColor(R.color.white),Math.abs(verticalOffset*1.0f)/appBarLayout.getTotalScrollRange()));
            tabme_line.setBackgroundColor(changeAlpha(getResources().getColor(R.color.grey_link),Math.abs(verticalOffset*1.0f)/appBarLayout.getTotalScrollRange()));
        });
    }

    private void initChildFragment(){
        fragments.add(new MeDiaryFragment());
        fragments.add(new MeFootprintFragment());
        adapter=new TabFragmentAdapter(getChildFragmentManager(),fragments);
        tabme_vp.setAdapter(adapter);

        commonNavigator=new CommonNavigator(getActivity());
        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new IndicatorAdapter(tabme_vp,mDataList)
                .setXTextSize(16)
                .setNorColor(getResources().getColor(R.color.title_color))
                .setSelectColor(getResources().getColor(R.color.now_txt_color))
                .setIndicMode(LinePagerIndicator.MODE_EXACTLY)
                .setIndicLineHeight(4)
                .setIndicLineWidth(20)
                .setIndicRadius(3)
                .setIndicColor(getResources().getColor(R.color.now_txt_color)));
        tabme_tab.setNavigator(commonNavigator);
        ViewPagerHelper.bind(tabme_tab, tabme_vp);
    }


    private void getLocalDB(){
        getInfo(loginDao.getObject());
    }

    private void initInfo(){
        if (loginDao.getObject()!=null && loginDao.getObject().getUserDO()!=null){
            infoMap.put("objId",String.valueOf(loginDao.getObject().getUserDO().getId()));
        }
        XLNetHttps.request(ApiConfig.API_MINE, infoMap, LoginVO.class,new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                LoginVO loginVO= XLGson.fromJosn(result,LoginVO.class);
                if (loginVO.getCode()==0){
                    getInfo(loginVO.getData());

                    loginDao.clearUserTable();
                    loginDao.saveObject(loginVO.getData());
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

    private void getInfo(LoginVO.LoginData info){
        sp_ver= SPUtils.getInstance().getInt("verify");
        handStatus=info.getHandleStatus();

        //背景
        if (!TextUtils.isEmpty(info.getUserDO().getBackgroundImage())){
            XLGlideLoader.loadImageByUrl(tabme_ivbg,info.getUserDO().getBackgroundImage());
        }
        //获赞
        if (info.getUgcContentLikeCount()>0){
            tabme_iv_zan.setVisibility(View.GONE);
            tabme_tvzn.setText(String.valueOf(info.getUgcContentLikeCount()));
        }else {
            tabme_iv_zan.setVisibility(View.VISIBLE);
        }
        //棒棒糖
        if (info.getLollipopCount()>0){
            tabme_v_bbt.setVisibility(View.GONE);
            tabme_tvbt.setText(String.valueOf(info.getLollipopCount()));
        }else {
            tabme_v_bbt.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(info.getUserDO().getNickName())){
            tabme_title.setText(info.getUserDO().getNickName());
        }else {
            tabme_title.setText("我的");
        }
        //头像
        XLGlideLoader.loadCircleImage(tabme_iv_head,info.getUserDO().getHeadImage());
        //性别  组织标识
        if (info.getUserDO().getUserType()==2 || info.getUserDO().getUserType()==3){
            UserComm.updateIdentify(tabme_iv_org,info.getUserDO());
        } else {
            UserComm.updateIdentify(tabme_iv_sex,info.getUserDO());
        }
        //粉丝
        tabme_tv_fans.setText(String.valueOf(info.getFollowersCount()));

        //关注
        tabme_tv_gz.setText(String.valueOf(info.getFollowingCount()));

        //判断是否验证
        if (sp_ver==0 && info.getUserDO().getStatus()==0){
            tabme_verrl.setVisibility(View.VISIBLE);
        }else {
            tabme_verrl.setVisibility(View.GONE);
        }
    }

    @SuppressLint("CheckResult")
    private void clicks(){
        //设置
        RxView.clicks(tabme_right)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> IntentUtil.toActivity(getActivity(),null, SetActivity.class));
        //棒棒糖
        RxView.clicks(tabme_llbbt)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (AppDoUrlFactory.noneMember(getActivity())){
                        AlertUtils.alertToVerify(getActivity(),loginDao.getObject().getHandleStatus());
                    }else {
                        if (appConfigDao.getAppCinfigDO()!=null){
                            Bundle bundle=new Bundle();
                            bundle.putString("URL",appConfigDao.getAppCinfigDO().getLollipopHistoryUrl());
                            IntentUtil.toActivity(getActivity(),bundle, WebActivity.class);
                        }
                    }
                });
        //粉丝
        RxView.clicks(tabme_lfans)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    Bundle bundle=new Bundle();
                    bundle.putInt("lop",CodeConfig.FOLLOW_FS);
                    IntentUtil.toActivity(getActivity(),bundle, FansAttentionActivity.class);
                });
        //关注
        RxView.clicks(tabme_lgz)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    Bundle bundle=new Bundle();
                    bundle.putInt("lop",CodeConfig.FOLLOW_GZ);
                    IntentUtil.toActivity(getActivity(),bundle, FansAttentionActivity.class);
                });
        //关闭验证
        tabme_veriv.setOnClickListener(v -> {
            SPUtils.getInstance().put("verify",1);
            tabme_verrl.setVisibility(View.GONE);
        });
        //2验证或验证中
        RxView.clicks(tabme_verrl)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (handStatus==1){
                        IntentUtil.toActivity(getActivity(),null, VerifingActivity.class);
                    }else {
                        IntentUtil.toActivity(getActivity(),null, UpLoadValidationActivity.class);
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void InfoEvent(XMessageEvent event){
        if (event.getCode()== CHANGE_CODE){
            initInfo();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventUtils.unRegister(this);
    }
}
