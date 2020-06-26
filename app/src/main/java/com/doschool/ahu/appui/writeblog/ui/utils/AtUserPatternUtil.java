package com.doschool.ahu.appui.writeblog.ui.utils;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by X on 2018/11/6
 */
public class AtUserPatternUtil {

    //@user转换  隐性字符串
    public static String patternToUser(String atStr){
        String name = "";
        String zhengze = "\\[a=.*?\\].*?\\[\\/a\\]";
        Pattern sinaPattern = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        Matcher matcher = sinaPattern.matcher(atStr);
        while (matcher.find()) {
            String key = matcher.group();
            Pattern patternNick = Pattern.compile("]@.*?\\[/a\\]", Pattern.CASE_INSENSITIVE);
            Matcher matcherNick = patternNick.matcher(key);
            String keyNick = null;
            while (matcherNick.find()) {
                keyNick = matcherNick.group();
                keyNick = keyNick.substring(1);
                name = keyNick.replace("[/a]", "");
            }
        }
        return name;
    }
}
