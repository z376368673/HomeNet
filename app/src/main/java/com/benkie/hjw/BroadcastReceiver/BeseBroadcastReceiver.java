package com.benkie.hjw.BroadcastReceiver;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by 37636 on 2018/4/9.
 */

public class BeseBroadcastReceiver {


    /**
     * 支付成功后发送广播更新首页项目信息
     * @param code
     */
    public static  void sendToPublic(Activity activity,int code){
        Intent intent = new Intent();
        intent.setAction("com.benkie.public");
        intent.putExtra("errCode", String.valueOf(code));
        activity.sendBroadcast(intent);
    }

    /**
     * 支付成功后发送广播更新首页技能信息
     * @param code
     */
    public static void sendToSkill(Activity activity,int code){
        Intent intent = new Intent();
        intent.setAction("com.benkie.skill");
        intent.putExtra("errCode", String.valueOf(code));
        activity.sendBroadcast(intent);
    }

    /**
     * 取消收藏
     * @param activity
     * @param code
     */
    public static  void sendCollection(Activity activity,int code){
        Intent intent = new Intent();
        intent.setAction("com.benkie.collection");
        intent.putExtra("errCode", String.valueOf(code));
        activity.sendBroadcast(intent);
    }

}
