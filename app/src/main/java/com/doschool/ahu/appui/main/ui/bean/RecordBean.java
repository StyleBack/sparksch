package com.doschool.ahu.appui.main.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;

/**
 * Created by X on 2018/10/25
 */
public class RecordBean extends BaseModel implements Serializable{
    private static final long serialVersionUID = 6907222012926253180L;

    private Record data;

    public Record getData() {
        return data;
    }

    public void setData(Record data) {
        this.data = data;
    }

    public static class Record{
        private int isFirstLogin;

        public int getIsFirstLogin() {
            return isFirstLogin;
        }

        public void setIsFirstLogin(int isFirstLogin) {
            this.isFirstLogin = isFirstLogin;
        }
    }
}
