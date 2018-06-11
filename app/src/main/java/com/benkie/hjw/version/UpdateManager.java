package com.benkie.hjw.version;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.icu.text.DecimalFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.benkie.hjw.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.umeng.socialize.utils.ContextUtil.getPackageName;
import static com.umeng.socialize.utils.Log.TAG;

public class UpdateManager {

    private Activity mContext;

    // 返回的安装包url

    private Dialog noticeDialog;

    private Dialog downloadDialog;
    /* 下载包安装路径 */
    private String savePath = "";

    private String saveFileName = "";

    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;
    private TextView tv_progress;

    private static final int DOWN_UPDATE = 1;

    private static final int DOWN_OVER = 2;

    private int progress;

    private Thread downLoadThread;

    private boolean interceptFlag = false;

    private int localVersion, serverVersion, isfoucs;

    private String ApkUrl;
    private String des;

    public void setApkUrl(String apkUrl) {
        ApkUrl = apkUrl;
    }

    public void setDes(String des) {
        this.des = des;
    }

    private String convertSizeToM(long size) {
        String sizeStr = "0M";
        if (size < 1024 * 1024) {
            sizeStr = size / 1024 + "K";
        } else {
            sizeStr = size / 1024 / 1024 + "M";
        }
        return sizeStr;
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    if (totalSize == 0 || progress == 0) {
                        tv_progress.setText("正在计算...");
                    } else {
                        //String prog = String.format("已下载：%d %", totalSize * 100 / progress);
                        //tv_progress.setText("已下载: "+ convertSizeToM(progress)+"/"+convertSizeToM((int) totalSize));
                        tv_progress.setText("已下载: " + progress + "/%");
                    }
                    break;
                case DOWN_OVER:
                    downloadDialog.cancel();
                    installApk();
                    break;
                default:
                    break;
            }
        }
    };

    public UpdateManager(Activity context, int serverVersion, int isfoucs) {
        this.mContext = context;
        this.serverVersion = serverVersion;
        this.isfoucs = isfoucs;
    }

    // 外部接口让主Activity调用
    public void checkUpdateInfo() {

        PackageManager manager;
        PackageInfo info = null;
        manager = mContext.getPackageManager();
        try {
            info = manager.getPackageInfo(mContext.getPackageName(), 0);
            localVersion = info.versionCode;
            if (serverVersion > localVersion) {
                showNoticeDialog();
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void showNoticeDialog() {
        Builder builder = new Builder(mContext);
        builder.setTitle("版本更新");
        builder.setCancelable(false);
        builder.setMessage(des);
        builder.setPositiveButton("立即下载", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });
        builder.setNegativeButton("退出", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setLoadVersion();
                if (isfoucs == 1) {
                    mContext.finish();
                }
                dialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }

    private void setLoadVersion() {
        // MyApplication.setVersion(false);
    }

    private void showDownloadDialog() {
        Builder builder = new Builder(mContext);
        builder.setTitle("软件版本更新");
        builder.setCancelable(false);

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);
        tv_progress = (TextView) v.findViewById(R.id.tv_progress);

        builder.setView(v);
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
                mContext.finish();
            }
        });
        downloadDialog = builder.create();
        downloadDialog.setCancelable(false);
        downloadDialog.show();
        downloadApk();
    }

    private long totalSize = 0;
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(ApkUrl);
                Log.e("apkurl", ApkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                int length = conn.getContentLength();
                totalSize = length;
                InputStream is = conn.getInputStream();

                //savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);;
                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                ;
                if (!file.exists()) {
                    boolean b = file.mkdir();
                    Log.e("创建文件:", b + "=>" + file.getPath());
                }
                savePath = file.getAbsolutePath();
                saveFileName = ApkUrl.substring(ApkUrl.lastIndexOf("/"));
                saveFileName = savePath + saveFileName;
                File ApkFile = new File(saveFileName);
                if (ApkFile.exists()) {
                    ApkFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载.

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    /**
     * 下载apk
     */

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     */
    public void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent install = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = mContext.getPackageManager().canRequestPackageInstalls();
            if (!b) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                mContext.startActivityForResult(intent, 10012);
                return;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 调用系统自带安装环境
            Log.w(TAG, "版本大于 N ，开始使用 fileProvider 进行安装");
            install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mContext, "com.benkie.hjw.fileProvider", apkfile);
            install.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
        }
        mContext.startActivity(install);
        mContext.finish();
        //install(apkfile.getPath());
    }

}
