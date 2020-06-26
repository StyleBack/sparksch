package com.doschool.ahu.appui.home.ui.bean;

import com.doschool.ahu.appui.main.ui.bean.MicroblogCommentVO;
import com.doschool.ahu.base.model.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by X on 2018/9/14
 */
public class BlogComt extends BaseModel implements Serializable {
    private static final long serialVersionUID = -5809967920971955209L;

    private List<MicroblogCommentVO> data;

    public List<MicroblogCommentVO> getData() {
        return data;
    }

    public void setData(List<MicroblogCommentVO> data) {
        this.data = data;
    }
}
