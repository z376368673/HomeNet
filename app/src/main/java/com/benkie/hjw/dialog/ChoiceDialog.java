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
public class ChoiceDialog extends Dialog implements View.OnClickListener {


    private TextView tv_edit;
    private TextView tv_del;
    private TextView tv_share;
    private TextView tv_add_img;
    private Handler handler;
    Context context;
    public ChoiceDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public ChoiceDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ChoiceDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        setContentView(R.layout.dialog_choice);
        tv_edit = (TextView) findViewById(R.id.tv_edit);
        tv_del = (TextView) findViewById(R.id.tv_del);
        tv_share = (TextView) findViewById(R.id.tv_share);
        tv_add_img = (TextView) findViewById(R.id.tv_add_img);

        tv_edit.setOnClickListener(this);
        tv_del.setOnClickListener(this);
        tv_add_img.setOnClickListener(this);
        tv_share.setOnClickListener(this);
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
        if(v == tv_edit){
            handler.sendEmptyMessage(1);
        }else if(v == tv_del){
            handler.sendEmptyMessage(2);
        }else if(v == tv_add_img){
            handler.sendEmptyMessage(3);
        }else if (v==tv_share){
            handler.sendEmptyMessage(4);
        }
        dismiss();
    }
}
