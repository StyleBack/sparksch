package com.doschool.ahu.appui.discover.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by X on 2018/9/23
 */
public class ToolServiceBean extends BaseModel implements Serializable {
    private static final long serialVersionUID = -2552695901580107612L;

    private Tool data;

    public Tool getData() {
        return data;
    }

    public void setData(Tool data) {
        this.data = data;
    }

    public static class Tool{
        private List<ServiceConfigBean> normal;

        public List<ServiceConfigBean> getNormal() {
            return normal;
        }

        public void setNormal(List<ServiceConfigBean> normal) {
            this.normal = normal;
        }

        private List<ServiceConfigBean> large;

        public List<ServiceConfigBean> getLarge() {
            return large;
        }

        public void setLarge(List<ServiceConfigBean> large) {
            this.large = large;
        }
    }
}
