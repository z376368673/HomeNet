package com.benkie.hjw.bean;

import java.io.Serializable;

/**
 * Created by 37636 on 2018/1/25.
 */

public class CollectProductBean implements Serializable {
    private int uid;//项目id
    private int cid;// 收藏id
    private String name;//"私人别墅",
    private String serveName;//二级服务标签,
    private String city;// 所在城市
    private String picture;//http://www.3huanju.com:8080/yetdwell/res/upload/1521469277748.jpg

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServeName() {
        return serveName;
    }

    public void setServeName(String serveName) {
        this.serveName = serveName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
