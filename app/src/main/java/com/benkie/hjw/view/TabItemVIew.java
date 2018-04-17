package com.benkie.hjw.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import me.majiajie.pagerbottomtabstrip.internal.RoundMessageView;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;

/**
 * Created by 37636 on 2018/1/19.
 */

public class TabItemVIew extends NormalItemView {

    private ImageView mIcon;
    private  TextView mTitle;
    private  RoundMessageView mMessages;

    public TabItemVIew(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(me.majiajie.pagerbottomtabstrip.R.layout.item_normal, this, true);

        mIcon = (ImageView) findViewById(me.majiajie.pagerbottomtabstrip.R.id.icon);
        mTitle = (TextView) findViewById(me.majiajie.pagerbottomtabstrip.R.id.title);
        mMessages = (RoundMessageView) findViewById(me.majiajie.pagerbottomtabstrip.R.id.messages);
    }

    public TabItemVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabItemVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
    public TextView getmTitle(){
        return mTitle;
    }
}
