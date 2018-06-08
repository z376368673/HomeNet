package com.benkie.hjw.version;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.benkie.hjw.R;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.dialog.BaseDialog;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.ui.BaseActivity;
import com.benkie.hjw.ui.MainActivity;
import com.benkie.hjw.utils.ToastUtil;

import java.io.IOException;

import androidkun.com.versionupdatelibrary.entity.VersionUpdateConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by 37636 on 2018/6/8.
 */

public abstract class UpdataVersion {
    private Activity context;
    public static String  URL ="";
    public UpdataVersion(Activity context){
        this.context = context;
    }

    public abstract void isUpdateVersion(boolean isUp);

    public void getVersion() {
        Call call = Http.links.upDataVersion();
        Http.http.call(context, call, false, new Http.JsonCallback() {
            @Override
            public void onResult(String json, String error) {
                try {
                JSONObject jsObj = JSON.parseObject(json);
                String apkUrl = jsObj.getString("apkUrl");
                String description = jsObj.getString("description");
                int version = jsObj.getIntValue("version");
                    isUpdata(apkUrl,description,version);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    isUpdateVersion(false);
                }
            }

            @Override
            public void onFail(String error) {
                ToastUtil.showInfo(context, error);
                isUpdateVersion(false);
            }
        });
    }

    private void isUpdata(final String apkUrl, String description, final int versionCode) throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
        if (versionCode>packageInfo.versionCode){
            BaseDialog.showMag(context, "版本更新",
                    description, "立即更新",
                    new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getId()==R.id.iv_close){
                        context.finish();
                    }else {
                        Toast.makeText(context,"还居网正在下载...",Toast.LENGTH_SHORT).show();
                        downLoad(apkUrl,versionCode+"");
                        context.finish();
                    }
                }
            });
        }else {
            isUpdateVersion(false);
        }
    }


    private void downLoad(String url,String version){
        VersionUpdateConfig.getInstance()//获取配置实例
                .setContext(context)//设置上下文
                .setDownLoadURL(url)//设置文件下载链接
                .setNewVersion(version)//设置即将下载的APK的版本号,避免重复下载
                //.setFileSavePath(savePath)//设置文件保存路径（可不设置）
                .setNotificationIconRes(R.mipmap.ic_huanju)//设置通知图标
                .setNotificationSmallIconRes(R.mipmap.ic_huanju)//设置通知小图标
                .setNotificationTitle("还居网")//设置通知标题
                .startDownLoad();//开始下载
    }
}
