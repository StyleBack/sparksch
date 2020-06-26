package com.doschool.ahu.appui.main.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;

/**
 * Created by X on 2018/9/28
 */
public class SingleUserVO extends BaseModel implements Serializable {
    private static final long serialVersionUID = 933780817554037247L;

    private int userId;
    private String nickName;
    private int sex;
    private String departAbbr;
    private String headImage;
    private int userType;
    private int followState;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getDepartAbbr() {
        return departAbbr;
    }

    public void setDepartAbbr(String departAbbr) {
        this.departAbbr = departAbbr;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getFollowState() {
        return followState;
    }

    public void setFollowState(int followState) {
        this.followState = followState;
    }

    public boolean isST(){//学生
        return (getUserType()==1 || getUserType()==4)?true:false;
    }

    public boolean isTeacher(){//教师
        return getUserType()==3?true:false;
    }

    public boolean isOR(){//组织
        return getUserType()==2?true:false;
    }

    public boolean isBoy(){
        return getSex()==2?true:false;
    }

    public boolean isGirl(){
        return getSex()==1?true:false;
    }
}
