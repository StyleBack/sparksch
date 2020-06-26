package com.doschool.ahu.appui.infors.chat.ui.model;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.RelativeLayout;

import com.doschool.ahu.appui.home.ui.activity.OtherSingleActivity;
import com.doschool.ahu.appui.infors.chat.ui.adapter.holder.ViewHolder;
import com.doschool.ahu.appui.infors.chat.util.TimeUtil;
import com.doschool.ahu.appui.reglogin.bean.LoginVO;
import com.doschool.ahu.base.BaseApplication;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageStatus;
import com.tencent.imsdk.ext.message.TIMMessageExt;

import org.xutils.common.Callback;

import static com.doschool.ahu.base.BaseApplication.getContext;

/**
 * 消息数据基类
 */
public abstract class Message {

    protected final String TAG = "Message";

    TIMMessage message;

    private boolean hasTime;

    /**
     * 消息描述信息
     */
    private String desc;


    public TIMMessage getMessage() {
        return message;
    }


    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context 显示消息的上下文
     */
    public abstract void showMessage(ViewHolder viewHolder, Context context);

    /**
     * 获取显示气泡
     *
     * @param viewHolder 界面样式
     */
    public RelativeLayout getBubbleView(ViewHolder viewHolder){
        viewHolder.systemMessage.setVisibility(hasTime? View.VISIBLE: View.GONE);
        viewHolder.systemMessage.setText(TimeUtil.getChatTimeStr(message.timestamp()));
        showDesc(viewHolder);
        if (message.isSelf()){
            viewHolder.leftPanel.setVisibility(View.GONE);
            viewHolder.rightPanel.setVisibility(View.VISIBLE);
            LoginDao loginDao=new LoginDao(BaseApplication.getInstance());
            ArrayMap<String, String> map = XLNetHttps.getBaseMap(BaseApplication.getInstance());
            map.put("objId", String.valueOf(loginDao.getObject().getUserDO().getId()));
            XLNetHttps.request(ApiConfig.API_MINE, map, LoginVO.class, new XLCallBack() {
                @Override
                public void XLSucc(String result) {
                    LoginVO loginVO = XLGson.fromJosn(result, LoginVO.class);
                    if (loginVO.getCode() == 0) {
//                                viewHolder.sender.setText(loginVO.getData().getUserDO().getNickName());
                        XLGlideLoader.loadCircleImage(viewHolder.rightAvatar,loginVO.getData().getUserDO().getHeadImage());
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

            return viewHolder.rightMessage;
        }else{
            viewHolder.leftPanel.setVisibility(View.VISIBLE);
            viewHolder.rightPanel.setVisibility(View.GONE);
            //群聊显示名称，群名片>个人昵称>identify
//            if (message.getConversation().getType() == TIMConversationType.Group){
//                viewHolder.sender.setVisibility(View.VISIBLE);
//                String name = "";
//                if (message.getSenderGroupMemberProfile()!=null) name = message.getSenderGroupMemberProfile().getNameCard();
//                if (name.equals("")&&message.getSenderProfile()!=null) name = message.getSenderProfile().getNickName();
//                if (name.equals("")) name = message.getSender();
//                viewHolder.sender.setText(name);
//            }else{
                    ArrayMap<String, String> map = XLNetHttps.getBaseMap(BaseApplication.getInstance());
                    map.put("objId", message.getConversation().getPeer());
                    XLNetHttps.request(ApiConfig.API_MINE, map, LoginVO.class, new XLCallBack() {
                        @Override
                        public void XLSucc(String result) {
                            LoginVO loginVO = XLGson.fromJosn(result, LoginVO.class);
                            if (loginVO.getCode() == 0) {
//                                viewHolder.sender.setText(loginVO.getData().getUserDO().getNickName());
                                XLGlideLoader.loadCircleImage(viewHolder.leftAvatar,loginVO.getData().getUserDO().getHeadImage());
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

                    //进个人主页
                    viewHolder.leftAvatar.setOnClickListener(v -> {
                        Bundle bundle=new Bundle();
                        bundle.putInt("userid", Integer.parseInt(message.getConversation().getPeer()));
                        IntentUtil.toActivity(getContext(),bundle,OtherSingleActivity.class);
                    });
                viewHolder.sender.setVisibility(View.GONE);
//            }
            return viewHolder.leftMessage;
        }

    }

    /**
     * 显示消息状态
     *
     * @param viewHolder 界面样式
     */
    public void showStatus(ViewHolder viewHolder){
        switch (message.status()){
            case Sending:
                viewHolder.error.setVisibility(View.GONE);
                viewHolder.sending.setVisibility(View.VISIBLE);
                break;
            case SendSucc:
                viewHolder.error.setVisibility(View.GONE);
                viewHolder.sending.setVisibility(View.GONE);
                break;
            case SendFail:
                viewHolder.error.setVisibility(View.VISIBLE);
                viewHolder.sending.setVisibility(View.GONE);
                viewHolder.leftPanel.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 判断是否是自己发的
     *
     */
    public boolean isSelf(){
        return message.isSelf();
    }

    /**
     * 获取消息摘要
     *
     */
    public abstract String getSummary();
    String revoke_another;
    String getRevokeSummary() {
        String revoke_by_self = "你撤回了一条消息";
        String revoke_by_other = "'%1$s'撤回了一条消息";
        String revoke_next="消息被撤回";
        if (message.status() == TIMMessageStatus.HasRevoked) {
            if(message.isSelf()){
                return revoke_by_self;
            }else {
//                ArrayMap<String, String> map = XLNetHttps.getBaseMap(BaseApplication.getInstance());
//                map.put("objId", message.getSender());
//                XLNetHttps.request(ApiConfig.API_MINE, map LoginVO.class new XLCallBack() {
//                    @Override
//                    public void XLSucc(String result) {
//                        LoginVO loginVO = XLGson.fromJosn(result, LoginVO.class);
//                        if (loginVO.getCode() == 0) {
//                            if (!TextUtils.isEmpty(loginVO.getData().getRemarkName())){
//                                revoke_another=loginVO.getData().getRemarkName()+"撤回了一条消息";
//                            }else {
//                                revoke_another=loginVO.getData().getUserDO().getNickName()+"撤回了一条消息";
//                            }
//                        }
//                    }
//                    @Override
//                    public void XLError(Throwable ex, boolean isOnCallback) {
//                    }
//                    @Override
//                    public void XLCancle(Callback.CancelledException cex) {
//                    }
//                    @Override
//                    public void XLFinish() {
//                    }
//                });
                return revoke_next;
            }
        }
        return null;
    }

    /**
     * 保存消息或消息文件
     *
     */
    public abstract void save();


    /**
     * 删除消息
     *
     */
    public void remove(){
        TIMMessageExt ext = new TIMMessageExt(message);
        ext.remove();
    }




    /**
     * 是否需要显示时间获取
     *
     */
    public boolean getHasTime() {
        return hasTime;
    }


    /**
     * 是否需要显示时间设置
     *
     * @param message 上一条消息
     */
    public void setHasTime(TIMMessage message){
        if (message == null){
            hasTime = true;
            return;
        }
        hasTime = this.message.timestamp() - message.timestamp() > 300;
    }


    /**
     * 消息是否发送失败
     *
     */
    public boolean isSendFail(){
        return message.status() == TIMMessageStatus.SendFail;
    }

    /**
     * 清除气泡原有数据
     *
     */
    protected void clearView(ViewHolder viewHolder){
        getBubbleView(viewHolder).removeAllViews();
        getBubbleView(viewHolder).setOnClickListener(null);
    }

    /**
     * 显示撤回的消息
     *
     */
    boolean checkRevoke(ViewHolder viewHolder) {
        if (message.status() == TIMMessageStatus.HasRevoked) {
            viewHolder.leftPanel.setVisibility(View.GONE);
            viewHolder.rightPanel.setVisibility(View.GONE);
            viewHolder.systemMessage.setVisibility(View.VISIBLE);
            viewHolder.systemMessage.setText(getSummary());
            return true;
        }
        return false;
    }

    /**
     * 获取发送者
     *
     */
    public String getSender(){
        if (message.getSender() == null) return "";
        return message.getSender();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    private void showDesc(ViewHolder viewHolder){

        if (desc == null || desc.equals("")){
            viewHolder.rightDesc.setVisibility(View.GONE);
        }else{
            viewHolder.rightDesc.setVisibility(View.VISIBLE);
            viewHolder.rightDesc.setText(desc);
        }
    }
}
