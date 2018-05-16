package com.benkie.hjw.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.benkie.hjw.R;
import com.benkie.hjw.bean.CollectProductBean;
import com.benkie.hjw.bean.HomeProductBean;
import com.benkie.hjw.bean.Picture;
import com.benkie.hjw.bean.TypeBean;
import com.benkie.hjw.utils.Tools;

import java.util.List;

/**
 * Created by 37636 on 2018/1/23.
 */

public abstract class CollectionProductAdapter extends ArrayAdapter<CollectProductBean> {
    static Context context;

    public CollectionProductAdapter(@NonNull Context context) {
        super(context, 0);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_collection_product, null);
            holder = new ViewHolder();
            holder.initView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CollectProductBean item = getItem(position);
        holder.initData(item, position);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_img;
        TextView tv_name;
        TextView tv_type;
        TextView tv_address;
        TextView tv_del;

        void initView(View convertView) {
            iv_img = convertView.findViewById(R.id.iv_img);
            tv_name = convertView.findViewById(R.id.tv_name);
            tv_type = convertView.findViewById(R.id.tv_type);
            tv_address = convertView.findViewById(R.id.tv_address);
            tv_del = convertView.findViewById(R.id.tv_del);
        }
        void initData(final CollectProductBean item, int p) {
            String picture = item.getPicture();
            Tools.loadImg(context, iv_img, picture);
            tv_type.setText(item.getServeName());
            tv_name.setText(item.getName());
            tv_address.setText(item.getCity()+item.getAddress());
            tv_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delCollection(item);
                }
            });
        }
    }
   public abstract void  delCollection(CollectProductBean item);

}
