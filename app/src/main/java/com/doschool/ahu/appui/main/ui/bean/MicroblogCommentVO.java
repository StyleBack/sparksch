package com.doschool.ahu.appui.main.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.util.List;

/**
 * Created by X on 2018/9/4
 */
public class MicroblogCommentVO extends BaseModel {

    /**
     * 子评论列表
     */
    private List<MicroblogCommentVO> childCommentVOList;

    /**
     * 评论基本信息
     */
    private MicroblogCommentDO microblogCommentDO;

    /**
     * 评论者信息
     */
    private UserVO userVO;

    /**
     * 目标评论用户信息
     */
    private UserVO targetUserVO;

    /**
     * 是否点赞
     */
    private int isLike;
    /**
     * 点赞数量
     */
    private int likeCount;

    public List<MicroblogCommentVO> getChildCommentVOList() {
        return childCommentVOList;
    }

    public void setChildCommentVOList(List<MicroblogCommentVO> childCommentVOList) {
        this.childCommentVOList = childCommentVOList;
    }

    public MicroblogCommentDO getMicroblogCommentDO() {
        return microblogCommentDO;
    }

    public void setMicroblogCommentDO(MicroblogCommentDO microblogCommentDO) {
        this.microblogCommentDO = microblogCommentDO;
    }

    public UserVO getUserVO() {
        return userVO;
    }

    public void setUserVO(UserVO userVO) {
        this.userVO = userVO;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public UserVO getTargetUserVO() {
        return targetUserVO;
    }

    public void setTargetUserVO(UserVO targetUserVO) {
        this.targetUserVO = targetUserVO;
    }
}
