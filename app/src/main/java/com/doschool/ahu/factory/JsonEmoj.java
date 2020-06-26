package com.doschool.ahu.factory;

import android.content.Context;

import com.doschool.ahu.appui.main.ui.bean.Smile;
import com.doschool.ahu.utils.XLGson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by X on 2018/8/31
 */
public class JsonEmoj {

    public static Smile getJson(Context context){
        StringBuilder newstringBuilder = new StringBuilder();
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open("defaultExpression.json");
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
        Smile smile= XLGson.fromJosn(result,Smile.class);
        return smile;
    }
}
