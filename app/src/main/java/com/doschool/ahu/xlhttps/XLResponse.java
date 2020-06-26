package com.doschool.ahu.xlhttps;


import java.io.Serializable;

/**
 * Created by X on 2018/7/18
 *
 * 需要子类去实现的解析基类
 */
public abstract class XLResponse implements Serializable {

    private static final long serialVersionUID = 8004368341583124431L;
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
