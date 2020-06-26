package com.doschool.ahu.appui.main.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by X on 2018/10/25
 */
public class VerifyBean extends BaseModel implements Serializable {
    private static final long serialVersionUID = 3357813473760207783L;
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private UserVerifyDOBean userVerifyDO;
        private String waitTime;
        private int isRed;
        private List<String> userVerifyPictureDOList;

        public UserVerifyDOBean getUserVerifyDO() {
            return userVerifyDO;
        }

        public void setUserVerifyDO(UserVerifyDOBean userVerifyDO) {
            this.userVerifyDO = userVerifyDO;
        }

        public String getWaitTime() {
            return waitTime;
        }

        public void setWaitTime(String waitTime) {
            this.waitTime = waitTime;
        }

        public int getIsRed() {
            return isRed;
        }

        public void setIsRed(int isRed) {
            this.isRed = isRed;
        }

        public List<String> getUserVerifyPictureDOList() {
            return userVerifyPictureDOList;
        }

        public void setUserVerifyPictureDOList(List<String> userVerifyPictureDOList) {
            this.userVerifyPictureDOList = userVerifyPictureDOList;
        }

        public static class UserVerifyDOBean {
            /**
             * id : 195
             * userId : 60351
             * schoolId : 1
             * departId : 3
             * majorId : 102
             * adminId : -1
             * adminRealName :
             * userType : 1
             * userVerifyType : -1
             * handleStatus : 1
             * funcId : X20181025
             * noticeId : -1
             * otherId : -1
             * realName : 雨雨雨
             * phone : 15755167748
             * sex : 0
             * urgeNum : 0
             * submitTime : 2018-10-25 11:05:08
             * handleTime : 2018-10-25 11:05:08
             * gmtCreate : 2018-10-25 11:05:08
             * gmtModified : 2018-10-25 11:05:08
             * isDeleted : 0
             */

            private int id;
            private int userId;
            private int schoolId;
            private int departId;
            private int majorId;
            private int adminId;
            private String adminRealName;
            private int userType;
            private int userVerifyType;
            private int handleStatus;
            private String funcId;
            private String noticeId;
            private String otherId;
            private String realName;
            private String phone;
            private int sex;
            private int urgeNum;
            private String submitTime;
            private String handleTime;
            private String gmtCreate;
            private String gmtModified;
            private int isDeleted;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getSchoolId() {
                return schoolId;
            }

            public void setSchoolId(int schoolId) {
                this.schoolId = schoolId;
            }

            public int getDepartId() {
                return departId;
            }

            public void setDepartId(int departId) {
                this.departId = departId;
            }

            public int getMajorId() {
                return majorId;
            }

            public void setMajorId(int majorId) {
                this.majorId = majorId;
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

            public int getUserType() {
                return userType;
            }

            public void setUserType(int userType) {
                this.userType = userType;
            }

            public int getUserVerifyType() {
                return userVerifyType;
            }

            public void setUserVerifyType(int userVerifyType) {
                this.userVerifyType = userVerifyType;
            }

            public int getHandleStatus() {
                return handleStatus;
            }

            public void setHandleStatus(int handleStatus) {
                this.handleStatus = handleStatus;
            }

            public String getFuncId() {
                return funcId;
            }

            public void setFuncId(String funcId) {
                this.funcId = funcId;
            }

            public String getNoticeId() {
                return noticeId;
            }

            public void setNoticeId(String noticeId) {
                this.noticeId = noticeId;
            }

            public String getOtherId() {
                return otherId;
            }

            public void setOtherId(String otherId) {
                this.otherId = otherId;
            }

            public String getRealName() {
                return realName;
            }

            public void setRealName(String realName) {
                this.realName = realName;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public int getUrgeNum() {
                return urgeNum;
            }

            public void setUrgeNum(int urgeNum) {
                this.urgeNum = urgeNum;
            }

            public String getSubmitTime() {
                return submitTime;
            }

            public void setSubmitTime(String submitTime) {
                this.submitTime = submitTime;
            }

            public String getHandleTime() {
                return handleTime;
            }

            public void setHandleTime(String handleTime) {
                this.handleTime = handleTime;
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
