package com.doschool.ahu.appui.infors.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;
import java.util.List;

public class PushRecordBean extends BaseModel implements Serializable {
    private static final long serialVersionUID = 236558854764237232L;

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private int id;
        private String schoolId;
        private String superAdminId;
        private int adminId;
        private String imageUrl;
        private String text;
        private String title;
        private String ticker;
        private String extra;
        private int type;
        private int pushNumber;
        private int openNumber;
        private int openRate;
        private int pushStatus;
        private int checkStatus;
        private String checkMessage;
        private String iosMsgId;
        private String androidMsgId;
        private String pushDate;
        private String gmtCreate;
        private String gmtModified;
        private int isDeleted;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public String getSuperAdminId() {
            return superAdminId;
        }

        public void setSuperAdminId(String superAdminId) {
            this.superAdminId = superAdminId;
        }

        public int getAdminId() {
            return adminId;
        }

        public void setAdminId(int adminId) {
            this.adminId = adminId;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTicker() {
            return ticker;
        }

        public void setTicker(String ticker) {
            this.ticker = ticker;
        }

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getPushNumber() {
            return pushNumber;
        }

        public void setPushNumber(int pushNumber) {
            this.pushNumber = pushNumber;
        }

        public int getOpenNumber() {
            return openNumber;
        }

        public void setOpenNumber(int openNumber) {
            this.openNumber = openNumber;
        }

        public int getOpenRate() {
            return openRate;
        }

        public void setOpenRate(int openRate) {
            this.openRate = openRate;
        }

        public int getPushStatus() {
            return pushStatus;
        }

        public void setPushStatus(int pushStatus) {
            this.pushStatus = pushStatus;
        }

        public int getCheckStatus() {
            return checkStatus;
        }

        public void setCheckStatus(int checkStatus) {
            this.checkStatus = checkStatus;
        }

        public String getCheckMessage() {
            return checkMessage;
        }

        public void setCheckMessage(String checkMessage) {
            this.checkMessage = checkMessage;
        }

        public String getIosMsgId() {
            return iosMsgId;
        }

        public void setIosMsgId(String iosMsgId) {
            this.iosMsgId = iosMsgId;
        }

        public String getAndroidMsgId() {
            return androidMsgId;
        }

        public void setAndroidMsgId(String androidMsgId) {
            this.androidMsgId = androidMsgId;
        }

        public String getPushDate() {
            return pushDate;
        }

        public void setPushDate(String pushDate) {
            this.pushDate = pushDate;
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
