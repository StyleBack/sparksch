package com.doschool.ahu.appui.main.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;

/**
 * Created by X on 2018/9/4
 */
public class UserVO extends BaseModel implements Serializable {

    private static final long serialVersionUID = -4207925095913150602L;
    /**
     * 主键,其它表中的user_id
     */
    private int id;

    //view中的id
    private int userId;
    /**
     * 昵称
     */
    private String nickName;

    //备注
    private String remarkName;
    /**
     * 性别1->女,2->男,0->保密
     */
    private int sex;
    /**
     * 院系简称
     */
    private String departAbbr;
    /**
     * 头像
     */
    private String headImage;
    /**
     * 用户类型 1-本科生 2-组织 3-教师 4-研究生 5-游客
     */
    private int userType;
    /**
     * open_id,部分使用场景替代uid
     */
    private String openId;
    /**
     * 学工号
     */
    private String funcId;
    /**
     * 真实姓名,30个字符应该够了
     */
    private String realName;
    /**
     * 手机号,20位防止出现什么异常情况
     */
    private String phone;
    /**
     * schoolId
     */
    private int schoolId;
    /**
     * 入学年份,Enrollment Year
     */
    private String enrYear;
    /**
     * 院系id,-1代表未设定
     */
    private int departId;
    /**
     * 专业id,-1代表未设定
     */
    private int majorId;
    /**
     * 密码,长度限定
     */
    private String password;
    /**
     * 邮箱,长度未知,先限定50
     */
    private String email;
    /**
     * 背景图片,长度待定
     */
    private String backgroundImage;
    /**
     * 自我介绍
     */
    private String selfIntro;
    /**
     * 家乡
     */
    private String hometown;
    /**
     * 兴趣爱好,长度暂定50
     */
    private String hobby;
    /**
     * 星座
     */
    private String constel;
    /**
     * 恋爱状态
     */
    private String loveStatus;
    /**
     * 是否内部账号
     */
    private int isInner;
    /**
     * 状态,用于做封禁标识以及验证状态
     */
    private int status;
    /**
     * IM签名
     */
    private String userSig;
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

    private String token;
    private String key;

    private String expiration;

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getEnrYear() {
        return enrYear;
    }

    public void setEnrYear(String enrYear) {
        this.enrYear = enrYear;
    }

    public int getDepartId() {
        return departId;
    }

    public void setDepartId(int departId) {
        this.departId = departId;
    }

    public int getMajorId() {
        return majorId;
    }

    public void setMajorId(int majorId) {
        this.majorId = majorId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getSelfIntro() {
        return selfIntro;
    }

    public void setSelfIntro(String selfIntro) {
        this.selfIntro = selfIntro;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getConstel() {
        return constel;
    }

    public void setConstel(String constel) {
        this.constel = constel;
    }

    public String getLoveStatus() {
        return loveStatus;
    }

    public void setLoveStatus(String loveStatus) {
        this.loveStatus = loveStatus;
    }

    public int getIsInner() {
        return isInner;
    }

    public void setIsInner(int isInner) {
        this.isInner = isInner;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
}
