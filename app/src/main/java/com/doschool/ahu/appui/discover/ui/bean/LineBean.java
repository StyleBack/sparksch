package com.doschool.ahu.appui.discover.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by X on 2018/9/27
 */
public class LineBean extends BaseModel implements Serializable{
    private static final long serialVersionUID = -1450474450274529853L;

    private List<LineDt> data;

    public List<LineDt> getData() {
        return data;
    }

    public void setData(List<LineDt> data) {
        this.data = data;
    }

    public static class LineDt{
        private HeadLineDo headlineArticleDO;
        private String nickName;
        private String headImage;
        private String authorId;

        public String getAuthorId() {
            return authorId;
        }

        public void setAuthorId(String authorId) {
            this.authorId = authorId;
        }

        public HeadLineDo getHeadlineArticleDO() {
            return headlineArticleDO;
        }

        public void setHeadlineArticleDO(HeadLineDo headlineArticleDO) {
            this.headlineArticleDO = headlineArticleDO;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeadImage() {
            return headImage;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }

    }

}
