package com.doschool.ahu.utils;

import com.doschool.ahu.appui.main.event.XMessageEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by X on 2018/8/30
 */
public class EventUtils {

    //注册
    public static void register(Object subscriber){
        EventBus.getDefault().register(subscriber);
    }
    //解除注册
    public static void unRegister(Object subscriber){
        if (EventBus.getDefault().isRegistered(subscriber)){
            EventBus.getDefault().unregister(subscriber);
        }
    }

    //发送事件
    public static void onPost(XMessageEvent event){
        EventBus.getDefault().post(event);
    }
}
