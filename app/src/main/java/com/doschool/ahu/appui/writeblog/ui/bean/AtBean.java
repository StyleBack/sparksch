package com.doschool.ahu.appui.writeblog.ui.bean;

import com.doschool.ahu.appui.main.ui.bean.SingleUserVO;
import com.doschool.ahu.appui.main.ui.bean.UserVO;
import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by X on 2018/9/18
 */
public class AtBean extends BaseModel implements Serializable {
    private static final long serialVersionUID = -2701634227399401465L;

    private List<SingleUserVO> data;

    public List<SingleUserVO> getData() {
        return data;
    }

    public void setData(List<SingleUserVO> data) {
        this.data = data;
    }

    private List<AtData> data2;

    public List<AtData> getData2() {
        return data2;
    }

    public void setData2(List<AtData> data2) {
        this.data2 = data2;
    }

    public static class AtData{
        private UserVO userDO;
        private String departName;
        private String majorName;
        private int lollipopCount;
        private int userBlogCount;
        private int ugcContentLikeCount;
        private int followingCount;
        private int followersCount;
        private int followState;
        private String headThumbnail;

        public String getHeadThumbnail() {
            return headThumbnail;
        }

        public void setHeadThumbnail(String headThumbnail) {
            this.headThumbnail = headThumbnail;
        }

        public UserVO getUserDO() {
            return userDO;
        }

        public void setUserDO(UserVO userDO) {
            this.userDO = userDO;
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

        public int getUgcContentLikeCount() {
            return ugcContentLikeCount;
        }

        public void setUgcContentLikeCount(int ugcContentLikeCount) {
            this.ugcContentLikeCount = ugcContentLikeCount;
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

        public int getFollowState() {
            return followState;
        }

        public void setFollowState(int followState) {
            this.followState = followState;
        }
    }
}
