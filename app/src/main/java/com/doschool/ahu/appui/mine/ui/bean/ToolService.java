package com.doschool.ahu.appui.mine.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by X on 2018/9/11
 */
public class ToolService extends BaseModel implements Serializable {
    private static final long serialVersionUID = -4048377664683199116L;

    private List<ToolBean> data;

    public List<ToolBean> getData() {
        return data;
    }

    public void setData(List<ToolBean> data) {
        this.data = data;
    }

    public static class ToolBean{
        private int id;
        private int schoolId;
        private int userType;
        private int deviceType;
        private String name;
        private int version;
        private int page;
        private int isLarge;
        private String icon;
        private int isNative;
        private int visiable;
        private int available;
        private String text;
        private String unavailableText;
        private int index;
        private String doUrl;
        private String color;
        private int deviceVersion;
        private int isTest;
        private String gmtCreate;
        private String gmtModified;
        private int isDeleted;

        public String getDoUrl() {
            return doUrl;
        }

        public void setDoUrl(String doUrl) {
            this.doUrl = doUrl;
        }

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

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public int getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(int deviceType) {
            this.deviceType = deviceType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getIsLarge() {
            return isLarge;
        }

        public void setIsLarge(int isLarge) {
            this.isLarge = isLarge;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getIsNative() {
            return isNative;
        }

        public void setIsNative(int isNative) {
            this.isNative = isNative;
        }

        public int getVisiable() {
            return visiable;
        }

        public void setVisiable(int visiable) {
            this.visiable = visiable;
        }

        public int getAvailable() {
            return available;
        }

        public void setAvailable(int available) {
            this.available = available;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUnavailableText() {
            return unavailableText;
        }

        public void setUnavailableText(String unavailableText) {
            this.unavailableText = unavailableText;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public int getDeviceVersion() {
            return deviceVersion;
        }

        public void setDeviceVersion(int deviceVersion) {
            this.deviceVersion = deviceVersion;
        }

        public int getIsTest() {
            return isTest;
        }

        public void setIsTest(int isTest) {
            this.isTest = isTest;
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
