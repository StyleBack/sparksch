package com.doschool.ahu.appui.main.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;

public class UpDateApp extends BaseModel implements Serializable {
    private static final long serialVersionUID = -1733575559158281347L;

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private AppVersionDOBean appVersionDO;
        /**
         * 1->不提示,2->普通提示,3->强制更新提示
         */
        private int status;

        public AppVersionDOBean getAppVersionDO() {
            return appVersionDO;
        }

        public void setAppVersionDO(AppVersionDOBean appVersionDO) {
            this.appVersionDO = appVersionDO;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public static class AppVersionDOBean {
            private int id;
            private int schoolId;
            private String name;
            private String title;
            private String updateInfo;
            private String version;
            private int deviceType;
            private int deviceVersion;
            private String downloadUrl;
            private String appSize;
            private int isDayRemind;
            private int isForce;
            private String upTime;
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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUpdateInfo() {
                return updateInfo;
            }

            public void setUpdateInfo(String updateInfo) {
                this.updateInfo = updateInfo;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public int getDeviceType() {
                return deviceType;
            }

            public void setDeviceType(int deviceType) {
                this.deviceType = deviceType;
            }

            public int getDeviceVersion() {
                return deviceVersion;
            }

            public void setDeviceVersion(int deviceVersion) {
                this.deviceVersion = deviceVersion;
            }

            public String getDownloadUrl() {
                return downloadUrl;
            }

            public void setDownloadUrl(String downloadUrl) {
                this.downloadUrl = downloadUrl;
            }

            public String getAppSize() {
                return appSize;
            }

            public void setAppSize(String appSize) {
                this.appSize = appSize;
            }

            public int getIsDayRemind() {
                return isDayRemind;
            }

            public void setIsDayRemind(int isDayRemind) {
                this.isDayRemind = isDayRemind;
            }

            public int getIsForce() {
                return isForce;
            }

            public void setIsForce(int isForce) {
                this.isForce = isForce;
            }

            public String getUpTime() {
                return upTime;
            }

            public void setUpTime(String upTime) {
                this.upTime = upTime;
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
}
