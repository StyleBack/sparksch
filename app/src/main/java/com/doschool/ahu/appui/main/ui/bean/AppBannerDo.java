package com.doschool.ahu.appui.main.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by X on 2018/10/8
 */
public class AppBannerDo extends BaseModel implements Serializable{
    private static final long serialVersionUID = 9004410522176429798L;

    private List<BannerData> data;

    public List<BannerData> getData() {
        return data;
    }

    public void setData(List<BannerData> data) {
        this.data = data;
    }

    public static class BannerData {

        public static final int TYPE_NORMAL=1;
        public static final int TYPE_HOT=2;
        public static final int TYPE_TOPIC=3;
        public static final int TYPE_LINK=4;

        /**
         * id : 475
         * bannerName : 蚂蚁在路上音乐节
         * topicName : 蚂蚁在路上音乐节
         * image : http://opko3tbv1.bkt.clouddn.com/Fu5T6Ff1JVqI6OCEBqf-EwQkdHNa
         * imageDourl : {"paramList":[],"action":"do://jump/topic/one"}
         * fastbarDourl : {"action":"do://jump/blog/add"}
         * endtime : 2018-09-14 11:51:08
         * sort : 1
         * schoolId : 1
         * type : 3
         * gmtCreate : 2018-07-26 11:51:39
         * gmtModified : 2018-07-26 11:51:39
         * isDeleted : 0
         * topicId : 4414
         */

        private int id;
        /**
         * banner名称
         */
        private String bannerName;
        /**
         * 跳转话题时,话题名
         */
        private String topicName;
        /**
         * banner图片地址
         */
        private String image;
        /**
         * 图片doUrl
         */
        private String imageDourl;
        /**
         * fastbar对应的doUrl
         */
        private String fastbarDourl;
        /**
         * 下线时间
         */
        private String endtime;
        /**
         * 排序字段,暂定
         */
        private int sort;
        private int schoolId;
        /**
         * 1->正常,2->热门,3->话题,4->链接
         */
        private int type;
        private String gmtCreate;
        private String gmtModified;
        private int isDeleted;
        private int topicId;
        private MicroblogVO.DataBean data;

        public MicroblogVO.DataBean getData() {
            return data;
        }

        public void setData(MicroblogVO.DataBean data) {
            this.data = data;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBannerName() {
            return bannerName;
        }

        public void setBannerName(String bannerName) {
            this.bannerName = bannerName;
        }

        public String getTopicName() {
            return topicName;
        }

        public void setTopicName(String topicName) {
            this.topicName = topicName;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImageDourl() {
            return imageDourl;
        }

        public void setImageDourl(String imageDourl) {
            this.imageDourl = imageDourl;
        }

        public String getFastbarDourl() {
            return fastbarDourl;
        }

        public void setFastbarDourl(String fastbarDourl) {
            this.fastbarDourl = fastbarDourl;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(int schoolId) {
            this.schoolId = schoolId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public String getGmtModified() {
            return gmtModified;
        }

        public void setGmtModified(String gmtModified) {
            this.gmtModified = gmtModified;
        }

        public int getIsDeleted() {
            return isDeleted;
        }

        public void setIsDeleted(int isDeleted) {
            this.isDeleted = isDeleted;
        }

        public int getTopicId() {
            return topicId;
        }

        public void setTopicId(int topicId) {
            this.topicId = topicId;
        }
    }
}
