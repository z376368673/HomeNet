package com.benkie.hjw.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.benkie.hjw.R;
import com.benkie.hjw.constants.Constants;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.net.Http;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.Locale;

/**
 * Created by 37636 on 2018/2/27.
 */

public class ShareUtils {

    /**
     * 技术服务分享
     */
    public static void shareSkill(Activity activity,Handler handler,int id) {
        String title = "我的技术服务";
        String link = String.format(Locale.CHINA, Http.BASE_URL+"/yetdwell/sharing/technicist.do?userInfoId=%s&lat=%s&lng=%s", id, Constants.Latitude,Constants.Longitude);
        showSharePanel(activity,  handler,title, "技术服务", link);
    }

    /**
     * 技术服务集赞分享  没有qq分享
     */
    public static void shareSkills(Activity activity,Handler handler,int id) {
        String title = "我的技术服务";
        String link = String.format(Locale.CHINA, Http.BASE_URL+"/yetdwell/sharing/otherTechnicist.do?userInfoId=%s&lat=%s&lng=%s", id, Constants.Latitude,Constants.Longitude);
        showSharePanels(activity,  handler,title, "技术服务", link);
    }

    /**
     * 项目分享
     */
    public static void shareProduct(Activity activity,Handler handler,int id) {
        String title = "我的项目";
        String link = String.format(Locale.CHINA, Http.BASE_URL+"/yetdwell/sharing/endDetails.do?itemid=%s",  id);
        showSharePanel(activity,  handler,title, "完成项目", link);
    }

    /**
     * 项目集赞分享
     */
    public static void shareProducts(Activity activity,Handler handler,int id) {
        String title = "我的项目";
        String link = String.format(Locale.CHINA, Http.BASE_URL+"/yetdwell/sharing/otherEndDetails.do?itemid=%s",  id);
        showSharePanels(activity,  handler,title, "完成项目", link);
    }


    /**
     * 好友分享
     */
    public static void shareRecomment(Activity activity, Handler handler,int id) {
        String title = "好友分享";
        String link = String.format(Locale.CHINA, Http.BASE_URL+"/yetdwell/sharing/recommend.do?",  id);
        showSharePanel(activity, handler,title, "好友分享", link);
    }



    private static void showSharePanel(final Activity activity, final Handler handler, final String title, final String content, final String link){
        boolean flag = true;
        String str = "";
        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE};
            for (String permission: mPermissionList){
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                    str = permission;
                    flag = false;
                    break;
                }
            }
        }
        if (!flag){
            Toast.makeText(activity,"缺少权限："+str,Toast.LENGTH_SHORT).show();
            return;
        }

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_share_bottom_sheet,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view)
                .setCancelable(true);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        WindowManager m = dialog.getWindow().getWindowManager();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = d.getWidth(); //设置dialog的宽度为当前手机屏幕的宽度
        dialog.getWindow().setAttributes(p);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMWeb web = new UMWeb(link);
                web.setTitle(title);
                web.setDescription("我为还局网代言"+"\n"+String.format(Locale.CHINA,"来自还居网%s分享",content));
                web.setThumb(new UMImage(activity,R.mipmap.ic_huanju));
                ShareAction shareAction = new ShareAction(activity)
                        .withMedia(web)
                        .setCallback(new ShareCallback(activity,handler));
                if (v.getId()==R.id.wechatCircle){
                    shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                    shareAction.share();
                }else if (v.getId()==R.id.wechat){
                    shareAction.setPlatform(SHARE_MEDIA.WEIXIN);
                    shareAction.share();
                }else if (v.getId()==R.id.qq){
                    shareAction.setPlatform(SHARE_MEDIA.QQ);
                    shareAction.share();
                }
                dialog.dismiss();
            }
        };
        view.findViewById(R.id.wechatCircle).setOnClickListener(listener);
        view.findViewById(R.id.wechat).setOnClickListener(listener);
        view.findViewById(R.id.qq).setOnClickListener(listener);
    }

    private static void showSharePanels(final Activity activity, final Handler handler, final String title, final String content, final String link){
        boolean flag = true;
        String str = "";
        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE};
            for (String permission: mPermissionList){
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                    str = permission;
                    flag = false;
                    break;
                }
            }
        }
        if (!flag){
            Toast.makeText(activity,"缺少权限："+str,Toast.LENGTH_SHORT).show();
            return;
        }

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_share_bottom_sheet,null);
        view.findViewById(R.id.qq).setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view)
                .setCancelable(true);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        WindowManager m = dialog.getWindow().getWindowManager();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = d.getWidth(); //设置dialog的宽度为当前手机屏幕的宽度
        dialog.getWindow().setAttributes(p);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMWeb web = new UMWeb(link);
                web.setTitle(title);
                web.setDescription("我为还局网代言"+"\n"+String.format(Locale.CHINA,"来自还居网%s分享",content));
                web.setThumb(new UMImage(activity,R.mipmap.ic_huanju));
                ShareAction shareAction = new ShareAction(activity)
                        .withMedia(web)
                        .setCallback(new ShareCallback(activity,handler));
                if (v.getId()==R.id.wechatCircle){
                    shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                    shareAction.share();
                    handler.sendEmptyMessage(200);
                }else if (v.getId()==R.id.wechat){
                    shareAction.setPlatform(SHARE_MEDIA.WEIXIN);
                    shareAction.share();
                    handler.sendEmptyMessage(200);
                }else if (v.getId()==R.id.qq){
                    shareAction.setPlatform(SHARE_MEDIA.QQ);
                    shareAction.share();
                }
                dialog.dismiss();
            }
        };
        view.findViewById(R.id.wechatCircle).setOnClickListener(listener);
        view.findViewById(R.id.wechat).setOnClickListener(listener);
        view.findViewById(R.id.qq).setOnClickListener(listener);
    }

    private static class ShareCallback implements UMShareListener {
        Context mContext;
        Handler handler;
        ShareCallback(Context context, Handler handler){
            mContext = context;
            this.handler = handler;
        }
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toast.makeText(mContext,"分享成功",Toast.LENGTH_LONG).show();
            handler.sendEmptyMessage(200);
        }
        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Log.e(">>",throwable.getMessage()+"-->"+share_media.toString());
            Toast.makeText(mContext,"分享失败",Toast.LENGTH_LONG).show();
            handler.sendEmptyMessage(404);
        }
        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Toast.makeText(mContext,"取消分享",Toast.LENGTH_LONG).show();
            handler.sendEmptyMessage(202);
        }
    }
}
