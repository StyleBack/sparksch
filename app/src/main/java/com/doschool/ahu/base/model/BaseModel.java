package com.doschool.ahu.base.model;

import java.io.Serializable;

/**
 * Created by X on 2018/8/21
 */
public class BaseModel implements Serializable {
    private static final long serialVersionUID = 7316842584536334111L;

    private int code;
    private int messageType;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
