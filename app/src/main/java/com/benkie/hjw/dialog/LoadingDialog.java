package com.benkie.hjw.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.benkie.hjw.R;
import com.wang.avi.AVLoadingIndicatorView;

/*
 *  创建者:     ZH
 *  创建时间:   2018/03/08  9:32
 *  描述:  
 */
public class LoadingDialog extends Dialog implements View.OnClickListener ,DialogInterface.OnDismissListener{

    private ImageView iv_loading;
    private AVLoadingIndicatorView avi;
    Context context;
    public LoadingDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }
    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loading);
        Window window = getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        iv_loading = findViewById(R.id.iv_loading);
        Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.rotate_animtion);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        iv_loading.startAnimation(operatingAnim);
        setOnDismissListener(this);
        setCanceledOnTouchOutside(false);
        avi = findViewById(R.id.avi);
        avi.show();

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        avi.hide();
    }
}
