package com.doschool.ahu.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.doschool.ahu.appui.main.ui.bean.AppCofingDO;
import com.doschool.ahu.appui.reglogin.bean.LoginVO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by X on 2018/9/11
 */
public class AppConfigDao {

    private Context context;
    private SparkDBHelper ordersDBHelper;

    public AppConfigDao(Context context) {
        this.context = context;
        ordersDBHelper = new SparkDBHelper(context);
    }


    /**
     * 保存
     * @param appDO
     */
    public void saveObject(AppCofingDO.AppDO appDO){
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(appDO);
            objectOutputStream.flush();
            byte data[] = arrayOutputStream.toByteArray();
            objectOutputStream.close();
            arrayOutputStream.close();
            SQLiteDatabase database = ordersDBHelper.getWritableDatabase();
            database.execSQL("insert into AppConfig (configdata) values(?)", new Object[] { data });
            database.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 取数据 单条
     * @return
     */
    public AppCofingDO.AppDO getAppCinfigDO(){
        AppCofingDO.AppDO appDO=null;
        SQLiteDatabase database=ordersDBHelper.getReadableDatabase();
        Cursor cursor=null;
        cursor=database.rawQuery("select * from "+SparkDBHelper.APP_TABLE, null);
        try {
            if (cursor!=null){
                while (cursor.moveToNext()){
                    byte data[]=cursor.getBlob(cursor.getColumnIndex("configdata"));
                    ByteArrayInputStream arrayInputStream=new ByteArrayInputStream(data);
                    try {
                        ObjectInputStream inputStream=new ObjectInputStream(arrayInputStream);
                        appDO= (AppCofingDO.AppDO) inputStream.readObject();
                        inputStream.close();
                        arrayInputStream.close();
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor!=null){
                cursor.close();
            }
        }
        return appDO;
    }

    /**
     * 清空表
     */
    public void clearUserTable(){
        SQLiteDatabase sql=ordersDBHelper.getReadableDatabase();
        sql.execSQL("DELETE FROM AppConfig");
    }

}
