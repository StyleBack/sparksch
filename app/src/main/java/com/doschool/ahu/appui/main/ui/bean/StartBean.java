package com.doschool.ahu.appui.main.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;

/**
 * Created by X on 2018/10/10
 */
public class StartBean extends BaseModel implements Serializable {
    private static final long serialVersionUID = 587319684859649345L;

    /**
     * data : {"id":1,"name":"小安闪屏","pictureUrl":"http://cdn.dobell.me/FpBj-94-b6W_ku1nkByTnSmJR3xP","schoolId":1,"doUrl":"{\"paramList\":[\"https://www.baidu.com\"],\"action\":\"do://jump/web/one\"}","gmtCreate":"2018-09-29 10:40:13","gmtModified":"2018-10-08 14:26:10","isDeleted":0}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * name : 小安闪屏
         * pictureUrl : http://cdn.dobell.me/FpBj-94-b6W_ku1nkByTnSmJR3xP
         * schoolId : 1
         * doUrl : {"paramList":["https://www.baidu.com"],"action":"do://jump/web/one"}
         * gmtCreate : 2018-09-29 10:40:13
         * gmtModified : 2018-10-08 14:26:10
         * isDeleted : 0
         */

        private int id;
        private String name;
        private String pictureUrl;
        private int schoolId;
        private String doUrl;
        private String gmtCreate;
        private String gmtModified;
        private int isDeleted;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public int getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(int schoolId) {
            this.schoolId = schoolId;
        }

        public String getDoUrl() {
            return doUrl;
        }

        public void setDoUrl(String doUrl) {
            this.doUrl = doUrl;
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
    }
}
