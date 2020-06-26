package com.doschool.ahu.appui.main.ui.bean;

import java.util.List;

/**
 * Created by X on 2018/11/9
 */
public class ConsModel {

    private List<ConsBean> cons;

    public List<ConsBean> getCons() {
        return cons;
    }

    public void setCons(List<ConsBean> cons) {
        this.cons = cons;
    }

    public static class ConsBean {

        private String name;
        private String time;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
