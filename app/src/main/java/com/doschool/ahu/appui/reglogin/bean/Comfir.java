package com.doschool.ahu.appui.reglogin.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by X on 2018/9/17
 */
public class Comfir extends BaseModel implements Serializable {
    private static final long serialVersionUID = -5790066248055269109L;


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private int id;
        private int schoolId;
        private String systemName;
        private String systemUrl;
        private String tipFirst;
        private String tipSecond;
        private String tipFail;
        private int status;
        private int needCaptcha;
        private int userType;
        private String gmtCreate;
        private String gmtModified;
        private int isDeleted;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(int schoolId) {
            this.schoolId = schoolId;
        }

        public String getSystemName() {
            return systemName;
        }

        public void setSystemName(String systemName) {
            this.systemName = systemName;
        }

        public String getSystemUrl() {
            return systemUrl;
        }

        public void setSystemUrl(String systemUrl) {
            this.systemUrl = systemUrl;
        }

        public String getTipFirst() {
            return tipFirst;
        }

        public void setTipFirst(String tipFirst) {
            this.tipFirst = tipFirst;
        }

        public String getTipSecond() {
            return tipSecond;
        }

        public void setTipSecond(String tipSecond) {
            this.tipSecond = tipSecond;
        }

        public String getTipFail() {
            return tipFail;
        }

        public void setTipFail(String tipFail) {
            this.tipFail = tipFail;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getNeedCaptcha() {
            return needCaptcha;
        }

        public void setNeedCaptcha(int needCaptcha) {
            this.needCaptcha = needCaptcha;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
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

        @Override
        public String toString() {
            return systemName;
        }
    }

}
