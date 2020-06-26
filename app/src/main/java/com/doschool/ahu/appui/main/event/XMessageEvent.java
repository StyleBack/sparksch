package com.doschool.ahu.appui.main.event;

/**
 * Created by X on 2018/8/30
 */
public class XMessageEvent<T> {

    private int code;
    private T data;

    public XMessageEvent(int code) {
        this.code = code;
    }

    public XMessageEvent(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
