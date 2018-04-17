package com.benkie.hjw.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benkie.hjw.R;

/**
 * Created by 37636 on 2018/1/20.
 */

public class HeadView extends LinearLayout {
    //返回按钮
    ImageView iv_back;
    //标题
    TextView tv_title;
    //右边得 按钮 / 文字
    TextView right_tv;
    ImageView right_iv;

    Context context;

    public HeadView(Context context) {
        this(context, null, 0);
    }

    public HeadView(Context context, @Nullable AttributeSet attrs) {
        // super(context, attrs);
        this(context, attrs, 0);
    }

    public HeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.head_view, this, true);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        right_tv = findViewById(R.id.right_tv);
        right_iv = findViewById(R.id.right_iv);
    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title){
        tv_title.setText(title);
    }
    /**
     *改变文字
     * @param text
     */
    public void setBtText(String text) {
        right_tv.setText(text);
        right_tv.setVisibility(VISIBLE);
        right_iv.setVisibility(GONE);
    }
    public String getBtText(){
        if (right_tv.getText()!=null)
        return right_tv.getText().toString();
        return "";
    }
    /***
     *改变图标
     * @param id
     */
    public void setBtImg(int id) {
        right_iv.setImageResource(id);
        right_iv.setVisibility(VISIBLE);
        right_tv.setVisibility(GONE);
    }

    /**
     *设置返回按钮点击事件
     * @param activity
     */
    public void setBtBack(final Activity activity){
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }
    /**
     *设置返回按钮点击事件
     */
    public void setBtBackListener(OnClickListener onClickListener){
        iv_back.setOnClickListener(onClickListener);
    }
    /**
     * 设置监听
     * @param onClickListener
     */
    public void setBtClickListener(OnClickListener onClickListener) {
        right_tv.setOnClickListener(onClickListener);
        right_iv.setOnClickListener(onClickListener);
    }
}
