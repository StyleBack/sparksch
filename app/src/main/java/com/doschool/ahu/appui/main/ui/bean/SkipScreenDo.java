package com.doschool.ahu.appui.main.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;

/**
 * Created by X on 2018/12/5.
 */
public class SkipScreenDo extends BaseModel implements Serializable {
    private static final long serialVersionUID = 2336285584071325182L;

    private SkipCo data;

    public SkipCo getData() {
        return data;
    }

    public void setData(SkipCo data) {
        this.data = data;
    }

    public static class SkipCo {
        private JumpScreenRecordDOBean jumpScreenRecordDO;
        private int status;

        public JumpScreenRecordDOBean getJumpScreenRecordDO() {
            return jumpScreenRecordDO;
        }

        public void setJumpScreenRecordDO(JumpScreenRecordDOBean jumpScreenRecordDO) {
            this.jumpScreenRecordDO = jumpScreenRecordDO;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public static class JumpScreenRecordDOBean {
            private int id;
            private String schoolId;
            private int adminId;
            private String adminRealName;
            private String splashName;
            private String imageUrl;
            private int splashType;
            private int imageDoUrlType;
            private int buttonLeftDoUrlType;
            private int buttonRightDoUrlType;
            private String imageParams;
            private String buttonLeftParams;
            private String buttonRightParams;
            private String imageDoUrl;
            private String buttonLeftDoUrl;
            private String buttonRightDoUrl;
            private int displayRate;
            private int isForce;
            private int splashStatus;
            private String upTime;
            private String downTime;
            private int openNumber;
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

            public int getAdminId() {
                return adminId;
            }

            public void setAdminId(int adminId) {
                this.adminId = adminId;
            }

            public String getAdminRealName() {
                return adminRealName;
            }

            public void setAdminRealName(String adminRealName) {
                this.adminRealName = adminRealName;
            }

            public String getSplashName() {
                return splashName;
            }

            public void setSplashName(String splashName) {
                this.splashName = splashName;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public int getSplashType() {
                return splashType;
            }

            public void setSplashType(int splashType) {
                this.splashType = splashType;
            }

            public int getImageDoUrlType() {
                return imageDoUrlType;
            }

            public void setImageDoUrlType(int imageDoUrlType) {
                this.imageDoUrlType = imageDoUrlType;
            }

            public int getButtonLeftDoUrlType() {
                return buttonLeftDoUrlType;
            }

            public void setButtonLeftDoUrlType(int buttonLeftDoUrlType) {
                this.buttonLeftDoUrlType = buttonLeftDoUrlType;
            }

            public int getButtonRightDoUrlType() {
                return buttonRightDoUrlType;
            }

            public void setButtonRightDoUrlType(int buttonRightDoUrlType) {
                this.buttonRightDoUrlType = buttonRightDoUrlType;
            }

            public String getImageParams() {
                return imageParams;
            }

            public void setImageParams(String imageParams) {
                this.imageParams = imageParams;
            }

            public String getButtonLeftParams() {
                return buttonLeftParams;
            }

            public void setButtonLeftParams(String buttonLeftParams) {
                this.buttonLeftParams = buttonLeftParams;
            }

            public String getButtonRightParams() {
                return buttonRightParams;
            }

            public void setButtonRightParams(String buttonRightParams) {
                this.buttonRightParams = buttonRightParams;
            }

            public String getImageDoUrl() {
                return imageDoUrl;
            }

            public void setImageDoUrl(String imageDoUrl) {
                this.imageDoUrl = imageDoUrl;
            }

            public String getButtonLeftDoUrl() {
                return buttonLeftDoUrl;
            }

            public void setButtonLeftDoUrl(String buttonLeftDoUrl) {
                this.buttonLeftDoUrl = buttonLeftDoUrl;
            }

            public String getButtonRightDoUrl() {
                return buttonRightDoUrl;
            }

            public void setButtonRightDoUrl(String buttonRightDoUrl) {
                this.buttonRightDoUrl = buttonRightDoUrl;
            }

            public int getDisplayRate() {
                return displayRate;
            }

            public void setDisplayRate(int displayRate) {
                this.displayRate = displayRate;
            }

            public int getIsForce() {
                return isForce;
            }

            public void setIsForce(int isForce) {
                this.isForce = isForce;
            }

            public int getSplashStatus() {
                return splashStatus;
            }

            public void setSplashStatus(int splashStatus) {
                this.splashStatus = splashStatus;
            }

            public String getUpTime() {
                return upTime;
            }

            public void setUpTime(String upTime) {
                this.upTime = upTime;
            }

            public String getDownTime() {
                return downTime;
            }

            public void setDownTime(String downTime) {
                this.downTime = downTime;
            }

            public int getOpenNumber() {
                return openNumber;
            }

            public void setOpenNumber(int openNumber) {
                this.openNumber = openNumber;
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
