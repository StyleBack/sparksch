package com.doschool.ahu.appui.reglogin.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;

/**
 * Created by X on 2018/9/17
 */
public class SysNext extends BaseModel implements Serializable {
    private static final long serialVersionUID = -1388188285126401822L;

    /**
     * data : {"sex":"女","name":"祝苇华"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private static final long serialVersionUID = 3154101604778811893L;
        /**
         * sex : 女
         * name : 祝苇华
         */

        private String sex;
        private String name;

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
