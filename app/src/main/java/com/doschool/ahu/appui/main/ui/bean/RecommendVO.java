package com.doschool.ahu.appui.main.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by X on 2019/1/21.
 */
public class RecommendVO extends BaseModel implements Serializable {
    private static final long serialVersionUID = -1761198316038353451L;

    private Recom data;

    public Recom getData() {
        return data;
    }

    public void setData(Recom data) {
        this.data = data;
    }

    public static class Recom{
        private String message;
        private List<RecomList> microblogVOList;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<RecomList> getMicroblogVOList() {
            return microblogVOList;
        }

        public void setMicroblogVOList(List<RecomList> microblogVOList) {
            this.microblogVOList = microblogVOList;
        }
    }
}
