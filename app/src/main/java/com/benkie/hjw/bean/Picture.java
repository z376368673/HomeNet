package com.benkie.hjw.bean;

import android.os.Parcel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 37636 on 2018/3/7.
 */

public class  Picture  implements Serializable {
    private int id;//30,
    private int type;//图片类型
    private String describes;//描述
    private String picture;//图片
    private List<TypeBean> service;//服务分类

    public Picture(){}

    public Picture(int id, String picture) {
        this.id = id;
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<TypeBean> getService() {
        return service;
    }

    public void setService(List<TypeBean> service) {
        this.service = service;
    }
}