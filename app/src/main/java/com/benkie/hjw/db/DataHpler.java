package com.benkie.hjw.db;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.benkie.hjw.application.BaseApp;
import com.benkie.hjw.bean.UserInfo;
import com.benkie.hjw.utils.PrefUtils;

/**
 * Created by 37636 on 2018/1/20.
 */

public class DataHpler {
    private static Context context = BaseApp.getInstance();

    /**
     * 是否已登陆
     *
     * @return
     */
    public static boolean islogin() {
        if (!TextUtils.isEmpty(getToken()) && getToken().length() > 6) {
            return true;
        }
        return false;
    }

    /**
     * 设置token
     *
     * @param token
     */
    public static void setToken(String token) {
        PrefUtils.putString(context, "token", token);
    }

    /**
     * 获取 token
     *
     * @return token
     */
    public static String getToken() {
        return PrefUtils.getString(context, "token", "");
    }


    /**
     * 保存用户信息 userInfo为json字符串
     *
     * @param userInfo
     */
    public static void setUserInfo(String userInfo) {
        PrefUtils.putString(context, "userInfo", userInfo);
    }

    public static UserInfo getUserInfo() {
        try {
            String json = PrefUtils.getString(context, "userInfo", "");
            UserInfo userInfo = JSON.parseObject(json, UserInfo.class);
            if (userInfo == null) userInfo = new UserInfo();
            return userInfo;
        } catch (Exception e) {

        }
        return new UserInfo();
    }

    public static void setLikeType(String str) {
        PrefUtils.putString(context, "LikeType", str);
    }

    public static String getLikeType() { //会员
        return PrefUtils.getString(context, "LikeType", "");
    }


    /**
     * 是不是第一次
     *
     * @param key
     */
    public static void setFirstShow(String key) {
        PrefUtils.putBoolean(context, key, false);
    }

    public static boolean getFirstShow(String key) {
        return PrefUtils.getBoolean(context, key, true);
    }

    /**
     * 最后一次登陆账号
     */

    public static String getLastAccount() {
        String account = PrefUtils.getString(context, "Account", "");
        return account;
    }

    public static void setLastAccount(String account) {
        PrefUtils.putString(context, "Account", account);
    }

    /**
     * 清除所以保存信息
     */
    public static void exit() {
        //PrefUtils.clear(context); 这句代码貌似不能清除数据
        setToken("");
        setUserInfo("");
    }

}
