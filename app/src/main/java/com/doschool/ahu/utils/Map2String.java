package com.doschool.ahu.utils;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by X on 2018/10/10
 */
public class Map2String {

    public static String map2HttpParams(ArrayMap<String,String> map){
        String params="";
        for (Iterator it = map.keySet().iterator(); it.hasNext();){
            String key=it.next().toString();
            String value=map.get(key);
            if (!TextUtils.isEmpty(value)){
                try {
                    value= URLEncoder.encode(value,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (!it.hasNext()){//最后一个元素的情况
                    params=params+key+"="+value;
                }else {
                    params=params+key+"="+value+"&";
                }
            }
        }
        return params;
    }
}
