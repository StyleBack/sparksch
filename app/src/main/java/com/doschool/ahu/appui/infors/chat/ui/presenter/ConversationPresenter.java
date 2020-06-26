package com.doschool.ahu.appui.infors.chat.ui.presenter;

import android.util.Log;

import com.doschool.ahu.appui.infors.chat.ui.event.FriendshipEvent;
import com.doschool.ahu.appui.infors.chat.ui.event.GroupEvent;
import com.doschool.ahu.appui.infors.chat.ui.event.MessageEvent;
import com.doschool.ahu.appui.infors.chat.ui.event.RefreshEvent;
import com.doschool.ahu.appui.infors.chat.ui.model.CustomMessage;
import com.doschool.ahu.appui.infors.chat.ui.model.Message;
import com.doschool.ahu.appui.infors.chat.ui.model.MessageFactory;
import com.doschool.ahu.appui.infors.chat.ui.viewfeatures.ConversationView;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMManagerExt;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 会话界面逻辑
 */
public class ConversationPresenter implements Observer {

    private static final String TAG = "ConversationPresenter";
    private ConversationView view;

    public ConversationPresenter(ConversationView view){
        //注册消息监听
        MessageEvent.getInstance().addObserver(this);
        //注册刷新监听
        RefreshEvent.getInstance().addObserver(this);
        //注册好友关系链监听
        FriendshipEvent.getInstance().addObserver(this);
        //注册群关系监听
        GroupEvent.getInstance().addObserver(this);
        this.view = view;
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent){
            if (data instanceof TIMMessage) {
                TIMMessage msg = (TIMMessage) data;
                Message mMessage = MessageFactory.getMessage(msg);
                if (mMessage != null) {
                    if (mMessage instanceof CustomMessage){
                        TIMCustomElem elem = (TIMCustomElem) msg.getElement(0);
                        try {
                            String str = new String(elem.getData(), "UTF-8");
                            JSONObject jsonObject = new JSONObject(str);
                            int extType=jsonObject.optInt("extType");
                            if (extType==99){
                            }else {
                                view.updateMessage(msg);
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        view.updateMessage(msg);
                    }
                }
            }
        }else if (observable instanceof FriendshipEvent){
            FriendshipEvent.NotifyCmd cmd = (FriendshipEvent.NotifyCmd) data;
            switch (cmd.type){
                case ADD_REQ:
                case READ_MSG:
                case ADD:
                    view.updateFriendshipMessage();
                    break;
            }
        }else if (observable instanceof GroupEvent){
            GroupEvent.NotifyCmd cmd = (GroupEvent.NotifyCmd) data;
            switch (cmd.type){
                case UPDATE:
                case ADD:
                    view.updateGroupInfo((TIMGroupCacheInfo) cmd.data);
                    break;
                case DEL:
                    view.removeConversation((String) cmd.data);
                    break;

            }
        }else if (observable instanceof RefreshEvent){
            getConversation();
            view.refresh();
        }
    }



    public void getConversation(){
        List<TIMConversation> list = TIMManagerExt.getInstance().getConversationList();
        List<TIMConversation> result = new ArrayList<>();
        for (TIMConversation conversation : list){
            if (conversation.getType() == TIMConversationType.System  || conversation.getType() == TIMConversationType.Group) continue;
            result.add(conversation);
            TIMConversationExt conversationExt = new TIMConversationExt(conversation);
            conversationExt.getMessage(1, null, new TIMValueCallBack<List<TIMMessage>>() {
                @Override
                public void onError(int i, String s) {
                    Log.e(TAG, "get message error" + s);
                }

                @Override
                public void onSuccess(List<TIMMessage> timMessages) {
                    if (timMessages.size() > 0) {
                        view.updateMessage(timMessages.get(0));
                    }

                }
            });

        }
        view.initView(result);
    }

    /**
     * 删除会话
     *
     * @param type 会话类型
     * @param id 会话对象id
     */
    public boolean delConversation(TIMConversationType type, String id){
        return TIMManagerExt.getInstance().deleteConversationAndLocalMsgs(type, id);
    }


}
