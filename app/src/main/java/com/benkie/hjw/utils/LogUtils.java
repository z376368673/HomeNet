package com.benkie.hjw.utils;


import android.content.Context;
import android.util.Log;

public class LogUtils {
	private static boolean isShowMessage = true;

	public static void e(String tag, Object value) {
		if (isShowMessage&&value!=null) {
			Log.e(tag, String.valueOf(value));
		}
	}

	public static void showErrorMessage(Throwable e) {
		if (isShowMessage) {
			e.printStackTrace();
		}
	}
	public static void e(Context context, Object value) {
		if (isShowMessage&&value!=null) {
			Log.e(context.getClass().getName(), String.valueOf(value));
		}
	}
	public static void e(Class clas, Object value) {
		if (isShowMessage&&value!=null) {
			Log.e(clas.getName(), String.valueOf(value));
		}
	}
}
