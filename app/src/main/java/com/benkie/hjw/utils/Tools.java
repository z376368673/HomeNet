package com.benkie.hjw.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.benkie.hjw.R;
import com.benkie.hjw.application.BaseApp;
import com.benkie.hjw.net.Http;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 37636 on 2018/1/2.
 */

public class Tools {

    private static final int MIN_DELAY_TIME = 800;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    private static final double EARTH_RADIUS = 6378137.0;

    /***
     *
     * @param initValue 初始大小
     * @param num       个数
     * @param company   单位
     * @return initValue 单位
     */
    public static List<String> getDate(int initValue, int num, String company) {
        if (company == null) company = "";
        int d = initValue;
        List<String> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add((d + i) + company);
        }
        return list;
    }

    // 返回单位是米
    public static double getDistance(double longitude1, double latitude1,
                                     double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    public static String getStrDistance(double longitude1, double latitude1,
                                        double longitude2, double latitude2) {
        double s = getDistance(longitude1, latitude1, longitude2, latitude2);
        DecimalFormat df = new DecimalFormat("######0.0");
        s = s / 1000;
        String str = df.format(s) + "公里";
        return str;
    }

    public static String getStrDistance(LatLng latLng1, LatLng latLng2) {
        double s = AMapUtils.calculateLineDistance(latLng1, latLng2);
        DecimalFormat df = new DecimalFormat("######0.0");
        s = s / 1000;
        String str = df.format(s) + "公里";
        return str;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static void loadHeadImg(Context context, ImageView view, String url) {
        try {
            if (context == null) return;
            if (url == null) url = "";
            Glide.with(context)
                    .load(url)
                    .error(R.drawable.iv_defult_head)
                    .into(view);
        } catch (Exception e) {

        }
    }

    public static void loadImg(Context context, ImageView view, String url) {
        try {
            if (context == null) return;
            if (url == null) url = "";
            Glide.with(context)
                    .load(url)
                    .error(R.mipmap.iv_defult_img)
                    .into(view);
        } catch (Exception e) {
                e.printStackTrace();
        }
    }

    /**
     * @param context
     * @param view
     * @param url
     * @param placeholder 占位图
     */
    public static void loadImg(Context context, ImageView view, String url, int placeholder) {
        try {
            if (context == null) return;
            if (url == null) url = "";
            Glide.with(context)
                    .load(url)
                    .placeholder(placeholder)
                    .into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static String getYMD(int date){
        String unit = "";
        if (date>0&&date<30){
            unit = date+"天";
        }else if ( date<366&&date>=30&&date%30==0&&date==183){
            unit = "半年";
        }else if ( date<366&&date>=30&&date%30==0){
            unit = +date/30+"月";
        }else if ( date<366&&date>=30&&date%30!=0){
            unit = date+"天";
        }else if ( date%366==0&&date/366==1){
            unit = "年";
        }else if ( date%366==0){
            unit = date/366+"年";
        }else{
            unit = date+"天";
        }
        return unit;
    }
}
//