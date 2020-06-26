package com.doschool.ahu.db;

/**
 * Created by X on 2018/9/4
 */
public class ConversationDO {
    public int id;
    public String identify;

    public ConversationDO() {
    }

    public ConversationDO(int id, String identify) {
        this.id = id;
        this.identify=identify;
    }
}
