package com.benkie.hjw.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import com.benkie.hjw.bean.HomeProductBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 37636 on 2018/1/29.
 * <p>
 * 产品
 */

public class ProductSqliteOpenHelper extends SQLiteOpenHelper {
    public static ProductSqliteOpenHelper helper;
    public final static String TAB_NAME = "product";
    public final static String DB_NAME = "product.db";
    private String createSql = "create table " + TAB_NAME + " (id integer primary key autoincrement, itemId integer, name varchar(20), address varchar(20), finishDate varchar(20))";

    /**
     * 数据库的构造方法
     * 数据库查询的结果集，为null则使用默认的结果集
     * 数据库的版本，从1开始，小于1则抛异常
     *
     * @param context
     */
    private ProductSqliteOpenHelper(Context context) {
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

    public static ProductSqliteOpenHelper getNewHelpe(Context context) {
        if (helper == null) {
            helper = new ProductSqliteOpenHelper(context);
        }
        return helper;
    }


    /**
     * @param list
     */
    public void setProducts(List<HomeProductBean> list) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(ProductSqliteOpenHelper.TAB_NAME, "", null);
        String sql = "insert into " + ProductSqliteOpenHelper.TAB_NAME + "(itemId,name,address,finishDate) values(?,?,?,?)";
        SQLiteStatement sstatement = db.compileStatement(sql);
        db.beginTransaction();
        for (HomeProductBean p : list) {
            sstatement.bindLong(1, p.getItemId());
            sstatement.bindString(2, p.getName());
            sstatement.bindString(3, p.getCity());
            sstatement.bindString(4, p.getFinishDate());
            sstatement.executeInsert();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public List<HomeProductBean> getAotu(String key) {
        SQLiteDatabase db = helper.getWritableDatabase();
        List<HomeProductBean> beanList = new ArrayList<>();
        Cursor cur = null;
        if (!TextUtils.isEmpty(key)) {
            String[] columns = {"itemId","name", "address","finishDate"};
            //查询条件
            String where = "name like ? or address like ?";
            //查询参数
            String[] selectArgs = {key + "%", key + "%"};
            //执行查询
            cur = db.query(ProductSqliteOpenHelper.TAB_NAME, columns, where, selectArgs, null, null, null);
            if (cur != null)
                cur.moveToFirst();
            while (!cur.isAfterLast()) {
                HomeProductBean productBean = new HomeProductBean();
                int itemId = cur.getInt(0);
                String name = cur.getString(1);
                String address = cur.getString(2);
                String date = cur.getString(3);
                productBean.setItemId(itemId);
                productBean.setName(name);
                productBean.setAddress(address);
                productBean.setFinishDate(date);
                beanList.add(productBean);
                cur.moveToNext();
            }
        }
        return beanList;
    }

}
