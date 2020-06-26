package com.doschool.ahu.appui.main.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

/**
 * Created by X on 2018/9/4
 */
public class MicroblogCommentDO extends BaseModel {
    private int id;
    /**
     * 用户id
     */
    private int userId;
    /**
     * 评论内容
     */
    private String comment;
    /**
     * 微博id
     */
    private int hostBlogId;
    /**
     * 评论目标用户id
     */
    private int targetUserId;
    /**
     * 目标评论id
     */
    private int targetCommentId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getHostBlogId() {
        return hostBlogId;
    }

    public void setHostBlogId(int hostBlogId) {
        this.hostBlogId = hostBlogId;
    }

    public int getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(int targetUserId) {
        this.targetUserId = targetUserId;
    }

    public int getTargetCommentId() {
        return targetCommentId;
    }

    public void setTargetCommentId(int targetCommentId) {
        this.targetCommentId = targetCommentId;
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
