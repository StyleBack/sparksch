package com.doschool.ahu.appui.writeblog.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by X on 2019/1/4.
 */
public class VoteBean extends BaseModel implements Serializable {
    private static final long serialVersionUID = -3245875197255852454L;


    private List<IntroducerTxtBean> introducerTxt;

    public List<IntroducerTxtBean> getIntroducerTxt() {
        return introducerTxt;
    }

    public void setIntroducerTxt(List<IntroducerTxtBean> introducerTxt) {
        this.introducerTxt = introducerTxt;
    }

    public static class IntroducerTxtBean {

        private int id;
        private String hintConf;
        private String hintCons;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getHintConf() {
            return hintConf;
        }

        public void setHintConf(String hintConf) {
            this.hintConf = hintConf;
        }

        public String getHintCons() {
            return hintCons;
        }

        public void setHintCons(String hintCons) {
            this.hintCons = hintCons;
        }
    }
}
