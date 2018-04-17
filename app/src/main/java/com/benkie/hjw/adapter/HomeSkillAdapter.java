package com.benkie.hjw.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benkie.hjw.R;
import com.benkie.hjw.bean.HomeProductBean;
import com.benkie.hjw.bean.HomeSkillBean;
import com.benkie.hjw.bean.SkillBean;
import com.benkie.hjw.constants.Constants;
import com.benkie.hjw.utils.MapDistance;
import com.benkie.hjw.utils.Tools;
import com.benkie.hjw.view.LabelTextView;

import am.widget.wraplayout.WrapLayout;

/**
 * Created by 37636 on 2018/1/23.
 */

public class HomeSkillAdapter extends ArrayAdapter<HomeSkillBean> {
    static Context context;

    public HomeSkillAdapter(@NonNull Context context) {
        super(context, 0);
        this.context = context;
    }

    ViewHolder holder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.adapter_home_skill_item, null);
            holder = new ViewHolder();
            holder.initView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeSkillBean item = getItem(position);
        holder.initData(item, position);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_img;
        TextView tv_name;
        TextView tv_type;
        LinearLayout ll_add;
        TextView tv_introduce; //介绍
        TextView tv_address;
        TextView tv_distance;//距离

        void initView(View convertView) {
            iv_img = convertView.findViewById(R.id.iv_img);
            tv_name = convertView.findViewById(R.id.tv_name);
            tv_type = convertView.findViewById(R.id.tv_type);
            ll_add = convertView.findViewById(R.id.ll_add);
            tv_introduce = convertView.findViewById(R.id.tv_introduce);
            tv_address = convertView.findViewById(R.id.tv_address);
            tv_distance = convertView.findViewById(R.id.tv_distance);
        }

        void initData(final HomeSkillBean item, int p) {
            ll_add.removeAllViews();
            int size = item.getSkills().size();
            if (size>2) {
                size = 2;
                TextView dian = new TextView(context);
                dian.setText("...");
                dian.setTextColor(context.getResources().getColor(R.color.black_66));
                ll_add.addView(dian);
            }
            for (int i = 0; i < size; i++) {
                SkillBean skillBean = item.getSkills().get(i);
                LabelTextView labelTextView = new LabelTextView(context);
                labelTextView.setContent(skillBean.getName(), skillBean.getCertificate() + "");
                labelTextView.setTextSize(13);
                labelTextView.setPadding(0, 5, 10, 5);
                ll_add.addView(labelTextView, 0);
            }

            Tools.loadHeadImg(context, iv_img, item.getImgUrl());
            tv_name.setText(item.getName());
            if (item.getServeType() == 0) {
                tv_type.setText(" (个人)");
            } else {
                tv_type.setText(" (团队)");
            }
            String distance = item.getDistance() > 1000 ? item.getDistance() / 1000 + "km" : item.getDistance() + "m";
            tv_distance.setText(distance);
            tv_address.setText(item.getAddress());
            tv_introduce.setText(item.getDescribes());
        }
    }
}
