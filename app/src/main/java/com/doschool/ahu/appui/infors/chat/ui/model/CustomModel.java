package com.doschool.ahu.appui.infors.chat.ui.model;

import com.doschool.ahu.base.model.BaseModel;
import com.doschool.ahu.base.model.DoUrlModel;

import java.io.Serializable;

/**
 * Created by X on 2018/10/8
 */
public class CustomModel extends BaseModel implements Serializable {
    private static final long serialVersionUID = -7994627166283028234L;

    private DoUrlModel doUrl;
    private int extType;
    private String imageUrl;
    private String title;
    private String version;

    public DoUrlModel getDoUrl() {
        return doUrl;
    }

    public void setDoUrl(DoUrlModel doUrl) {
        this.doUrl = doUrl;
    }

    public int getExtType() {
        return extType;
    }

    public void setExtType(int extType) {
        this.extType = extType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "CustomModel{" +
                "doUrl=" + doUrl +
                ", extType=" + extType +
                ", imageUrl='" + imageUrl + '\'' +
                ", title='" + title + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
