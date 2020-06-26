package com.doschool.ahu.appui.mine.ui.bean;

import com.doschool.ahu.appui.discover.ui.bean.MicroblogTopicDO;
import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by X on 2018/9/25
 */
public class MineTopicBean extends BaseModel implements Serializable {
    private static final long serialVersionUID = -6538644108528139059L;

    private List<TD> data;

    public List<TD> getData() {
        return data;
    }

    public void setData(List<TD> data) {
        this.data = data;
    }

    public static class TD{

        private int microblogNum;
        private MicroblogTopicDO microblogTopicDO;

        public int getMicroblogNum() {
            return microblogNum;
        }

        public void setMicroblogNum(int microblogNum) {
            this.microblogNum = microblogNum;
        }

        public MicroblogTopicDO getMicroblogTopicDO() {
            return microblogTopicDO;
        }

        public void setMicroblogTopicDO(MicroblogTopicDO microblogTopicDO) {
            this.microblogTopicDO = microblogTopicDO;
        }
    }
}
