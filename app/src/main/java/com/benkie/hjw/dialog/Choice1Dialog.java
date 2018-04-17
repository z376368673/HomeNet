package com.benkie.hjw.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.benkie.hjw.R;

/*
 *  创建者:     ZH
 *  创建时间:   2018/03/08  9:32
 *  描述:  
 */
public class Choice1Dialog extends Dialog implements View.OnClickListener {


    private TextView tv_text1;
    private TextView tv_text2;
    private Handler handler;
    Context context;
    public Choice1Dialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public Choice1Dialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected Choice1Dialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        setContentView(R.layout.dialog_choice1);
        tv_text1 =  findViewById(R.id.tv_text1);
        tv_text2 =  findViewById(R.id.tv_text2);

        tv_text1.setOnClickListener(this);
        tv_text2.setOnClickListener(this);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        if (handler==null)return;
        if(v == tv_text1){
            handler.sendEmptyMessage(1);
        }else if(v == tv_text2){
            handler.sendEmptyMessage(2);
        }
        dismiss();
    }
}
