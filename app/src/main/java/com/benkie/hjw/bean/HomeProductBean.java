package com.benkie.hjw.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 37636 on 2018/1/25.
 */

public class HomeProductBean implements Serializable {
    private int itemId;//项目id
    private String name;//"私人别墅",
    private String address;//"广东省、深圳市、宝安区、西1",
    private String city;//项目地址
    private int mdate;//30,剩余展示时间
    private int itemTypeId;//1项目分类id,
    private String typeName;//项目分类名称
    private int readCount;//0, 阅读量
    private int itemPoint;//0, 点赞数量
    private int tag;//  是否推荐  0不是 ：1是  2,集赞中
    private String finishDate;//完成时间
    private Picture imgs;//服务分类
    private double nextdate;//时间戳

    public HomeProductBean() {

    }

    public HomeProductBean(int itemId, String name, String city, String finishDate) {
        this.itemId = itemId;
        this.name = name;
        this.city = city;
        this.finishDate = finishDate;
    }

    public double getNextdate() {
        return nextdate;
    }

    public void setNextdate(double nextdate) {
        this.nextdate = nextdate;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getItemPoint() {
        return itemPoint;
    }

    public void setItemPoint(int itemPoint) {
        this.itemPoint = itemPoint;
    }

    public int getMdate() {
        return mdate;
    }

    public void setMdate(int mdate) {
        this.mdate = mdate;
    }

    public int getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(int itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public Picture getImgs() {
        return imgs;
    }

    public void setImgs(Picture imgs) {
        this.imgs = imgs;
    }
}
