package com.doschool.ahu.appui.main.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by X on 2018/10/24
 */
public class DepartBean extends BaseModel implements Serializable {
    private static final long serialVersionUID = 2868912663490094370L;

    private List<PartDta> data;

    public List<PartDta> getData() {
        return data;
    }

    public void setData(List<PartDta> data) {
        this.data = data;
    }

    public static class PartDta{
        private int id;
        private String departName;
        private String departAbbr;
        private int schoolId;
        private int type;
        private String gmtCreate;
        private String gmtModified;
        private int isDeleted;
        private List<MajorBean> majorDOS;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDepartName() {
            return departName;
        }

        public void setDepartName(String departName) {
            this.departName = departName;
        }

        public String getDepartAbbr() {
            return departAbbr;
        }

        public void setDepartAbbr(String departAbbr) {
            this.departAbbr = departAbbr;
        }

        public int getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(int schoolId) {
            this.schoolId = schoolId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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

        public List<MajorBean> getMajorDOS() {
            return majorDOS;
        }

        public void setMajorDOS(List<MajorBean> majorDOS) {
            this.majorDOS = majorDOS;
        }

        @Override
        public String toString() {
            if (departAbbr.length()>8){
                departAbbr=departAbbr.substring(0,7)+"...";
            }
            return departAbbr;
        }
    }
}
