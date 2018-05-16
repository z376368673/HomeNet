package com.benkie.hjw.popwindow;

import android.app.Activity;
import android.app.Application;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.benkie.hjw.application.BaseApp;

/**
 * Created by 37636 on 2018/2/24.
 */

public class BasePopWindow extends PopupWindow {
    Activity activity;
   public BasePopWindow(Activity activity){
       this.activity = activity;
   }


    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
            int[] location = new int[2];
            parent.getLocationOnScreen(location);
            y = (int) (location[1] + parent.getMeasuredHeight())+y +getStatusBarHeight();
        }
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void showAsDropDown(View anchor,int x,int y) {
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.N) {

            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
        }
        super.showAsDropDown(anchor,x,y);
    }

    private int getStatusBarHeight() {
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }
}
