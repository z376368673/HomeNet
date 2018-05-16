package com.benkie.hjw.popwindow;

/**
 * Created by 37636 on 2018/1/24.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.benkie.hjw.R;
import com.benkie.hjw.bean.PopBean;

import java.util.List;

public class ListPopWindow extends BasePopWindow implements AdapterView.OnItemClickListener {
    private ListView conentView;
    private Context context;
    private MyAdadpter myAdadpter;

    public ListPopWindow(final Activity context) {
        super(context);
        this.context = context;
        conentView = new ListView(context);
        initView(conentView);

        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);

        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xEE333333);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        //this.setBackgroundDrawable(null);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.AnimationPreview);
    }

    private void initView(ListView conentView) {
        conentView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        myAdadpter = new MyAdadpter(context);
        conentView.setOnItemClickListener(this);
        conentView.setAdapter(myAdadpter);
        conentView.setDividerHeight(0);
        conentView.setPadding(0, 0, 0, 0);
        conentView.setBackgroundResource(R.color.transparent5);
    }

    public void setData(List<? extends PopBean> list) {
        myAdadpter.addAll(list);
        myAdadpter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        PopBean popBean = (PopBean) adapterView.getAdapter().getItem(i);
        if (popWindowCheckedListener != null)
            popWindowCheckedListener.onPopWindowCheckedListener(popBean);
        dismiss();
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent,0,0);
        } else {
            this.dismiss();
        }
    }

    PopWindowCheckedListener popWindowCheckedListener;

    public void setPopWindowCheckedListener(PopWindowCheckedListener popWindowCheckedListener) {
        this.popWindowCheckedListener = popWindowCheckedListener;
    }

    public interface PopWindowCheckedListener {
        void onPopWindowCheckedListener(PopBean popBean);
    }

    private int gravity = Gravity.CENTER_VERTICAL;

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public class MyAdadpter extends ArrayAdapter<PopBean> {

        public MyAdadpter(@NonNull Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LinearLayout linearLayout = new LinearLayout(parent.getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
                View view = new View(parent.getContext());
                view.setBackgroundColor(context.getResources().getColor(R.color.white_f0));
                view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,3));
                linearLayout.addView(view);
                TextView textView = new TextView(parent.getContext());
                textView.setTextColor(context.getResources().getColor(R.color.black_66));
                textView.setPadding(35, 20, 35, 20);
                textView.setTextSize(17);
                textView.setGravity(gravity);
                textView.setBackgroundResource(R.color.white);
                textView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                linearLayout.addView(textView);
                convertView = linearLayout;
                convertView.setTag(textView);
            }
            PopBean item = getItem(position);
            TextView textView = (TextView) convertView.getTag();
            textView.setText(item.getName());
            return convertView;
        }
    }

}