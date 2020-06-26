package com.doschool.ahu.appui.reglogin.bean;

import com.doschool.ahu.appui.main.ui.bean.UserVO;
import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;

/**
 * Created by X on 2018/9/10
 */
public class LoginVO extends BaseModel {
    private static final long serialVersionUID = -8687657336845969917L;

    private LoginData data;

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

    public static class LoginData implements Serializable{
        private static final long serialVersionUID = 2481410916976551631L;
        private int id=1;
        private UserVO userDO;
        /**
         * 用户账号信息
         */
        private UserVO userAccountDO;
        /**
         * 头像缩略图
         */
        private String headThumbnail;
        /**
         * 院系名称
         */
        private String departName;
        /**
         * 专业名称
         */
        private String majorName;
        /**
         * 棒棒糖数量
         */
        private int lollipopCount;
        /**
         * 微博数量
         */
        private int userBlogCount;
        /**
         * 关注数量
         */
        private int followingCount;
        /**
         * 粉丝数量
         */
        private int followersCount;
        /**
         * 备注(双向关系时存在)
         */
        private String remarkName;
        /**
         * 关注状态(双向关系时存在,见枚举FollowStateEnumm)
         */
        private int followState;
        /**
         * 微博、评论被点赞数量
         */
        private int ugcContentLikeCount;

        /**
         * 0->未提交,1->待审核,2->已通过,3->已拒绝
         */
        private int handleStatus;

        public int getHandleStatus() {
            return handleStatus;
        }

        public void setHandleStatus(int handleStatus) {
            this.handleStatus = handleStatus;
        }

        public LoginData() {
        }

        public UserVO getUserDO() {
            return userDO;
        }

        public void setUserDO(UserVO userDO) {
            this.userDO = userDO;
        }

        public UserVO getUserAccountDO() {
            return userAccountDO;
        }

        public void setUserAccountDO(UserVO userAccountDO) {
            this.userAccountDO = userAccountDO;
        }

        public String getHeadThumbnail() {
            return headThumbnail;
        }

        public void setHeadThumbnail(String headThumbnail) {
            this.headThumbnail = headThumbnail;
        }

        public String getDepartName() {
            return departName;
        }

        public void setDepartName(String departName) {
            this.departName = departName;
        }

        public String getMajorName() {
            return majorName;
        }

        public void setMajorName(String majorName) {
            this.majorName = majorName;
        }

        public int getLollipopCount() {
            return lollipopCount;
        }

        public void setLollipopCount(int lollipopCount) {
            this.lollipopCount = lollipopCount;
        }

        public int getUserBlogCount() {
            return userBlogCount;
        }

        public void setUserBlogCount(int userBlogCount) {
            this.userBlogCount = userBlogCount;
        }

        public int getFollowingCount() {
            return followingCount;
        }

        public void setFollowingCount(int followingCount) {
            this.followingCount = followingCount;
        }

        public int getFollowersCount() {
            return followersCount;
        }

        public void setFollowersCount(int followersCount) {
            this.followersCount = followersCount;
        }

        public String getRemarkName() {
            return remarkName;
        }

        public void setRemarkName(String remarkName) {
            this.remarkName = remarkName;
        }

        public int getFollowState() {
            return followState;
        }

        public void setFollowState(int followState) {
            this.followState = followState;
        }

        public int getUgcContentLikeCount() {
            return ugcContentLikeCount;
        }

        public void setUgcContentLikeCount(int ugcContentLikeCount) {
            this.ugcContentLikeCount = ugcContentLikeCount;
        }
    }
}
