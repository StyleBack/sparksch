package com.doschool.ahu.base.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by X on 2018/9/27
 */
public class DoUrlModel extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1684131648628466197L;
    private String action;
    private List<String> paramList;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<String> getParamList() {
        return paramList;
    }

    public void setParamList(List<String> paramList) {
        this.paramList = paramList;
    }
}
