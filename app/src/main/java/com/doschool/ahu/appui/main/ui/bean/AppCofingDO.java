package com.doschool.ahu.appui.main.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;

/**
 * Created by X on 2018/9/28
 */
public class AppCofingDO extends BaseModel implements Serializable {
    private static final long serialVersionUID = -5924279113354878086L;

    private AppDO data;

    public AppDO getData() {
        return data;
    }

    public void setData(AppDO data) {
        this.data = data;
    }

    public static class AppDO implements  Serializable{
        private static final long serialVersionUID = -6944533196798626999L;
        private int id;
        private int schoolId;
        /**
         * webview跳转rootUrl
         */
        private String webviewRootUrl;
        /**
         * 棒棒糖玩法介绍地址
         */
        private String lollipopIntroUrl;
        /**
         * appLogo地址
         */
        private String applogoUrl;
        /**
         * 棒棒糖历史记录
         */
        private String lollipopHistoryUrl;
        /**
         * app使用协议地址
         */
        private String agreementUrl;
        /**
         * 关于我们地址
         */
        private String aboutusUrl;
        /**
         * app名称
         */
        private String appName;
        /**
         * android下载地址
         */
        private String androidDownloadUrl;
        /**
         * ios appstore地址
         */
        private String iosDownloadUrl;
        /**
         * 创建时间
         */
        private String gmtCreate;
        /**
         * 修改时间
         */
        private String gmtModified;
        /**
         * 1表示删除,0表示未删除
         */
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

        public String getWebviewRootUrl() {
            return webviewRootUrl;
        }

        public void setWebviewRootUrl(String webviewRootUrl) {
            this.webviewRootUrl = webviewRootUrl;
        }

        public String getLollipopIntroUrl() {
            return lollipopIntroUrl;
        }

        public void setLollipopIntroUrl(String lollipopIntroUrl) {
            this.lollipopIntroUrl = lollipopIntroUrl;
        }

        public String getApplogoUrl() {
            return applogoUrl;
        }

        public void setApplogoUrl(String applogoUrl) {
            this.applogoUrl = applogoUrl;
        }

        public String getLollipopHistoryUrl() {
            return lollipopHistoryUrl;
        }

        public void setLollipopHistoryUrl(String lollipopHistoryUrl) {
            this.lollipopHistoryUrl = lollipopHistoryUrl;
        }

        public String getAgreementUrl() {
            return agreementUrl;
        }

        public void setAgreementUrl(String agreementUrl) {
            this.agreementUrl = agreementUrl;
        }

        public String getAboutusUrl() {
            return aboutusUrl;
        }

        public void setAboutusUrl(String aboutusUrl) {
            this.aboutusUrl = aboutusUrl;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAndroidDownloadUrl() {
            return androidDownloadUrl;
        }

        public void setAndroidDownloadUrl(String androidDownloadUrl) {
            this.androidDownloadUrl = androidDownloadUrl;
        }

        public String getIosDownloadUrl() {
            return iosDownloadUrl;
        }

        public void setIosDownloadUrl(String iosDownloadUrl) {
            this.iosDownloadUrl = iosDownloadUrl;
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
