package com.doschool.ahu.appui.main.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;

/**
 * Created by X on 2018/11/12
 */
public class MicroblogPlaceDO extends BaseModel implements Serializable {
    private static final long serialVersionUID = -7174003467864819119L;
    /**
     * 主键,其它表中的user_id
     */
    private int id;
    /**
     * 地标id
     */
    private String placeId;
    /**
     * 地标名称
     */
    private String placeName;
    /**
     * schoolId
     */
    private int schoolId;
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

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
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
