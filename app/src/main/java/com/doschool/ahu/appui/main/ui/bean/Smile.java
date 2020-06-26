package com.doschool.ahu.appui.main.ui.bean;

import java.util.List;

/**
 * Created by X on 2018/8/31
 */
public class Smile {


    private String sourceID;
    private Object sourceURL;
    private double sourceSize;
    private boolean sourceIsGIF;
    private List<SourceExpressionsBean> sourceExpressions;

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public Object getSourceURL() {
        return sourceURL;
    }

    public void setSourceURL(Object sourceURL) {
        this.sourceURL = sourceURL;
    }

    public double getSourceSize() {
        return sourceSize;
    }

    public void setSourceSize(double sourceSize) {
        this.sourceSize = sourceSize;
    }

    public boolean isSourceIsGIF() {
        return sourceIsGIF;
    }

    public void setSourceIsGIF(boolean sourceIsGIF) {
        this.sourceIsGIF = sourceIsGIF;
    }

    public List<SourceExpressionsBean> getSourceExpressions() {
        return sourceExpressions;
    }

    public void setSourceExpressions(List<SourceExpressionsBean> sourceExpressions) {
        this.sourceExpressions = sourceExpressions;
    }

    public static class SourceExpressionsBean {

        private String expressionString;
        private int expressionWidth;
        private String expressionID;
        private String expressionImageName;
        private boolean expressionIsGIF;
        private Object expressionURL;
        private int expressionHeight;

        public String getExpressionString() {
            return expressionString;
        }

        public void setExpressionString(String expressionString) {
            this.expressionString = expressionString;
        }

        public int getExpressionWidth() {
            return expressionWidth;
        }

        public void setExpressionWidth(int expressionWidth) {
            this.expressionWidth = expressionWidth;
        }

        public String getExpressionID() {
            return expressionID;
        }

        public void setExpressionID(String expressionID) {
            this.expressionID = expressionID;
        }

        public String getExpressionImageName() {
            return expressionImageName;
        }

        public void setExpressionImageName(String expressionImageName) {
            this.expressionImageName = expressionImageName;
        }

        public boolean isExpressionIsGIF() {
            return expressionIsGIF;
        }

        public void setExpressionIsGIF(boolean expressionIsGIF) {
            this.expressionIsGIF = expressionIsGIF;
        }

        public Object getExpressionURL() {
            return expressionURL;
        }

        public void setExpressionURL(Object expressionURL) {
            this.expressionURL = expressionURL;
        }

        public int getExpressionHeight() {
            return expressionHeight;
        }

        public void setExpressionHeight(int expressionHeight) {
            this.expressionHeight = expressionHeight;
        }
    }
}
