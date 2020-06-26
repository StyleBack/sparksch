package com.doschool.ahu.appui.main.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;

/**
 * Created by X on 2019/1/12.
 */
public class MicroblogBrowseRecordDO extends BaseModel implements Serializable {
    private static final long serialVersionUID = -7478203652572169437L;

    /**
     * 主键
     */
    private int id;

    /**
     * schoolId
     */
    private int schoolId;

    /**
     * userId
     */
    private int userId;

    /**
     * blogId
     */
    private int blogId;

    /**
     * 浏览次数
     */
    private int browseNum;

    /**
     * 最近一次的浏览时间
     */
    private String lastBrowseTime;

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
    private Integer isDeleted;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public int getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(int browseNum) {
        this.browseNum = browseNum;
    }

    public String getLastBrowseTime() {
        return lastBrowseTime;
    }

    public void setLastBrowseTime(String lastBrowseTime) {
        this.lastBrowseTime = lastBrowseTime;
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

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
