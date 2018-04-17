package com.benkie.hjw.adapter;

/**
 * Created by 37636 on 2018/1/26.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.benkie.hjw.R;
import com.benkie.hjw.bean.Category;

import java.util.List;

/**
 * Created by fuweiwei on 2016/1/8.
 */
public class GridTextAdapter extends ArrayAdapter<Category> {

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
        Category category = getItem(position);
        item_text.setText(category.getName());
        item_text.setTag(category.getId());
        return view;
    }


}