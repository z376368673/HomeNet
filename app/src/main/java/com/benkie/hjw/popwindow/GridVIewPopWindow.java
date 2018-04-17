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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.benkie.hjw.R;
import com.benkie.hjw.adapter.GridTextAdapter;
import com.benkie.hjw.bean.PopBean;

import java.util.List;

public class GridVIewPopWindow extends BasePopWindow implements AdapterView.OnItemClickListener{
    View conentView;
    private GridView gridView;
    private Context context;
    private GridTextAdapter myAdadpter;
    public GridVIewPopWindow(final Activity context) {
        super(context);
        this.context = context;
        conentView = LayoutInflater.from(context).inflate(R.layout.pop_gird_view,null);
        initView(conentView);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);

        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(33333333);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        //this.setBackgroundDrawable(null);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.AnimationPreview);
    }

    private void initView(View conentView) {
        gridView =conentView.findViewById(R.id.gridview);
        gridView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        gridView.setBackgroundResource(R.color.white);
        myAdadpter = new GridTextAdapter(context);
        gridView.setOnItemClickListener(this);
        gridView.setAdapter(myAdadpter);
    }

    public void  setData(List<? extends PopBean> list){
        if (list==null)return;
        myAdadpter.addAll(list);
        myAdadpter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        PopBean popBean = (PopBean) adapterView.getAdapter().getItem(i);
        if (popWindowCheckedListener!=null)
            popWindowCheckedListener.onPopWindowCheckedListener(popBean);
        dismiss();
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }
    PopWindowCheckedListener popWindowCheckedListener;

    public void setPopWindowCheckedListener(PopWindowCheckedListener popWindowCheckedListener) {
        this.popWindowCheckedListener = popWindowCheckedListener;
    }

    public interface PopWindowCheckedListener{
        void onPopWindowCheckedListener(PopBean popBean);
    }
    private  int gravity =  Gravity.CENTER_VERTICAL;

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public class GridTextAdapter extends ArrayAdapter<PopBean> {

        private Context context;

        public GridTextAdapter(@NonNull Context context) {
            super(context, 0);
            this.context = context;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view==null)
                view = LayoutInflater.from(context).inflate(R.layout.adapter_mygridview_item, null);
            TextView item_text = (TextView) view.findViewById(R.id.text_item);
            item_text.setText(getItem(position).getName());
            item_text.setTag(getItem(position).getId());
            return view;
        }


    }

}