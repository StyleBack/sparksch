package com.doschool.ahu.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by X on 2018/9/28
 */
public class UrlUtil {

    public static String paramMap2String(String method, Map<String, String> paramValues) {
        String params = "";
        Set<String> key = paramValues.keySet();
        String beginLetter = "";
        if (method.equalsIgnoreCase("get")) {
            beginLetter = "?";
        }

        for (Iterator<String> it = key.iterator(); it.hasNext(); ) {
            String s = (String) it.next();
            if (params.equals("")) {
                params += beginLetter + s + "=" + paramValues.get(s);
            } else {
                params += "&" + s + "=" + paramValues.get(s);
            }
        }
        return params;
    }
}
