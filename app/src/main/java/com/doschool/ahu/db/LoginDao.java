package com.doschool.ahu.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.doschool.ahu.appui.reglogin.bean.LoginVO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by X on 2018/9/11
 */
public class LoginDao {

    private Context context;
    private SparkDBHelper ordersDBHelper;

    public LoginDao(Context context) {
        this.context = context;
        ordersDBHelper = new SparkDBHelper(context);
    }


    /**
     * 保存
     */
    public void saveObject(LoginVO.LoginData loginData) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(loginData);
            objectOutputStream.flush();
            byte[] data = arrayOutputStream.toByteArray();
            objectOutputStream.close();
            arrayOutputStream.close();
            SQLiteDatabase database = ordersDBHelper.getWritableDatabase();
            database.execSQL("insert into User (logindata) values(?)", new Object[]{data});
            database.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 取数据 单条
     */
    public LoginVO.LoginData getObject() {
        LoginVO.LoginData loginData = null;
        SQLiteDatabase database = ordersDBHelper.getReadableDatabase();
        try (Cursor cursor = database.rawQuery("select * from " + SparkDBHelper.USER_TABLE, null)) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    byte[] data = cursor.getBlob(cursor.getColumnIndex("logindata"));
                    ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
                    try {
                        ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
                        loginData = (LoginVO.LoginData) inputStream.readObject();
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
        }
        return loginData;
    }

    /**
     * 清空表
     */
    public void clearUserTable() {
        SQLiteDatabase sql = ordersDBHelper.getReadableDatabase();
        sql.execSQL("DELETE FROM User");
    }

}
