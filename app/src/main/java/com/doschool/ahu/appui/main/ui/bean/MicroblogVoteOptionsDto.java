package com.doschool.ahu.appui.main.ui.bean;


import java.io.Serializable;

/**
 * Created by X on 2019/1/15.
 */
public class MicroblogVoteOptionsDto  implements Serializable {
    private static final long serialVersionUID = 4284176290255865520L;

    private int totalNum;
    private String proportion;
    private boolean isSelected;

    private MicroblogVoteOptionsDO microblogVoteOptionsDO;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public MicroblogVoteOptionsDO getMicroblogVoteOptionsDO() {
        return microblogVoteOptionsDO;
    }

    public void setMicroblogVoteOptionsDO(MicroblogVoteOptionsDO microblogVoteOptionsDO) {
        this.microblogVoteOptionsDO = microblogVoteOptionsDO;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
