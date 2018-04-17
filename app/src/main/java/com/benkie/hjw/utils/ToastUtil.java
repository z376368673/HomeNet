package com.benkie.hjw.utils;


import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	public static void showInfo(Context context, int msg) {
		Toast t = Toast.makeText(context, context.getString(msg),
				Toast.LENGTH_SHORT);
		t.show();
	}

	public static void showInfo(Context context, int msg, int gravity) {
		Toast t = Toast.makeText(context, context.getString(msg),
				Toast.LENGTH_SHORT);
		t.setGravity(gravity, 0, 0);
		t.show();
	}

	public static void showInfo(Context context, int res, Object... msg) {
		Toast t = Toast.makeText(context,
				context.getResources().getString(res, msg), Toast.LENGTH_SHORT);
		t.show();
	}

	public static void showInfo(Context context, String msg) {
		Toast t = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		t.show();
	}

	public static void showInfo(Context context, String msg, int gravity) {
		Toast t = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		t.setGravity(gravity, 0, 0);
		t.show();
	}
	
}
