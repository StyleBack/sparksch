package com.doschool.ahu.appui.infors.chat.ui.model;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.infors.chat.ui.ChatActivity;
import com.doschool.ahu.appui.reglogin.bean.LoginVO;
import com.doschool.ahu.base.BaseApplication;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.ext.message.TIMConversationExt;

import org.xutils.common.Callback;


/**
 * 好友或群聊的会话
 */
public class NomalConversation extends Conversation {


    private TIMConversation conversation;



    //最后一条消息
    private Message lastMessage;
    private String userImage;

    public NomalConversation(TIMConversation conversation){
        this.conversation = conversation;
        type = conversation.getType();
        identify = conversation.getPeer();
    }


    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }


    @Override
    public String getAvatar() {
        switch (type){
            case C2C:
                ArrayMap<String,String> map=XLNetHttps.getBaseMap(BaseApplication.getInstance());
                map.put("objId",identify);
                XLNetHttps.request(ApiConfig.API_MINE, map, LoginVO.class, new XLCallBack() {
                    @Override
                    public void XLSucc(String result) {
                        LoginVO loginVO= XLGson.fromJosn(result,LoginVO.class);
                        if (loginVO.getCode()==0){
                            userImage=loginVO.getData().getUserDO().getHeadImage();
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
                return userImage;
//            case Group:
//                return String.valueOf(R.mipmap.head_group);
        }
        return "";
    }

    /**
     * 跳转到聊天界面或会话详情
     *
     * @param context 跳转上下文
     */
    @Override
    public void navToDetail(Context context) {
        ChatActivity.navToChat(context,identify,name,type);
    }

    /**
     * 获取最后一条消息摘要
     */
    @Override
    public String getLastMessageSummary(){
        TIMConversationExt ext = new TIMConversationExt(conversation);
        if (ext.hasDraft()){
            TextMessage textMessage = new TextMessage(ext.getDraft());
            if (lastMessage == null || lastMessage.getMessage().timestamp() < ext.getDraft().getTimestamp()){
                return BaseApplication.getContext().getString(R.string.conversation_draft) + textMessage.getSummary();
            }else{
                return lastMessage.getSummary();
            }
        }else{
            if (lastMessage == null){
                return "";
            }else {
                return lastMessage.getSummary();
            }
        }
    }

    /**
     * 获取名称
     */
    @Override
    public String getName() {
//        if (type == TIMConversationType.Group){
//            name=GroupInfo.getInstance().getGroupName(identify);
//            if (name.equals("")) name = "群组";
//        }else{
            ArrayMap<String,String> map=XLNetHttps.getBaseMap(BaseApplication.getInstance());
            map.put("objId",identify);
            XLNetHttps.request(ApiConfig.API_MINE, map, LoginVO.class, new XLCallBack() {
                @Override
                public void XLSucc(String result) {
                    LoginVO loginVO= XLGson.fromJosn(result,LoginVO.class);
                    if (loginVO.getCode()==0){
                        if (!TextUtils.isEmpty(loginVO.getData().getRemarkName())){
                            name=loginVO.getData().getRemarkName();
                        }else {
                            name=loginVO.getData().getUserDO().getNickName();
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

//        }
        return name;
    }

    /**
     * 获取未读消息数量
     */
    @Override
    public long getUnreadNum(){
        if (conversation == null) return 0;
        TIMConversationExt ext = new TIMConversationExt(conversation);
        return ext.getUnreadMessageNum();
    }

    /**
     * 将所有消息标记为已读
     */
    @Override
    public void readAllMessage(){
        if (conversation != null){
            TIMConversationExt ext = new TIMConversationExt(conversation);
            ext.setReadMessage(null, null);
        }
    }


    /**
     * 获取最后一条消息的时间
     */
    @Override
    public long getLastMessageTime() {
        TIMConversationExt ext = new TIMConversationExt(conversation);
        if (ext.hasDraft()){
            if (lastMessage == null || lastMessage.getMessage().timestamp() < ext.getDraft().getTimestamp()){
                return ext.getDraft().getTimestamp();
            }else{
                return lastMessage.getMessage().timestamp();
            }
        }
        if (lastMessage == null) return 0;
        return lastMessage.getMessage().timestamp();
    }

    /**
     * 获取会话类型
     */
    public TIMConversationType getType(){
        return conversation.getType();
    }
}
