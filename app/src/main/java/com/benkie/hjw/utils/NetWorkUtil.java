package com.benkie.hjw.utils;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtil {
	
	/**
	 * 无网络提示框
	 * @param context
	 */
	public static void netWorkConnectedDialog(final Context context) {
		// TODO Auto-generated method stub
		Builder dialog=new Builder(context);
		dialog.setTitle("温馨提示：");
		dialog.setMessage("无法连接网络，请设置网络");
		dialog.setPositiveButton("设置网络", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
				context.startActivity(intent);
			}
		});
		dialog.setNeutralButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			dialog.dismiss();	
			}
		});
		dialog.show();
	}
	
	/**
	 * 检测网络
	 * 
	 * @return
	 */
	public static boolean isNetWorkConnected(final Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}
	
}
