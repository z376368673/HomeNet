package com.benkie.hjw.bean;

import java.util.List;

/**
 * Created by 37636 on 2018/1/25.
 */

public class ProducDetailstBean {
    private int itemId;//项目id
    private String name;//"私人别墅",
    private String uname;//"私人别墅",
    private String imgUrl;//"用户头像",
    private String address;//"广东省、深圳市、宝安区、西1",
    private String city;//项目地址
    private int mdate;//30,
    private int itemTypeId;//1,
    private int readCount;//0, 阅读量

    private int itemPoint;//0, 点赞数量
    private int itemGather;//0, 集赞数量
    private int tag;//  是否推荐  0不是 ：1是  2,集赞中

    private int flag;//0, 是否收藏
    private String mobile ="10086" ; //用户电话
    private String finishDate;//完成时间
    private List<Picture> imgs;//服务分类

    public ProducDetailstBean() {

    }

    public ProducDetailstBean(int itemId, String name, String city, String finishDate) {
        this.itemId = itemId;
        this.name = name;
        this.city = city;
        this.finishDate = finishDate;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItemPoint() {
        return itemPoint;
    }

    public void setItemPoint(int itemPoint) {
        this.itemPoint = itemPoint;
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

    public List<Picture> getImgs() {
        return imgs;
    }

    public void setImgs(List<Picture> imgs) {
        this.imgs = imgs;
    }

    public int getItemGather() {
        return itemGather;
    }

    public void setItemGather(int itemGather) {
        this.itemGather = itemGather;
    }
}
