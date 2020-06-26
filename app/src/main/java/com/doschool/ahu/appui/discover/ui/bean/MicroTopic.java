package com.doschool.ahu.appui.discover.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by X on 2018/9/23
 */
public class MicroTopic extends BaseModel implements Serializable {
    private static final long serialVersionUID = 519938286022972778L;

    private List<McData> data;

    public List<McData> getData() {
        return data;
    }

    public void setData(List<McData> data) {
        this.data = data;
    }

    public static class McData{

        private MicroblogTopicDO microblogTopicDO;

        public MicroblogTopicDO getMicroblogTopicDO() {
            return microblogTopicDO;
        }

        public void setMicroblogTopicDO(MicroblogTopicDO microblogTopicDO) {
            this.microblogTopicDO = microblogTopicDO;
        }
        public int getMicroblogNum() {
            return microblogNum;
        }

        public void setMicroblogNum(int microblogNum) {
            this.microblogNum = microblogNum;
        }

        private int microblogNum;
    }
}
