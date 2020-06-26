package com.doschool.ahu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by X on 2018/9/4
 */
public class SparkDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "spark.db";
    public static final String TABLE_NAME = "Conersations";
    public static final String USER_TABLE="User";
    public static final String APP_TABLE="AppConfig";

    public SparkDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table if not exists " + TABLE_NAME + " (Id integer primary key autoincrement, Identify text)";
        String sql1 = "create table if not exists " + USER_TABLE + " (Id integer primary key, logindata text)";
        String sql2 = "create table if not exists " + APP_TABLE + " (Id integer primary key autoincrement, configdata text)";
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(sql1);
        sqLiteDatabase.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        String sql1= "DROP TABLE IF EXISTS " + USER_TABLE;
        String sql2= "DROP TABLE IF EXISTS " + APP_TABLE;
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(sql1);
        sqLiteDatabase.execSQL(sql2);
        onCreate(sqLiteDatabase);
    }
}
