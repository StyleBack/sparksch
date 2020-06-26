package com.doschool.ahu.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by X on 2018/7/19
 *
 * gson解析
 */
public class XLGson {

    /**
     * 获取json 解析
     * @param result
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJosn(String result,Class<T> clazz){
        return new Gson().fromJson(result,clazz);
    }

    /**
     * object转json
     * @param clazz
     * @return
     */
    public static String toJson(Object clazz){
        return new Gson().toJson(clazz);
    }


    /**
     *通过builder
     * @param result
     * @param clazz
     * @param <T>
     * @return
     */
    public  static <T> T createJson(String result,Class<T> clazz){
        Gson gson = new GsonBuilder()
                .setLenient()// json宽松  容错机制
                .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
                .serializeNulls() //智能null
                .setPrettyPrinting()// 调教格式
                .disableHtmlEscaping() //默认是GSON把HTML 转义的
                .create();
        return gson.fromJson(result,clazz);
    }

}
