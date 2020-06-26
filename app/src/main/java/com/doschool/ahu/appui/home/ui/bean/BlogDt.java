package com.doschool.ahu.appui.home.ui.bean;

import com.doschool.ahu.appui.main.ui.bean.MicroblogCommentVO;
import com.doschool.ahu.appui.main.ui.bean.MicroblogMainDO;
import com.doschool.ahu.appui.main.ui.bean.MicroblogPlaceDO;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVO;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVoteOptionsDto;
import com.doschool.ahu.appui.main.ui.bean.UserVO;
import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by X on 2018/9/14
 */
public class BlogDt extends BaseModel implements Serializable {
    private static final long serialVersionUID = 736906873699180690L;

    public BlogDtBean getData() {
        return data;
    }

    public void setData(BlogDtBean data) {
        this.data = data;
    }

    private BlogDtBean data;

    public static class BlogDtBean {
        /**
         * 微博基础信息
         */
        private MicroblogMainDO microblogMainDO;

        /**
         * 微博分享信息
         */
        private MicroblogVO.DataBean.MicroblogShareDO microblogShareDO;

        public static class MicroblogShareDO{

        }

        /**
         * 最新三条评论
         */
        private List<MicroblogCommentVO> microblogCommentVOList;

        /**
         * 用户Vo
         */
        private UserVO userVO;
        /**
         * 地标信息
         */
        private MicroblogPlaceDO microblogPlaceDO;

        /**
         * 图片列表
         */
        private List<String> pictureList;
        /**
         * 缩略图列表
         */
        private List<String> thumbnailList;
        /**
         * 视频地址
         */
        private String videoName;
        /**
         * 视频缩略图
         */
        private String videoThumbnail;
        /**
         * 评论数量
         */
        private int commentCount;
        /**
         * 点赞数量
         */
        private int likeCount;
        /**
         * 话题名
         */
        private String topicName;
        /**
         * 是否点赞
         */
        private int isLike;
        /**
         * 新动静的文本(评论或赞了这条动态)
         */
        private String upText;
        /**
         * 最后操作人
         */
        private UserVO upUser;

        //热门排行榜
        private int hotRank;

        //1->已收藏，0->未收藏
        private int isCollect;
        //是否投票
        private boolean vote;
        private List<MicroblogVoteOptionsDto> microblogVoteOptionsDtoList;

        public boolean isVote() {
            return vote;
        }

        public void setVote(boolean vote) {
            this.vote = vote;
        }

        public List<MicroblogVoteOptionsDto> getMicroblogVoteOptionsDtoList() {
            return microblogVoteOptionsDtoList;
        }

        public void setMicroblogVoteOptionsDtoList(List<MicroblogVoteOptionsDto> microblogVoteOptionsDtoList) {
            this.microblogVoteOptionsDtoList = microblogVoteOptionsDtoList;
        }

        public MicroblogPlaceDO getMicroblogPlaceDO() {
            return microblogPlaceDO;
        }

        public void setMicroblogPlaceDO(MicroblogPlaceDO microblogPlaceDO) {
            this.microblogPlaceDO = microblogPlaceDO;
        }

        public MicroblogMainDO getMicroblogMainDO() {
            return microblogMainDO;
        }

        public void setMicroblogMainDO(MicroblogMainDO microblogMainDO) {
            this.microblogMainDO = microblogMainDO;
        }

        public MicroblogVO.DataBean.MicroblogShareDO getMicroblogShareDO() {
            return microblogShareDO;
        }

        public void setMicroblogShareDO(MicroblogVO.DataBean.MicroblogShareDO microblogShareDO) {
            this.microblogShareDO = microblogShareDO;
        }

        public List<MicroblogCommentVO> getMicroblogCommentVOList() {
            return microblogCommentVOList;
        }

        public void setMicroblogCommentVOList(List<MicroblogCommentVO> microblogCommentVOList) {
            this.microblogCommentVOList = microblogCommentVOList;
        }

        public UserVO getUserVO() {
            return userVO;
        }

        public void setUserVO(UserVO userVO) {
            this.userVO = userVO;
        }

        public List<String> getPictureList() {
            return pictureList;
        }

        public void setPictureList(List<String> pictureList) {
            this.pictureList = pictureList;
        }

        public List<String> getThumbnailList() {
            return thumbnailList;
        }

        public void setThumbnailList(List<String> thumbnailList) {
            this.thumbnailList = thumbnailList;
        }

        public String getVideoName() {
            return videoName;
        }

        public void setVideoName(String videoName) {
            this.videoName = videoName;
        }

        public String getVideoThumbnail() {
            return videoThumbnail;
        }

        public void setVideoThumbnail(String videoThumbnail) {
            this.videoThumbnail = videoThumbnail;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public String getTopicName() {
            return topicName;
        }

        public void setTopicName(String topicName) {
            this.topicName = topicName;
        }

        public int getIsLike() {
            return isLike;
        }

        public void setIsLike(int isLike) {
            this.isLike = isLike;
        }

        public String getUpText() {
            return upText;
        }

        public void setUpText(String upText) {
            this.upText = upText;
        }

        public UserVO getUpUser() {
            return upUser;
        }

        public void setUpUser(UserVO upUser) {
            this.upUser = upUser;
        }

        public int getHotRank() {
            return hotRank;
        }

        public void setHotRank(int hotRank) {
            this.hotRank = hotRank;
        }

        public int getIsCollect() {
            return isCollect;
        }

        public void setIsCollect(int isCollect) {
            this.isCollect = isCollect;
        }
    }
}
