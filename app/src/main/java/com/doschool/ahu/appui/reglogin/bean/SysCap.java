package com.doschool.ahu.appui.reglogin.bean;

import com.doschool.ahu.base.model.BaseModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by X on 2018/9/17
 */
public class SysCap extends BaseModel implements Serializable {
    private static final long serialVersionUID = -9213880784119984511L;


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements  Serializable {
        private static final long serialVersionUID = 7173943954661240137L;

        private ExtBean ext;
        private String path;
        private String create_time;
        private String school_name;
        private String id;
        @SerializedName("message")
        private String messageX;
        private String type;

        public ExtBean getExt() {
            return ext;
        }

        public void setExt(ExtBean ext) {
            this.ext = ext;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getSchool_name() {
            return school_name;
        }

        public void setSchool_name(String school_name) {
            this.school_name = school_name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessageX() {
            return messageX;
        }

        public void setMessageX(String messageX) {
            this.messageX = messageX;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public static class ExtBean implements Serializable {
            private static final long serialVersionUID = 1330418999661051835L;
            /**
             * cookie : e1vgvt55ymngxk55pmcyksba
             * viewState : dDwtNTE2MjI4MTQ7Oz6EbPZxwZLeI+vJ7sUQsX6GLavdSg==
             */

            private String cookie;
            private String viewState;
            private String captcha;

            public String getCookie() {
                return cookie;
            }

            public void setCookie(String cookie) {
                this.cookie = cookie;
            }

            public String getViewState() {
                return viewState;
            }

            public void setViewState(String viewState) {
                this.viewState = viewState;
            }

            public String getCaptcha() {
                return captcha;
            }

            public void setCaptcha(String captcha) {
                this.captcha = captcha;
            }
        }
    }
}
