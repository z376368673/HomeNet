package com.benkie.hjw.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.benkie.hjw.R;
import com.benkie.hjw.bean.HomeProductBean;
import com.benkie.hjw.bean.Picture;
import com.benkie.hjw.bean.TypeBean;
import com.benkie.hjw.utils.Tools;

import java.util.List;

/**
 * Created by 37636 on 2018/1/23.
 */

public class HomeProductAdapter extends ArrayAdapter<HomeProductBean> {
     Context context;

    public HomeProductAdapter(@NonNull Context context) {
        super(context, 0);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_home_product, null);
            holder = new ViewHolder();
            holder.initView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeProductBean item = getItem(position);
        holder.initData(item, position);
        return convertView;//itemPoint
    }

     class ViewHolder {
        ImageView iv_img;
        TextView tv_name;
        TextView tv_type;
        TextView tv_address;
        TextView tv_read;
        TextView tv_zan;

        void initView(View convertView) {
            iv_img = convertView.findViewById(R.id.iv_img);
            tv_name = convertView.findViewById(R.id.tv_name);
            tv_type = convertView.findViewById(R.id.tv_type);
            tv_address = convertView.findViewById(R.id.tv_address);
            tv_read = convertView.findViewById(R.id.tv_read);
            tv_zan = convertView.findViewById(R.id.tv_zan);
        }
        void initData(final HomeProductBean item, int p) {
            Picture picture = item.getImgs();
            if (picture != null) {
                Tools.loadImg(context, iv_img, picture.getPicture());
                List<TypeBean> services = picture.getService();
                tv_type.setText(getServiceString(services));
            } else {
                iv_img.setImageResource(R.mipmap.iv_no_img);
                tv_type.setText("");
            }
            tv_name.setText(item.getName());
            tv_address.setText(item.getCity()+item.getAddress());
            tv_read.setText(item.getReadCount()+"");
            tv_zan.setText(item.getItemPoint()+"");
        }
        public String getServiceString(List<TypeBean> services) {
            String str = "";
            for (int i = 0; i < services.size(); i++) {
                str += services.get(i).getName() + "/";
            }
            if (str.length() > 1) {
                str = str.substring(0, str.length() - 1);
            }
            return str;
        }
    }
}
