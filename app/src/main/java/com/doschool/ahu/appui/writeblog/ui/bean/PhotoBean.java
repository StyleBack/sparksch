package com.doschool.ahu.appui.writeblog.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;

/**
 * Created by X on 2018/9/19
 */
public class PhotoBean extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1434318891681046335L;

    private String path;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
