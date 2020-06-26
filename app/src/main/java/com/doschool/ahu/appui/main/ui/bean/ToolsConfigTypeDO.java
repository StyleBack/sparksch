package com.doschool.ahu.appui.main.ui.bean;

/**
 * Created by X on 2019/1/12.
 */
public class ToolsConfigTypeDO {

    private int id;
    private int schoolId;
    private String typeName;
    private int typeSort;
    private int userType;
    private int deviceVersion;

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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeSort() {
        return typeSort;
    }

    public void setTypeSort(int typeSort) {
        this.typeSort = typeSort;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(int deviceVersion) {
        this.deviceVersion = deviceVersion;
    }
}
