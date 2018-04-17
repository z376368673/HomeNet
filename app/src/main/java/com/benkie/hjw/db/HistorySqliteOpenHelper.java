package com.benkie.hjw.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 37636 on 2018/1/29.
 * <p>
 * 产品
 */

public class HistorySqliteOpenHelper extends SQLiteOpenHelper {
    public static HistorySqliteOpenHelper helper;
    public final static String TAB_NAME = "history";
    public final static String DB_NAME = "history.db";
    private String createSql = "create table " + TAB_NAME + " (id integer primary key autoincrement,  name varchar(20) UNIQUE)";

    /**
     * 数据库的构造方法
     * 数据库查询的结果集，为null则使用默认的结果集
     * 数据库的版本，从1开始，小于1则抛异常
     *
     * @param context
     */
    private HistorySqliteOpenHelper(Context context) {
        super(context, DB_NAME, null, 2);
    }

    /**
     * 数据库在第一次被创建时调用，表结构，初始化
     * 数据类型的长度是无用的，只是给程序员看的
     *
     * @param sqLiteDatabase 数据库
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createSql);//
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TAB_NAME);
        this.onCreate(sqLiteDatabase);
    }

    public static HistorySqliteOpenHelper getNewHelpe(Context context) {
        if (helper == null) {
            helper = new HistorySqliteOpenHelper(context);
        }
        return helper;
    }

    public void setData(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        try{
            String sql = "insert into " + HistorySqliteOpenHelper.TAB_NAME + "(name) values(?)";
            SQLiteStatement sstatement = db.compileStatement(sql);
            db.beginTransaction();
            sstatement.bindString(1, name);
            sstatement.executeInsert();
        }catch (SQLiteConstraintException exception){
            Log.e("exception","column name is not unique");
        }finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }

    }
    public void delectAll(){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(TAB_NAME, "", null);
        db.close();
    }
    public List<String> getAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        List<String> beanList = new ArrayList<>();
        String sql = "select * from " + TAB_NAME;
        Cursor cur = null;
        cur = db.rawQuery(sql, null);
        if (cur != null)
            cur.moveToFirst();
        while (!cur.isAfterLast()) {
            String name = cur.getString(1).trim();
            if (!TextUtils.isEmpty(name)){
                beanList.add(name);
            }
            cur.moveToNext();
        }
        return beanList;
    }

}
