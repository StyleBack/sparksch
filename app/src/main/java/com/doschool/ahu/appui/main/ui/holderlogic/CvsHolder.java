package com.doschool.ahu.appui.main.ui.holderlogic;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.infors.chat.ui.model.Conversation;
import com.doschool.ahu.appui.infors.chat.util.TimeUtil;
import com.doschool.ahu.appui.reglogin.bean.LoginVO;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.db.ConversationDO;
import com.doschool.ahu.db.ConversationDao;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout;

import org.xutils.common.Callback;

import java.util.List;


/**
 * Created by X on 2018/8/23
 */
public class CvsHolder extends BaseRvHolder {

    public RelativeLayout swipe_content;
    public ImageView cvs_m_img;
    public TextView cvs_m_tvname;
    public TextView cvs_m_tvmsg;
    public TextView cvs_m_tvtime;
    public TextView cvs_m_tvnum;
    public EasySwipeMenuLayout swipelayout;
    public TextView right_top;
    public TextView right_delete;

    public CvsHolder(View itemView) {
        super(itemView);
        swipe_content=findViewById(R.id.swipe_content);
        cvs_m_img=findViewById(R.id.cvs_m_img);
        cvs_m_tvname=findViewById(R.id.cvs_m_tvname);
        cvs_m_tvmsg=findViewById(R.id.cvs_m_tvmsg);
        cvs_m_tvtime=findViewById(R.id.cvs_m_tvtime);
        cvs_m_tvnum=findViewById(R.id.cvs_m_tvnum);
        swipelayout=findViewById(R.id.swipelayout);
        right_top=findViewById(R.id.right_top);
        right_delete=findViewById(R.id.right_delete);
    }

    public static CvsHolder newInstance(ViewGroup viewGroup,int layoutId){
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(layoutId,viewGroup,false);
        return new CvsHolder(view);
    }

    public void setUpDatas(final Context context, Conversation data,ConversationDao conversationDao){
        List<ConversationDO> orderList= conversationDao.getAllDate();

        ArrayMap<String, String> map = XLNetHttps.getBaseMap(context);
        map.put("objId", data.getIdentify());
        XLNetHttps.request(ApiConfig.API_MINE, map, LoginVO.class,new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                LoginVO loginVO = XLGson.fromJosn(result, LoginVO.class);
                if (loginVO.getCode() == 0) {
                    XLGlideLoader.loadCircleImage(cvs_m_img, loginVO.getData().getUserDO().getHeadImage());
                    if (!TextUtils.isEmpty(loginVO.getData().getRemarkName())){
                        cvs_m_tvname.setText(loginVO.getData().getRemarkName());
                    }else {
                        cvs_m_tvname.setText(loginVO.getData().getUserDO().getNickName());
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

        XLGlideLoader.loadCircleImage(cvs_m_img, data.getAvatar());
        cvs_m_tvname.setText(data.getName());
        cvs_m_tvmsg.setText(data.getLastMessageSummary());
        cvs_m_tvtime.setText(TimeUtil.getTimeStr(data.getLastMessageTime()));
        if (data.getUnreadNum()==0){
            cvs_m_tvnum.setVisibility(View.GONE);
        }else {
            cvs_m_tvnum.setVisibility(View.VISIBLE);
            if (data.getUnreadNum()>99){
                cvs_m_tvnum.setText("99+");
            }else {
                cvs_m_tvnum.setText(String.valueOf(data.getUnreadNum()));
            }
        }


        if (orderList!=null && orderList.size()>0){
            for (int i=0;i<orderList.size();i++){
                if (TextUtils.equals(data.getIdentify(),orderList.get(i).identify)){
                    right_top.setText("取消置顶");
                    swipe_content.setBackgroundColor(context.getResources().getColor(R.color.cvs_color));
                    break;
                }else {
                    right_top.setText("置顶");
                    swipe_content.setBackgroundColor(context.getResources().getColor(R.color.white));
                }
            }
        }

    }
}
