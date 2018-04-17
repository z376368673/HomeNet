package com.benkie.hjw.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benkie.hjw.R;

/**
 * Created by 37636 on 2018/1/23.
 */

public class CheckTextView2 extends LinearLayout implements Checkable{

    TextView name;
    private CheckBox mCheckBox;
    int checkedColor_n ;
    int checkedColor_p;


    public CheckTextView2(Context context) {
        this(context, null);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public CheckTextView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public CheckTextView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        //将加载出来的View添加到当前View层级中去。
        //有两种方案，一种是加载布局时将rootView传进去，或直接使用addView添加进去
        //View v = mLayoutInflater.inflate(R.layout.layout_custom_ctv, null);
        View v = mLayoutInflater.inflate(R.layout.check_textview_up_down, this, true);
        name = v.findViewById(R.id.tv_name);
        mCheckBox = v.findViewById(R.id.iv_isChecked);
        checkedColor_n = Color.parseColor("#666666");
        checkedColor_p = Color.parseColor("#0ECCB8");
        //this.addView(v);
    }

    public void setCheckedColor_n(int checkedColor_n) {
        this.checkedColor_n = checkedColor_n;
    }

    public void setCheckedColor_p(int checkedColor_p) {
        this.checkedColor_p = checkedColor_p;
    }

    @Override
    public void setChecked(boolean checked) {
        mCheckBox.setChecked(checked);
        if (mCheckBox.isChecked())
            name.setTextColor(checkedColor_p);
        else
            name.setTextColor(checkedColor_n);
    }

    @Override
    public boolean isChecked() {
        return mCheckBox.isChecked();
    }

    @Override
    public void toggle() {
        mCheckBox.toggle();
    }

    public void setTitle(String title) {
        name.setText(title);
    }

    @Override
    public void setActivated(boolean activated) {
        super.setActivated(activated);
    }

}