package com.benkie.hjw.map;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.benkie.hjw.view.CheckTextView1;

/**
 * Created by 37636 on 2018/1/23.
 */

public class MapAdapter extends ArrayAdapter<MapBean> {
    private boolean isBox = true;
    public MapAdapter(@NonNull Context context) {
        super(context, 0);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = new CheckTextView1(parent.getContext());
            holder = new ViewHolder();
            holder.checkTextView = (CheckTextView1) convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MapBean item = getItem(position);
        holder.initData(item,position);
        return convertView;
    }

    /**
     * 是否显示Box
     * @param box
     */
    public void setBox(boolean box) {
        isBox = !box;
    }

    class ViewHolder {
        CheckTextView1 checkTextView;
        void initData(final MapBean item, int position){
            checkTextView.setTitle(item.getPoiItem().getTitle());
            String adrs = item.getPoiItem().getProvinceName()+item.getPoiItem().getCityName()+item.getPoiItem().getAdName()+item.getPoiItem().getSnippet();
            checkTextView.setContent(adrs);
            checkTextView.setNoChecked(isBox);
            //checkTextView. setmCheckBox(item.isChecked());
        }
    }
}
