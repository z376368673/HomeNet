package com.benkie.hjw.map;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.services.core.PoiItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 37636 on 2018/1/23.
 */

public class MapBean implements Parcelable {
    private int id;
    private PoiItem poiItem;
    private boolean isChecked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PoiItem getPoiItem() {
        return poiItem;
    }

    public void setPoiItem(PoiItem poiItem) {
        this.poiItem = poiItem;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public static  List<MapBean> addAll(List<PoiItem> poiItems){
        List<MapBean> list = new ArrayList<>();
        if (poiItems==null)return list;
        for (int i = 0; i <poiItems.size() ; i++) {
            MapBean mapBean = new MapBean();
            mapBean.setId(i);
            mapBean.setPoiItem(poiItems.get(i));
            mapBean.setChecked(false);
            list.add(mapBean);
        }
        return list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
