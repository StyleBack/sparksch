package com.doschool.ahu.appui.discover.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;

/**
 * Created by X on 2018/9/27
 */
public class HeadLineDo extends BaseModel implements Serializable {
    private static final long serialVersionUID = -1831061044003452219L;

    /**
     * id
     */
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 标题
     */
    private String title;
    /**
     * 标签
     */
    private String label;
    /**
     * 来源
     */
    private String source;
    /**
     * 文章内容
     */
    private String content;
    /**
     * 点赞数
     */
    private int up;
    /**
     * 踩数
     */
    private int down;
    /**
     * 编辑寄语
     */
//    private String message;
    /**
     * 阅读数
     */
    private int view;
    /**
     * 封面图片
     */
    private String coverImage;
    /**
     * 时间标签
     */
    private String date;
    /**
     * 发布时间
     */
    private String publishTime;
    /**
     * 是否为置顶头条
     */
    private int isUp;
    /**
     * 记录创建时间
     */
    private String gmtCreate;
    /**
     * 记录修改时间
     */
    private String gmtModified;
    /**
     * 1表示删除,0表示未删除
     */
    private int isDeleted;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    @Override
//    public String getMessage() {
//        return message;
//    }
//
//    @Override
//    public void setMessage(String message) {
//        this.message = message;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public int getIsUp() {
        return isUp;
    }

    public void setIsUp(int isUp) {
        this.isUp = isUp;
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
