package com.benkie.hjw.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.benkie.hjw.R;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.net.Http;
import com.benkie.hjw.utils.PrefUtils;
import com.benkie.hjw.utils.StatusBarUtils;
import com.benkie.hjw.utils.ToastUtil;
import com.benkie.hjw.utils.Tools;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/22.
 */

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected  Activity mActivity;
    public static final int REQUEST_CODE_CHOOSE = 23;
    public boolean isModify  = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //StatusBarUtils.setStatusBarColor(this, R.color.colorMain);
        mActivity = this;
    }

    public static void toLogin(Context context) {
        /** 除了目标activity  清除其他所有activity
         *setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
         */
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void toLogin(Context context,boolean isBack) {
        /** 除了目标activity  清除其他所有activity
         *setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
         */
        Intent intent = new Intent(context, LoginActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("isBack",isBack);
        context.startActivity(intent);
    }

    public  void toStartAct(Class clas) {
        Intent intent = new Intent(mActivity, clas);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mActivity.startActivity(intent);
    }

    public static boolean islogin(Context context) {
        if (!DataHpler.islogin()) {
            toLogin(context);
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (Tools.isFastClick()){
            //ToastUtil.showInfo(mActivity,"您点击的太快了");
            return;
        }
    }
}
