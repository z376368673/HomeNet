package com.benkie.hjw.popwindow;

/**
 * Created by 37636 on 2018/1/24.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.benkie.hjw.R;
import com.benkie.hjw.bean.PopBean;

import java.util.List;

public abstract class BaseListPopWindow<T extends Object> extends BasePopWindow implements AdapterView.OnItemClickListener {
    private ListView conentView;
    private Context context;
    private MyAdadpter myAdadpter;
    private T data;

    public BaseListPopWindow(final Activity context) {
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
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        //ColorDrawable dw = new ColorDrawable(33333333);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        //this.setBackgroundDrawable(dw);
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

    public ArrayAdapter getAdadpter() {
        return myAdadpter;
    }

    public void setData(List<T> list) {
        myAdadpter.clear();
        myAdadpter.addAll(list);
        myAdadpter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        T object = (T) adapterView.getAdapter().getItem(i);
        if (popWindowCheckedListener != null)
            popWindowCheckedListener.onPopWindowCheckedListener(object);
        dismiss();
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            if (android.os.Build.VERSION.SDK_INT >=android.os.Build.VERSION_CODES.N) {
                this.showAtLocation(parent, Gravity.NO_GRAVITY, 0, 0);
            } else {
                this.showAsDropDown(parent, 0, 0);
            }
        } else {
            this.dismiss();
        }
    }

    PopWindowCheckedListener popWindowCheckedListener;

    public void setPopWindowCheckedListener(PopWindowCheckedListener popWindowCheckedListener) {
        this.popWindowCheckedListener = popWindowCheckedListener;
    }

    public interface PopWindowCheckedListener {
        void onPopWindowCheckedListener(Object t);
    }

    public abstract View getViews(int position, T t, View convertView, ViewGroup parent);

    public class MyAdadpter extends ArrayAdapter<T> {

        public MyAdadpter(@NonNull Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = getViews(position, getItem(position), conentView, parent);
            return v;
        }
    }

}