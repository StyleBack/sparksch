package com.doschool.ahu.appui.home.ui.fragment;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.event.XMessageEvent;
import com.doschool.ahu.appui.reglogin.bean.LoginVO;
import com.doschool.ahu.base.BaseFragment;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.utils.EventUtils;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ViewInject;


/**
 * Created by X on 2019/1/10.
 *
 * 他人资料
 */
public class OtherInfoFragment extends BaseFragment {

    public static final int OTH_CODE=1910;
    private int tagId;

    @ViewInject(R.id.oth_nktv)
    private TextView oth_nktv;

    @ViewInject(R.id.oth_xytv)
    private TextView oth_xytv;

    @ViewInject(R.id.oth_zytv)
    private TextView oth_zytv;

    @ViewInject(R.id.oth_njtv)
    private TextView oth_njtv;

    @ViewInject(R.id.oth_qmtv)
    private TextView oth_qmtv;

    @ViewInject(R.id.oth_hobetv)
    private TextView oth_hobetv;

    private ArrayMap<String,String> userMap=new ArrayMap<>();

    public static OtherInfoFragment newInstance(int userId){
        OtherInfoFragment otherInfoFragment=new OtherInfoFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("taggetId",userId);
        otherInfoFragment.setArguments(bundle);
        return otherInfoFragment;
    }


    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_otherinfo;
    }

    @Override
    protected void initViewEvents(Bundle savedInstanceState) {
        EventUtils.register(this);
        if (getArguments()!=null){
            tagId=getArguments().getInt("taggetId");
        }
        userMap=XLNetHttps.getBaseMap(getActivity());
        init();
    }

    private void init(){
        userMap.put("objId",String.valueOf(tagId));
        XLNetHttps.request(ApiConfig.API_MINE, userMap, LoginVO.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                LoginVO loginVO= XLGson.fromJosn(result,LoginVO.class);
                if (loginVO.getCode()==0){
                    upUI(loginVO.getData());
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

    private void upUI(LoginVO.LoginData data){
        //备注判断
        if (!TextUtils.isEmpty(data.getRemarkName())){
            oth_nktv.setText(data.getRemarkName());
        }else {
            oth_nktv.setText(data.getUserDO().getNickName());
        }
        oth_xytv.setText(data.getDepartName());
        oth_zytv.setText(data.getMajorName());
        oth_njtv.setText(data.getUserDO().getEnrYear()+"级");
        oth_qmtv.setText(data.getUserDO().getSelfIntro());
        oth_hobetv.setText(data.getUserDO().getHobby());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OthEvent(XMessageEvent event){
        if (event.getCode()==OTH_CODE){
            init();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventUtils.unRegister(this);
    }
}
