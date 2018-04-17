package com.benkie.hjw.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.benkie.hjw.R;
import com.benkie.hjw.bean.Picture;
import com.benkie.hjw.db.DataHpler;
import com.benkie.hjw.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Welcome2Activity extends BaseActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome2);
        ButterKnife.bind(this);
        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.welcome1);
        list.add(R.mipmap.welcome2);
        initViewPager(list);
    }

    private void initViewPager(List<Integer> list) {
        WorksPagerAdapter adapter = new WorksPagerAdapter(list);
        viewPager.setAdapter(adapter);
    }

    private class WorksPagerAdapter extends PagerAdapter {
        List<Integer> list;

        public WorksPagerAdapter(List<Integer> list) {
            this.list = list;
        }

        public List<Integer> getList() {
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
            View view = View.inflate(mActivity, R.layout.view_pager_img_welcome, null);
            ImageView iv = (ImageView) view.findViewById(R.id.iv_img);
            iv.setImageResource(list.get(position));
            container.addView(view);
            final int p = position;
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (p == 1) {
                        DataHpler.setFirstShow("firstIn");
                        toStartAct(MainActivity.class);
                        finish();
                    }
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
