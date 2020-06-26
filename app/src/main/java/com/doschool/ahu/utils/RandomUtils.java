package com.doschool.ahu.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by X on 2018/9/20
 */
public class RandomUtils {

    public static String getRandomChar(int length){
        char[] chars={'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        Random random=new Random();
        StringBuffer buffer=new StringBuffer();
        for (int i=0;i<length;i++){
            buffer.append(chars[random.nextInt(62)]);
        }
        return buffer.toString();
    }

    //通过时间戳+uid+随机5个字符生成文件名
    public static String getRandName(int uid,int length){
        Long timeStamp=System.currentTimeMillis();
        SimpleDateFormat sd=new SimpleDateFormat("yyyyMMddhhmmssSSS");
        String mat=sd.format(new Date());
        return mat+String.valueOf(uid)+getRandomChar(length);
    }

    //生成本地文件名
    public static String getRandName(int length){
        Long timeStamp=System.currentTimeMillis();
        SimpleDateFormat sd=new SimpleDateFormat("yyyyMMddhhmmssSSS");
        String mat=sd.format(new Date());
        return mat+getRandomChar(length);
    }
}
