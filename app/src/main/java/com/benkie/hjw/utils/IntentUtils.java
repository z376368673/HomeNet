package com.benkie.hjw.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class IntentUtils {

	public static void goTo(Context context, Class<?> mClass, Bundle extras) {
		Intent intent = new Intent(context, mClass);
		if (extras != null) {
			intent.putExtras(extras);
		}
		context.startActivity(intent);
	}

	public static void goTo(Context context, Class<?> mClass) {
		if (mClass != null && context != null) {
			Intent intent = new Intent(context, mClass);
			context.startActivity(intent);
		}
	}

	public static void goTo(Activity context, Class<?> mClass, int requestCode,
			int flags) {
		if (mClass != null && context != null) {
			Intent intent = new Intent(context, mClass);
			if (flags != -1) {
				intent.setFlags(flags);
			}
			context.startActivityForResult(intent, requestCode);
		}
	}

	public static void goTo(Context context, Class<?> mClass, int flags) {
		Intent intent = new Intent(context, mClass);
		intent.setFlags(flags);
		context.startActivity(intent);
	}

	public static void goTo(Context context, Class<?> mClass, Bundle extras,
			int flags) {
		Intent intent = new Intent(context, mClass);
		intent.setFlags(flags);
		if (extras != null) {
			intent.putExtras(extras);
		}
		context.startActivity(intent);
	}

	public static void goTo(Context context, Intent intent, Class<?> mClass,
			Bundle extras) {
		intent.setClass(context, mClass);
		if (extras != null) {
			intent.putExtras(extras);
		}
		context.startActivity(intent);
	}

	/**
	 * 调用系统浏览器打开页面
	 * 
	 * @param context
	 * @param url
	 */
	public static void goToWebView(Context context, String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClassName("com.android.browser",
				"com.android.browser.BrowserActivity");
		intent.setData(Uri.parse(url));
		context.startActivity(intent);
	}

	public static void sendBroadcast(Context context, String value) {
		Intent intent = new Intent();
		intent.setAction(value);
		context.sendBroadcast(intent);
	}

	public static void sendBroadcast(Context context, String value,
			Bundle bundle) {
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.setAction(value);
		context.sendBroadcast(intent);
	}

	public static void goToHome(Context context) {
		Intent home = new Intent(Intent.ACTION_MAIN);
		home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		home.addCategory(Intent.CATEGORY_HOME);
		context.startActivity(home);
	}

	public static void dialPhone(Context context, String phone) {
		try {
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ phone));
			context.startActivity(intent);
		} catch (Exception e) {
			LogUtils.showErrorMessage(e);
		}
	}

}
