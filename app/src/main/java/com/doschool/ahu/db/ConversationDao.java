package com.doschool.ahu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.doschool.ahu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X on 2018/9/4
 */
public class ConversationDao {
    private static final String TAG = "OrdersDao";

    // 列定义
    private final String[] ORDER_COLUMNS = new String[] {"Id", "Identify"};

    private Context context;
    private SparkDBHelper ordersDBHelper;

    public ConversationDao(Context context) {
        this.context = context;
        ordersDBHelper = new SparkDBHelper(context);
    }

    /**
     * 判断表中是否有数据
     */
    public boolean isDataExist(){
        int count = 0;

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = ordersDBHelper.getReadableDatabase();
            // select count(Id) from Orders
            cursor = db.query(SparkDBHelper.TABLE_NAME, new String[]{"COUNT(Id)"}, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            if (count > 0) return true;
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }

    /**
     * 初始化数据
     */
    public void initTable(){
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            db.execSQL("insert into " + SparkDBHelper.TABLE_NAME + " (Id, Identify) values (1, 'Arc')");
            db.execSQL("insert into " + SparkDBHelper.TABLE_NAME + " (Id, Identify) values (2, 'Bor')");
            db.execSQL("insert into " + SparkDBHelper.TABLE_NAME + " (Id, Identify) values (3, 'Cut')");
            db.execSQL("insert into " + SparkDBHelper.TABLE_NAME + " (Id, Identify) values (4, 'Bor')");
            db.execSQL("insert into " + SparkDBHelper.TABLE_NAME + " (Id, Identify) values (5, 'Arc')");
            db.execSQL("insert into " + SparkDBHelper.TABLE_NAME + " (Id, Identify) values (6, 'Doom')");
            db.execSQL("insert into " + SparkDBHelper.TABLE_NAME + " (Id, Identify) values (7, 'Doom')");
            db.execSQL("insert into " + SparkDBHelper.TABLE_NAME + " (Id, Identify) values (8, 'Doom')");
            db.execSQL("insert into " + SparkDBHelper.TABLE_NAME + " (Id, Identify) values (9, 'Doom')");
            db.execSQL("insert into " + SparkDBHelper.TABLE_NAME + " (Id, Identify) values (10, 'Doom')");

            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.e(TAG, "", e);
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    /**
     * 执行自定义SQL语句
     */
    public void execSQL(String sql) {
        SQLiteDatabase db = null;

        try {
            if (sql.contains("select")){
                Toast.makeText(context, R.string.strUnableSql, Toast.LENGTH_SHORT).show();
            }else if (sql.contains("insert") || sql.contains("update") || sql.contains("delete")){
                db = ordersDBHelper.getWritableDatabase();
                db.beginTransaction();
                db.execSQL(sql);
                db.setTransactionSuccessful();
                Toast.makeText(context, R.string.strSuccessSql, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, R.string.strErrorSql, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    /**
     * 查询数据库中所有数据
     */
    public List<ConversationDO> getAllDate(){
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = ordersDBHelper.getReadableDatabase();
            // select * from Orders
            cursor = db.query(SparkDBHelper.TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null);

            if (cursor.getCount() > 0) {
                List<ConversationDO> orderList = new ArrayList<ConversationDO>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder(cursor));
                }
                return orderList;
            }
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }

    /**
     * 新增一条数据
     */
    public boolean insertDate(ConversationDO order){
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            ContentValues contentValues = new ContentValues();
//            contentValues.put("Id", order.id);
            contentValues.put("Identify", order.identify);
            db.insertOrThrow(SparkDBHelper.TABLE_NAME, null, contentValues);

            db.setTransactionSuccessful();
            return true;
        }catch (SQLiteConstraintException e){
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.e(TAG, "", e);
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    /**
     * 删除一条数据  此处删除Id为7的数据
     */
    public boolean deleteOrder(String identify) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            // delete from Orders where Id = 7
            db.delete(SparkDBHelper.TABLE_NAME, "Identify = ?", new String[]{identify});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    /**
     * 清空表
     */
    public void clearConTable(){
        SQLiteDatabase sql=ordersDBHelper.getReadableDatabase();
        sql.execSQL("DELETE FROM Conersations");
    }

    /**
     * 将查找到的数据转换成Order类
     */
    private ConversationDO parseOrder(Cursor cursor){
        ConversationDO order = new ConversationDO();
//        order.id = (cursor.getInt(cursor.getColumnIndex("Id")));
        order.identify = (cursor.getString(cursor.getColumnIndex("Identify")));
        return order;
    }
}
