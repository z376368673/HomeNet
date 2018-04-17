package com.benkie.hjw.view;

/**
 * Created by 37636 on 2018/1/26.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by fuweiwei on 2016/1/8.
 */
public class MyGridView extends GridView {
    public MyGridView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}