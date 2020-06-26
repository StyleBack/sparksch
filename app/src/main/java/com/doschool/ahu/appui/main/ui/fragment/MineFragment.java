package com.doschool.ahu.appui.main.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.home.ui.activity.PersionalActivity;
import com.doschool.ahu.appui.main.event.XMessageEvent;
import com.doschool.ahu.appui.main.ui.activity.UpLoadValidationActivity;
import com.doschool.ahu.appui.main.ui.activity.VerifingActivity;
import com.doschool.ahu.appui.main.ui.activity.WebActivity;
import com.doschool.ahu.appui.mine.ui.activity.CollectedActivity;
import com.doschool.ahu.appui.mine.ui.activity.FansAttentionActivity;
import com.doschool.ahu.appui.mine.ui.activity.MineTopicActivity;
import com.doschool.ahu.appui.mine.ui.activity.SetActivity;
import com.doschool.ahu.appui.mine.ui.adapter.ServiceAdapter;
import com.doschool.ahu.appui.mine.ui.bean.ToolService;
import com.doschool.ahu.appui.reglogin.bean.LoginVO;
import com.doschool.ahu.base.BaseFragment;
import com.doschool.ahu.base.adapter.BaseRvAdapter;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.configfile.CodeConfig;
import com.doschool.ahu.db.AppConfigDao;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.AlertUtils;
import com.doschool.ahu.utils.EventUtils;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XRvUtils;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jaeger.library.StatusBarUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.concurrent.TimeUnit;


import static com.doschool.ahu.appui.home.ui.activity.PersionalActivity.PER_CODE_HEAD;
import static com.doschool.ahu.appui.main.ui.activity.ChangePersonalActivity.CHANGE_CODE;


/**
 * Created by X on 2018/7/4
 *
 * 个人中心
 */
public class MineFragment extends BaseFragment {

    //title
    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.tool_right_tv)
    private TextView tool_right_tv;

    @ViewInject(R.id.mine_rv)
    private XRecyclerView mine_rv;
    private LinearLayoutManager linearLayoutManager;

    private ImageView mine_ivhead;
    private TextView mine_txname;
    private ImageView mine_ivsex,ver_iv;
    private TextView mine_txsig;
    private TextView tvZan;
    private TextView mine_txfans;
    private TextView mine_txins;
    private TextView mine_txtang;
    private RecyclerView ser_rv;
    private LinearLayout mine_ll_goto;
    private RelativeLayout foot_rlcoll;
    private RelativeLayout foot_rltop,mine_rlgoper,ver_rl;
    private LinearLayout mine_fansll,mine_attentll,mine_bangll;
    private LoginDao loginDao;
    private boolean isRefresh=false;

    private ArrayMap<String,String> map=new ArrayMap<>();
    private ArrayMap<String,String> toolMap=new ArrayMap<>();
    private ServiceAdapter serviceAdapter;

    private int userID;

    private AppConfigDao appConfigDao;
    private int sp_ver;
    private int handStatus;

    @Override
    public void onResume() {
        StatusBarUtil.setColor(getActivity(),getResources().getColor(R.color.white),0);
        StatusBarUtil.setLightMode(getActivity());
        super.onResume();
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.minefragment_layout;
    }

    @Override
    protected void initViewEvents(Bundle savedInstanceState) {
        tool_title_tv.setVisibility(View.VISIBLE);
        tool_title_tv.setText("我的");
        tool_right_tv.setVisibility(View.VISIBLE);
        tool_right_tv.setText("设置");
        EventUtils.register(this);
        loginDao=new LoginDao(getActivity());
        appConfigDao=new AppConfigDao(getActivity());
        map= XLNetHttps.getBaseMap(getActivity());
        toolMap=XLNetHttps.getBaseMap(getActivity());
        initRv();
        addView();
        if (loginDao.getObject()!=null){
            getDb();
        }else {
            initData();
        }

        initRvServ();
        toolService();
    }

    private void initRv(){
        linearLayoutManager=new LinearLayoutManager(getActivity());
        XRvUtils.initRv(mine_rv,linearLayoutManager,LinearLayoutManager.VERTICAL,true,true,false);
        mine_rv.setAdapter(new BaseRvAdapter(getActivity()) {
            @Override
            protected int getItemLayoutID(int viewType) {
                return 0;
            }

            @Override
            protected RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            protected void bindData(RecyclerView.ViewHolder holder, int position, Object data) {

            }
        });

        mine_rv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRefresh=true;
                initData();
                toolService();
            }

            @Override
            public void onLoadMore() {

            }
        });
    }

    @SuppressLint("CheckResult")
    private void addView(){
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.mine_add_view,null);
        mine_rv.addHeaderView(view);

        mine_fansll=view.findViewById(R.id.mine_fansll);
        mine_attentll=view.findViewById(R.id.mine_attentll);
        mine_bangll=view.findViewById(R.id.mine_bangll);
        mine_ivhead=view.findViewById(R.id.mine_ivhead);
        mine_txname=view.findViewById(R.id.mine_txname);
        mine_ivsex=view.findViewById(R.id.mine_ivsex);
        mine_txsig=view.findViewById(R.id.mine_txsig);
        tvZan=view.findViewById(R.id.tvZan);
        mine_txfans=view.findViewById(R.id.mine_txfans);
        mine_txins=view.findViewById(R.id.mine_txins);
        mine_txtang=view.findViewById(R.id.mine_txtang);
        ser_rv=view.findViewById(R.id.ser_rv);
        mine_ll_goto=view.findViewById(R.id.mine_ll_goto);
        foot_rlcoll=view.findViewById(R.id.foot_rlcoll);
        foot_rltop=view.findViewById(R.id.foot_rltop);
        mine_rlgoper=view.findViewById(R.id.mine_rlgoper);
        ver_rl=view.findViewById(R.id.ver_rl);
        ver_iv=view.findViewById(R.id.ver_iv);

        //2验证或验证中
        RxView.clicks(ver_rl)
                .throttleFirst(2,TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (handStatus==1){
                        IntentUtil.toActivity(getActivity(),null, VerifingActivity.class);
                    }else {
                        IntentUtil.toActivity(getActivity(),null, UpLoadValidationActivity.class);
                    }
                });
        //关闭验证
        ver_iv.setOnClickListener(v -> {
            SPUtils.getInstance().put("verify",1);
            ver_rl.setVisibility(View.GONE);
        });

        //个人资料
        RxView.clicks(mine_rlgoper)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    Bundle bundle=new Bundle();
                    bundle.putInt("userid",userID);
                    IntentUtil.toActivity(getActivity(),bundle,PersionalActivity.class);
                });
        //fans
        RxView.clicks(mine_fansll)
                .throttleFirst(2,TimeUnit.SECONDS)
                .subscribe(o -> {
                    Bundle bundle=new Bundle();
                    bundle.putInt("lop",CodeConfig.FOLLOW_FS);
                    IntentUtil.toActivity(getActivity(),bundle, FansAttentionActivity.class);
                });
        //关注
        RxView.clicks(mine_attentll)
                .throttleFirst(2,TimeUnit.SECONDS)
                .subscribe(o -> {
                    Bundle bundle=new Bundle();
                    bundle.putInt("lop",CodeConfig.FOLLOW_GZ);
                    IntentUtil.toActivity(getActivity(),bundle, FansAttentionActivity.class);
                });
        //帮帮糖
        RxView.clicks(mine_bangll)
                .throttleFirst(2,TimeUnit.SECONDS)
                .subscribe(o->{
                    if (noneMember()){
                        AlertUtils.alertToVerify(getActivity(),loginDao.getObject().getHandleStatus());
                    }else {
                        if (appConfigDao.getAppCinfigDO()!=null){
                            Bundle bundle=new Bundle();
                            bundle.putString("URL",appConfigDao.getAppCinfigDO().getLollipopHistoryUrl());
                            IntentUtil.toActivity(getActivity(),bundle, WebActivity.class);
                        }
                    }
                });

        //收藏
        RxView.clicks(foot_rlcoll)
                .throttleFirst(2,TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (noneMember()){
                        AlertUtils.alertToVerify(getActivity(),loginDao.getObject().getHandleStatus());
                    }else {
                        IntentUtil.toActivity(getActivity(),null, CollectedActivity.class);
                    }
                });

        //话题
        RxView.clicks(foot_rltop)
                .throttleFirst(2,TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (noneMember()){
                        AlertUtils.alertToVerify(getActivity(),loginDao.getObject().getHandleStatus());
                    }else {
                        IntentUtil.toActivity(getActivity(),null, MineTopicActivity.class);
                    }
                });
    }

    private void getDb(){
            getInfo(loginDao.getObject());
    }

    private void initData(){
        if (loginDao.getObject()!=null && loginDao.getObject().getUserDO()!=null){
            map.put("objId",String.valueOf(loginDao.getObject().getUserDO().getId()));
        }
        XLNetHttps.request(ApiConfig.API_MINE, map, LoginVO.class,  new XLCallBack() {
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
                if (isRefresh){
                    mine_rv.refreshComplete();
                    isRefresh=false;
                }
            }
        });
    }

    //个人信息
    private void getInfo(LoginVO.LoginData loginData){
        sp_ver= SPUtils.getInstance().getInt("verify");
        userID=loginData.getUserDO().getId();
        handStatus=loginData.getHandleStatus();

        XLGlideLoader.loadCircleImage(mine_ivhead,loginData.getHeadThumbnail());
        mine_txname.setText(loginData.getUserDO().getNickName());
        if (loginData.getUserDO().getSex()==1){
            mine_ivsex.setImageResource(R.mipmap.icon_identity_girl);
        }else if (loginData.getUserDO().getSex()==2){
            mine_ivsex.setImageResource(R.mipmap.icon_identity_boy);
        }
        mine_txsig.setText(loginData.getUserDO().getSelfIntro());
        tvZan.setText(String.valueOf(loginData.getUgcContentLikeCount()));
        mine_txfans.setText(String.valueOf(loginData.getFollowersCount()));
        mine_txins.setText(String.valueOf(loginData.getFollowingCount()));
        mine_txtang.setText(String.valueOf(loginData.getLollipopCount()));

        if (sp_ver==0 && loginData.getUserDO().getStatus()==0){
            ver_rl.setVisibility(View.VISIBLE);
        }else {
            ver_rl.setVisibility(View.GONE);
        }
    }


    private void toolService(){
        toolMap.put("type",String.valueOf(CodeConfig.SERVICE_MINE));
        XLNetHttps.request(ApiConfig.API_TOOL_SERVICE, toolMap,  ToolService.class,  new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                ToolService toolService=XLGson.fromJosn(result,ToolService.class);
                initTool(toolService);
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

    private void initRvServ(){
        LinearLayoutManager lay=new LinearLayoutManager(getActivity());
        lay.setOrientation(LinearLayoutManager.VERTICAL);
        ser_rv.setLayoutManager(lay);
        serviceAdapter=new ServiceAdapter(getActivity());
        ser_rv.setAdapter(serviceAdapter);
    }
    //服务工具
    private void initTool(ToolService toolService){
        if (toolService.getData()!=null && toolService.getData().size()>0){
            ser_rv.setVisibility(View.VISIBLE);
            serviceAdapter.setDatas(toolService.getData());
        }else {
            ser_rv.setVisibility(View.GONE);
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


    @Event({R.id.tool_right_tv})
    private void onClicks(View view){
        switch (view.getId()){
            case R.id.tool_right_tv://设置
                IntentUtil.toActivity(getActivity(),null, SetActivity.class);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ChaEvent(XMessageEvent event){
        if (event.getCode()== CHANGE_CODE || event.getCode()==PER_CODE_HEAD){
            initData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XRvUtils.destroyRv(mine_rv);
        EventUtils.unRegister(this);
    }

}
