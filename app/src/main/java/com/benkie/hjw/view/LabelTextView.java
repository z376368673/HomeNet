package com.benkie.hjw.view;

import android.content.Context;
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

public class LabelTextView extends LinearLayout {

    TextView tv_text1;
    TextView tv_text2;

    public LabelTextView(Context context) {
        this(context, null);
    }

    public LabelTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        //将加载出来的View添加到当前View层级中去。
        //有两种方案，一种是加载布局时将rootView传进去，或直接使用addView添加进去
        //View v = mLayoutInflater.inflate(R.layout.layout_custom_ctv, null);
        View v = mLayoutInflater.inflate(R.layout.view_lable_text, this, true);
        tv_text1 = v.findViewById(R.id.tv_text1);
        tv_text2 = v.findViewById(R.id.tv_text2);
        //this.addView(v);
    }
    public void setTextSize(int size){
        if (tv_text1!=null)
            tv_text1.setTextSize(size);
            tv_text2.setTextSize(size-3);
    }
    public String getTv_text1() {
        if (tv_text1!=null)
        return tv_text1.getText().toString();
        return "";
    }

    public String getTv_text2() {
        if (tv_text2!=null)
            return tv_text2.getText().toString();
        return "";
    }

    public void setContent(String text1, String text2) {
        tv_text1.setText(text1);
        if (text2.equals("1")){
            tv_text2.setText("资格证");
        }else {
            tv_text2.setText("");
        }

    }
    @Override
    public void setActivated(boolean activated) {
        super.setActivated(activated);
    }
}