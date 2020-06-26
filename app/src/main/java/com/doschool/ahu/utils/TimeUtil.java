package com.doschool.ahu.utils;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by X on 2018/9/6
 */
public class TimeUtil {

    /**
     * 把long型的日期，转成智能型的日期
     */
    public static String dateLongToDiyStr(long time) {
        Calendar changeC = Calendar.getInstance();
        changeC.setTimeInMillis(time);
        Calendar currentC = Calendar.getInstance();
        if (currentC.getTimeInMillis() - changeC.getTimeInMillis() < 0) {
            return "来自未来";
        } else if (changeC.get(Calendar.YEAR) < currentC.get(Calendar.YEAR) - 2) {
            return new SimpleDateFormat("yyyy年M月d日 H:mm").format(changeC.getTime());
        } else if (changeC.get(Calendar.YEAR) == currentC.get(Calendar.YEAR) - 2) {
            return new SimpleDateFormat("前年M月d日 H:mm").format(changeC.getTime());
        } else if (changeC.get(Calendar.YEAR) == currentC.get(Calendar.YEAR) - 1) {
            return new SimpleDateFormat("去年M月d日 H:mm").format(changeC.getTime());
        } else if (changeC.get(Calendar.DAY_OF_YEAR) < currentC.get(Calendar.DAY_OF_YEAR) - 2) {
            return new SimpleDateFormat("M月d日 H:mm").format(changeC.getTime());
        } else if (changeC.get(Calendar.DAY_OF_YEAR) == currentC.get(Calendar.DAY_OF_YEAR) - 2) {
            return new SimpleDateFormat("前天 H:mm").format(changeC.getTime());
        } else if (changeC.get(Calendar.DAY_OF_YEAR) == currentC.get(Calendar.DAY_OF_YEAR) - 1) {
            return new SimpleDateFormat("昨天 H:mm").format(changeC.getTime());
        } else if (changeC.get(Calendar.DAY_OF_YEAR) == currentC.get(Calendar.DAY_OF_YEAR)) {
            return new SimpleDateFormat("H:mm").format(changeC.getTime());
        }
        return "";
    }

    /**
     * 把long型的日期，转成智能型的日期
     */
    public static String date2USDiy(Long date){
        Calendar old=Calendar.getInstance();
        old.setTimeInMillis(date);
        Calendar now=Calendar.getInstance();
        if (now.getTimeInMillis()-old.getTimeInMillis()<0){
            return "";
        }else if (old.get(Calendar.YEAR)==now.get(Calendar.YEAR)-1){
            return new SimpleDateFormat("yyyy年M月d日 H:mm").format(old.getTime());
        }else if (old.get(Calendar.DAY_OF_YEAR) < now.get(Calendar.DAY_OF_YEAR) - 2) {
            return new SimpleDateFormat("M月d日 H:mm").format(old.getTime());
        }else if (old.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) - 2) {
            return new SimpleDateFormat("前天 H:mm").format(old.getTime());
        }else if (old.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) - 1) {
            return new SimpleDateFormat("昨天 H:mm").format(old.getTime());
        }else if (old.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)){
            if (old.get(Calendar.HOUR) < now.get(Calendar.HOUR)){
                if (now.get(Calendar.HOUR)-old.get(Calendar.HOUR)>1){
                    return now.get(Calendar.HOUR)-old.get(Calendar.HOUR)+"小时前";
                }else {
                    if ((60-old.get(Calendar.MINUTE))+now.get(Calendar.MINUTE)>=60){
                        return now.get(Calendar.HOUR)-old.get(Calendar.HOUR)+"小时前";
                    }else {
                        return (60-old.get(Calendar.MINUTE))+now.get(Calendar.MINUTE)+"分钟前";
                    }
                }
            } else if (old.get(Calendar.HOUR) > now.get(Calendar.HOUR)) {//根据零界点判断
                return 12-old.get(Calendar.HOUR)+now.get(Calendar.HOUR)+"小时前";
            } else if (old.get(Calendar.MINUTE) < now.get(Calendar.MINUTE)) {
                return now.get(Calendar.MINUTE) - old.get(Calendar.MINUTE) + "分钟前";
            } else if (old.get(Calendar.MINUTE) == now.get(Calendar.MINUTE)) {
                return "刚刚";
            }
        }
        return "";
    }

    //取当前年份往下几年内的数据
    public static List<String> getYears(){
        List<String> years=new ArrayList<>();
        Calendar calendar=Calendar.getInstance();
        int mYear=calendar.get(Calendar.YEAR);
        for (int i=mYear;i>=2011;i--){
            years.add(String.valueOf(i));
        }
        return years;
    }

}
