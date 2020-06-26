package com.doschool.ahu.utils;

import android.content.Context;

import com.doschool.ahu.appui.main.ui.bean.CityBean;
import com.doschool.ahu.appui.main.ui.bean.ConsModel;
import com.doschool.ahu.appui.writeblog.ui.bean.VoteBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by X on 2018/11/9
 */
public class AssetJson {

    //省市解析
    public static CityBean getJosnCitys(Context context){
        StringBuilder newstringBuilder = new StringBuilder();
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open("city.json");
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(isr);
            String jsonLine;
            while ((jsonLine = reader.readLine()) != null) {
                newstringBuilder.append(jsonLine);
            }
            reader.close();
            isr.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result =  newstringBuilder .toString();
        CityBean smile= XLGson.fromJosn(result,CityBean.class);
        return smile;
    }

    //星座
    public static ConsModel getJsonCons(Context context){
        StringBuilder newstringBuilder = new StringBuilder();
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open("constellation.json");
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(isr);
            String jsonLine;
            while ((jsonLine = reader.readLine()) != null) {
                newstringBuilder.append(jsonLine);
            }
            reader.close();
            isr.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result =  newstringBuilder .toString();
        ConsModel consModel= XLGson.fromJosn(result,ConsModel.class);
        return consModel;
    }

    //投票随机提示
    public static VoteBean getVoteJs(Context context){
        StringBuilder voteBulder=new StringBuilder();
        InputStream inputStream=null;
        try {
            inputStream=context.getResources().getAssets().open("vote.json");
            InputStreamReader isr=new InputStreamReader(inputStream);
            BufferedReader reader=new BufferedReader(isr);
            String json="";
            while ((json=reader.readLine())!=null){
                voteBulder.append(json);
            }
            reader.close();
            isr.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result=voteBulder.toString();
        VoteBean voteBean=XLGson.fromJosn(result,VoteBean.class);
        return voteBean;
    }
}
