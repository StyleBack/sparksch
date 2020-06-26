package com.doschool.ahu.appui.main.event;


import com.doschool.ahu.appui.main.ui.holderlogic.CvsHolder;

/**
 * Created by X on 2018/8/24
 */
public interface SwipeItemClickListener {
    void onTop(int position,CvsHolder holder);
    void onDelete(int position);
    void onItemListener(int position);
}
