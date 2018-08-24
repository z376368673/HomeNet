package com.benkie.hjw.popwindow;

/**
 * Created by 37636 on 2018/1/24.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.benkie.hjw.R;
import com.benkie.hjw.bean.Picture;
import com.benkie.hjw.bean.PopBean;
import com.benkie.hjw.utils.Tools;
import com.benkie.hjw.view.MutipleTouchViewPager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class ImagePopWindow extends PopupWindow implements AdapterView.OnItemClickListener {
    private MutipleTouchViewPager conentView;
    private Context context;
    private WorksPagerAdapter myAdadpter;

    public ImagePopWindow(final Activity context) {
        super(context);
        this.context = context;
        conentView = new MutipleTouchViewPager(context);
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
        ColorDrawable dw = new ColorDrawable(33333333);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        //this.setBackgroundDrawable(null);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.AnimationPreview);
    }

    private void initView(ViewPager conentView) {
        myAdadpter = new WorksPagerAdapter();
        conentView.setAdapter(myAdadpter);
        conentView.setBackgroundResource(R.color.transparent5);

    }

    public void setData(List<Picture> list) {
        myAdadpter.setList(list);
        myAdadpter.notifyDataSetChanged();

    }

    public void setCurrentItem(int index) {
        if (conentView != null)
            conentView.setCurrentItem(index);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        PopBean popBean = (PopBean) adapterView.getAdapter().getItem(i);
        if (popWindowCheckedListener != null)
            popWindowCheckedListener.onPopWindowCheckedListener(popBean);
        dismiss();
    }

    public void showPopupWindow(View parent) {
            this.showAtLocation(parent,0,0,0);
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

    private class WorksPagerAdapter extends PagerAdapter {
        List<Picture> list = new ArrayList<>();

        public WorksPagerAdapter() {

        }

        public void setList(List<Picture> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public List<Picture> getList() {
            return list;
        }

        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(context, R.layout.view_pager_img, null);
            PhotoView iv =  view.findViewById(R.id.iv_img);
            Picture picture = list.get(position);
            Picasso.get().load(picture.getPicture()).into(iv);
            //Tools.loadImg(context, iv, picture.getPicture());
            container.addView(view);
            final int p = position;
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

}