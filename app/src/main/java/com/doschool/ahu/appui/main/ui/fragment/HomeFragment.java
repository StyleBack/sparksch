package com.doschool.ahu.appui.main.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SPUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.event.XMessageEvent;
import com.doschool.ahu.utils.EventUtils;
import com.doschool.ahu.widget.xtablay.IndicatorAdapter;
import com.doschool.ahu.appui.main.ui.activity.SearchActivity;
import com.doschool.ahu.base.BaseFragment;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.AlertUtils;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.widget.xtablay.TabFragmentAdapter;
import com.jaeger.library.StatusBarUtil;
import com.jakewharton.rxbinding2.view.RxView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.doschool.ahu.configfile.CodeConfig.SCROLL_CODE;
import static com.doschool.ahu.configfile.CodeConfig.SCROLL_CODE_FOLLOW;
import static com.doschool.ahu.configfile.CodeConfig.SCROLL_CODE_NEW;
import static com.doschool.ahu.configfile.CodeConfig.SCROLL_CODE_RECOMM;


/**
 * Created by X on 2018/7/4
 *
 * 首页
 */
public class HomeFragment extends BaseFragment {

    private static final String[] CHANNELS = new String[]{"最新","推荐","关注"};
    private List<String> mDataList = Arrays.asList(CHANNELS);

    //搜索
    @ViewInject(R.id.search_iv)
    private ImageView search_iv;

    @ViewInject(R.id.home_tabbar)
    private MagicIndicator home_tabbar;

    @ViewInject(R.id.bar_vp)
    private ViewPager bar_vp;

    private CommonNavigator commonNavigator;
    private TabFragmentAdapter adapter;
    private List<BaseFragment> fragments=new ArrayList<>();
    private LoginDao loginDao;

    @Override
    protected int getContentLayoutID() {
        return R.layout.homefragment_layout;
    }

    @Override
    protected void initViewEvents(Bundle savedInstanceState) {

        EventUtils.register(this);
        loginDao=new LoginDao(getActivity());
        initRxclick();
        initView();
    }


    @Override
    public void onResume() {
        StatusBarUtil.setTranslucentForImageView(getActivity(),0,null);
        StatusBarUtil.setLightMode(getActivity());
        super.onResume();
    }

    @SuppressLint("CheckResult")
    private void initRxclick(){
        RxView.clicks(search_iv)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                            if (noneMember()){
                                AlertUtils.alertToVerify(getActivity(),loginDao.getObject().getHandleStatus());
                            }else {
                                IntentUtil.toActivity(getActivity(),null, SearchActivity.class);
                            }
                });
    }

    //未验证用户
    private boolean noneMember(){
        int type= SPUtils.getInstance().getInt("phtype");
        if (type==-1 && loginDao.getObject()!=null &&  loginDao.getObject().getUserDO().getStatus()==0){
            return true;
        }
        return false;
    }

    private void initView(){
        fragments.add(new NewestFragment());
        fragments.add(new RecommendFragment());
        fragments.add(new ConcernFragment());

        bar_vp.setOffscreenPageLimit(2);
        adapter=new TabFragmentAdapter(getChildFragmentManager(),fragments);
        bar_vp.setAdapter(adapter);

        commonNavigator=new CommonNavigator(getActivity());
        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new IndicatorAdapter(bar_vp,mDataList)
                .setXTextSize(16)
                .setNorColor(getResources().getColor(R.color.un_title_color))
                .setSelectColor(getResources().getColor(R.color.title_color))
                .setIndicMode(LinePagerIndicator.MODE_EXACTLY)
                .setIndicLineHeight(4)
                .setIndicLineWidth(20)
                .setIndicRadius(3)
                .setIndicColor(getResources().getColor(R.color.now_txt_color)));
        home_tabbar.setNavigator(commonNavigator);
//        设置默认加载tab
        bar_vp.setCurrentItem(1);
        home_tabbar.onPageSelected(1);
        ViewPagerHelper.bind(home_tabbar, bar_vp);
    }

    //控制可见的回顶刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void scrollTab(XMessageEvent event){
        if (event.getCode()==SCROLL_CODE ){
            if (bar_vp.getCurrentItem()==0){
                EventUtils.onPost(new XMessageEvent(SCROLL_CODE_NEW));
            }else if (bar_vp.getCurrentItem()==1){
                EventUtils.onPost(new XMessageEvent(SCROLL_CODE_RECOMM));
            }else if (bar_vp.getCurrentItem()==2){
                EventUtils.onPost(new XMessageEvent(SCROLL_CODE_FOLLOW));
            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventUtils.unRegister(this);
        for (int i=0;i<fragments.size();i++){
            fragments.get(i).onDestroy();
        }
    }
}
