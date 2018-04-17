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

public class CheckTextView1 extends LinearLayout implements Checkable {

    TextView address;
    TextView address_all;
    private CheckBox mCheckBox;

    public CheckTextView1(Context context) {
        this(context, null);
    }

    public CheckTextView1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckTextView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        //将加载出来的View添加到当前View层级中去。
        //有两种方案，一种是加载布局时将rootView传进去，或直接使用addView添加进去
        //View v = mLayoutInflater.inflate(R.layout.layout_custom_ctv, null);
        View v = mLayoutInflater.inflate(R.layout.item_list_map_address, this, true);
        address = v.findViewById(R.id.tv_address);
        address_all = v.findViewById(R.id.tv_address_all);
        mCheckBox = v.findViewById(R.id.iv_isChecked);
        //this.addView(v);
    }
    public void setNoChecked(boolean b){
        if (b)
        mCheckBox.setVisibility(INVISIBLE);
        else mCheckBox.setVisibility(VISIBLE);
    }

    public void setmCheckBox(boolean isCheck) {
        mCheckBox.setChecked(isCheck);
    }

    @Override
    public void setChecked(boolean checked) {
        mCheckBox.setChecked(checked);
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
        address.setText(title);
    }
    public void setContent(String conten) {
        address_all.setText(conten);
    }
    @Override
    public void setActivated(boolean activated) {
        super.setActivated(activated);
    }
}