package com.benkie.hjw.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.benkie.hjw.R;
import com.benkie.hjw.bean.Category;

/**
 * Created by 37636 on 2018/1/23.
 */

public class CategoryAdapter extends ArrayAdapter<Category> {
    private static int position = 0;
    public static Context context;
    private static Category category;
    private static String selectedText = "";

    public CategoryAdapter(@NonNull Context context) {
        super(context, 0);
        this.context = context;
    }

    public void setCheck(Category category) {
        this.category = category;
        this.notifyDataSetChanged();
    }

    public int getPosition(@Nullable String item) {
        for (int i = 0; i <getCount() ; i++) {
           Category category = getItem(i);
           if (category.getName().equals(item)){
               return i;
           }
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_horizontal_list, null);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.tv_name);
            holder.checkBox = convertView.findViewById(R.id.iv_isChecked);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Category item = getItem(position);
        holder.initData(item, position);
        return convertView;
    }

    static class ViewHolder {
        TextView name;
        CheckBox checkBox;

        void initData(final Category item, int p) {
            name.setText(item.getName());
            if ( category.getId() == item.getId()) {
                checkBox.setChecked(true);
                name.setTextColor(context.getResources().getColor(R.color.colorMain));
            } else {
                checkBox.setChecked(false);
                name.setTextColor(context.getResources().getColor(R.color.black_33));
            }
        }
    }
}
