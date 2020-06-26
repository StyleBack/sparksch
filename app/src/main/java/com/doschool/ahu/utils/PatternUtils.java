package com.doschool.ahu.utils;


import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtils {
    /**
     * 将字符串中的连续的多个换行缩减
     * @param str  要处理的内容
     * @return	返回的结果
     */
    public static String replaceLineBlanks(String str) {
        String result = "";
        if (str != null) {
            Pattern p = Pattern.compile("(\r?\n(\\s*\r?\n)+)");
            Matcher m = p.matcher(str);
            result = m.replaceAll("\r\n\r\n");
        }
        if (!TextUtils.isEmpty(result)){
            return result;
        }else {
            return str;
        }
    }
}
