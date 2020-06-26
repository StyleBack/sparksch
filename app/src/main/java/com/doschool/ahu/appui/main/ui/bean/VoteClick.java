package com.doschool.ahu.appui.main.ui.bean;

import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by X on 2019/1/16.
 */
public class VoteClick extends BaseModel implements Serializable {
    private static final long serialVersionUID = 6241874839782285466L;

    private List<MicroblogVoteOptionsDto> data;

    public List<MicroblogVoteOptionsDto> getData() {
        return data;
    }

    public void setData(List<MicroblogVoteOptionsDto> data) {
        this.data = data;
    }
}
