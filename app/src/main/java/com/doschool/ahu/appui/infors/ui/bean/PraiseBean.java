package com.doschool.ahu.appui.infors.ui.bean;

import com.doschool.ahu.appui.main.ui.bean.MicroblogCommentDO;
import com.doschool.ahu.appui.main.ui.bean.MicroblogMainDO;
import com.doschool.ahu.appui.main.ui.bean.UserVO;
import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by X on 2018/9/22
 */
public class PraiseBean extends BaseModel implements Serializable {

    private static final long serialVersionUID = 95522836475728612L;

    private List<PraData> data;

    public List<PraData> getData() {
        return data;
    }

    public void setData(List<PraData> data) {
        this.data = data;
    }

    public static class PraData{
        private int userId;
        private int hostId;
        private int messageType;
        private int messageId;
        private String createTime;
        private MicroblogMainDO originalObject;
        private MicroblogCommentDO newObject;
        private UserVO fromUser;

        public int getMessageId() {
            return messageId;
        }

        public void setMessageId(int messageId) {
            this.messageId = messageId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getHostId() {
            return hostId;
        }

        public void setHostId(int hostId) {
            this.hostId = hostId;
        }

        public int getMessageType() {
            return messageType;
        }

        public void setMessageType(int messageType) {
            this.messageType = messageType;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public MicroblogMainDO getOriginalObject() {
            return originalObject;
        }

        public void setOriginalObject(MicroblogMainDO originalObject) {
            this.originalObject = originalObject;
        }

        public MicroblogCommentDO getNewObject() {
            return newObject;
        }

        public void setNewObject(MicroblogCommentDO newObject) {
            this.newObject = newObject;
        }

        public UserVO getFromUser() {
            return fromUser;
        }

        public void setFromUser(UserVO fromUser) {
            this.fromUser = fromUser;
        }
    }
}
