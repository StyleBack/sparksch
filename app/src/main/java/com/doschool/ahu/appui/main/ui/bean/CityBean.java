package com.doschool.ahu.appui.main.ui.bean;

import java.util.List;

/**
 * Created by X on 2018/11/9
 */
public class CityBean {


    private List<CitysBean> citys;

    public List<CitysBean> getCitys() {
        return citys;
    }

    public void setCitys(List<CitysBean> citys) {
        this.citys = citys;
    }

    public static class CitysBean {
        private String name;
        private List<CityArrBean> cityArr;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<CityArrBean> getCityArr() {
            return cityArr;
        }

        public void setCityArr(List<CityArrBean> cityArr) {
            this.cityArr = cityArr;
        }

        public static class CityArrBean {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return name;
            }
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
