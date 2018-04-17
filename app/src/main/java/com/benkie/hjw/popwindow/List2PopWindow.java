package com.benkie.hjw.popwindow;

/**
 * Created by 37636 on 2018/1/24.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.benkie.hjw.R;
import com.benkie.hjw.bean.PopBean;
import com.benkie.hjw.bean.TypeBean;
import com.benkie.hjw.utils.LogUtils;

import java.util.List;

public class List2PopWindow extends BasePopWindow {

    private View conentView;
    private ListView listView1;
    private ListView listView2;
    private Activity context;
    private MyAdadpter myAdadpter;
    private MyAdadpter2 myAdadpter2;
    private Window mWindow;//当前Activity 的窗口

    public List2PopWindow(final Activity context) {
        super(context);
        this.context = context;
        conentView = View.inflate(context,R.layout.pop_windows_list_2, null);
        initView(conentView);

        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        /** 北京变暗效果 **/
//        mWindow = context.getWindow();
//        WindowManager.LayoutParams lp = mWindow.getAttributes();
//        lp.alpha = 0.4f;
//        mWindow.setAttributes(lp);

        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(00000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        //this.setBackgroundDrawable(null);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.AnimationPreview);

    }

    @Override
    public void dismiss() {
        super.dismiss();
//        WindowManager.LayoutParams lp = mWindow.getAttributes();
//        lp.alpha = 1f;
//        mWindow.setAttributes(lp);
    }

    private  void initView(View conentView){

        listView1 = conentView.findViewById(R.id.listView1);
        listView2 = conentView.findViewById(R.id.listView2);
        listView1.setDividerHeight(0);
        listView2.setDividerHeight(0);

        myAdadpter = new MyAdadpter(context);
        listView1.setAdapter(myAdadpter);
        listView1.setOnItemClickListener(listener1);

        myAdadpter2 = new MyAdadpter2(context);
        listView2.setAdapter(myAdadpter2);
        listView2.setOnItemClickListener(listener2);
    }
    AdapterView.OnItemClickListener listener1 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            TypeBean typeBean = (TypeBean) adapterView.getAdapter().getItem(i);
            if (typeBean == null) return;
            List<TypeBean.Tclass> tclist = typeBean.getTcList();
            if (tclist==null) return;
            upAdapter(tclist,i);
        }
    };

    private void upAdapter(List<? extends PopBean> list,int p){
        myAdadpter.selected(p);
        myAdadpter2.clear();
        myAdadpter2.addAll(list);
        myAdadpter2.selected(-1);
    }

    AdapterView.OnItemClickListener listener2 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            TypeBean.Tclass  tclass = (TypeBean.Tclass) adapterView.getAdapter().getItem(i);
            if (tclass==null)return;
            myAdadpter2.selected(i);
            if (popWindowCheckedListener!=null)
                popWindowCheckedListener.onPopWindowCheckedListener(tclass);
            dismiss();
        }
    };

    public void setData(List<TypeBean> list) {
        myAdadpter.addAll(list);
        myAdadpter.notifyDataSetChanged();
        if (list!=null){
           upAdapter(list.get(0).getTcList(),0);
        }
//        LayoutParams layoutParams1 = listView1.getLayoutParams();
//        LayoutParams layoutParams2 = listView2.getLayoutParams();
//        layoutParams2.height = layoutParams1.height;
//        listView2.setLayoutParams(layoutParams2);
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

    public interface PopWindowCheckedListener {
        void onPopWindowCheckedListener(PopBean popBean);
    }

    public class MyAdadpter extends ArrayAdapter<PopBean> {
        private int selectePos = -1;

        public MyAdadpter(@NonNull Context context) {
            super(context, 0);
        }

        public void selected(int selectePos) {
            this.selectePos = selectePos;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.pop_windows_item_list_2, null);
            }
            TextView textView1 = convertView.findViewById(R.id.text1);
            TextView textView2 = convertView.findViewById(R.id.text2);
            PopBean item = getItem(position);
            textView1.setText(item.getName());
            textView2.setText(item.getName2());
            textView2.setVisibility(View.GONE);
            textView1.setBackgroundResource(R.color.white);
            if (selectePos == position) {
                textView1.setBackgroundResource(R.color.white_f4);
                textView1.setTextColor(ContextCompat.getColor(context, R.color.colorMain));
                LogUtils.e("position1--->",position);
            } else {
                textView1.setBackgroundResource(R.color.white);
                textView1.setTextColor(ContextCompat.getColor(context, R.color.black_66));
            }
            return convertView;
        }
    }

    public class MyAdadpter2 extends ArrayAdapter<PopBean> {
        private int selectePos = -1;

        public void selected(int selectePos) {
            this.selectePos = selectePos;
            notifyDataSetChanged();
        }

        public MyAdadpter2(@NonNull Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.pop_windows_item_list_2, null);
            }
            TextView textView1 = convertView.findViewById(R.id.text1);
            TextView textView2 = convertView.findViewById(R.id.text2);
            PopBean item = getItem(position);
            textView1.setText(item.getName());
            textView2.setText(item.getNum()+"");
            textView2.setVisibility(View.INVISIBLE);
            if (selectePos == position) {
                LogUtils.e("position2--->",position);
                textView1.setTextColor(ContextCompat.getColor(context, R.color.colorMain));
                textView2.setTextColor(ContextCompat.getColor(context, R.color.colorMain));
            } else {
                textView1.setTextColor(ContextCompat.getColor(context, R.color.black_66));
                textView2.setTextColor(ContextCompat.getColor(context, R.color.black_66));
            }
            return convertView;
        }
    }

}