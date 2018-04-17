package com.benkie.hjw.bean;

import java.util.List;

/**
 * Created by 37636 on 2018/3/7.
 */

public class SkillDetailsBean {

    private int id;
    private String imgUrl; //头像
    private String name; //用户名称
    private String mobile  ; //用户电话
    private int flag;// 是否收藏0否 1收藏
    private int pointNumber;//点赞数量
    private int userInfoId;// 是否收藏0否 1收藏
    private String distance; //距离 km
    private String address; //地址
    private String describes;//实力
    private int serveType;//服务类型 0.个人，1.团队

    private List<SkillBean> skills;

    private double lng;
    private double lat;

    private int dflag;//1代表还有会员天数0代表没有会员天数
    private int nday;// 大于0代表还有多少天会员，小于0则没有

    public int getId() {
        return id;
    }

    public double getLng() {
        return lng;
    }

    public int getUserInfoId() {
        return userInfoId;
    }

    public int getPointNumber() {
        return pointNumber;
    }

    public void setPointNumber(int pointNumber) {
        this.pointNumber = pointNumber;
    }

    public void setUserInfoId(int userInfoId) {
        this.userInfoId = userInfoId;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getDflag() {
        return dflag;
    }

    public void setDflag(int dflag) {
        this.dflag = dflag;
    }

    public int getNday() {
        return nday;
    }

    public void setNday(int nday) {
        this.nday = nday;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public int getServeType() {
        return serveType;
    }

    public void setServeType(int serveType) {
        this.serveType = serveType;
    }

    public List<SkillBean> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillBean> skills) {
        this.skills = skills;
    }
}
