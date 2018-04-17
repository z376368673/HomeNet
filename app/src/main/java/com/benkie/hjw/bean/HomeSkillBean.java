package com.benkie.hjw.bean;

import java.util.List;

/**
 * Created by 37636 on 2018/2/7.
 */

public class HomeSkillBean {

    private int id;
    private String name;
    private String address;
    private String describes;//实力
    private int serveType;//服务类型 0个人1团队
    private int distance;//距离 单位 米
    private int pointNumber;//点赞数
    private double lng;//经纬度
    private double lat;//

    private int userInfoId;//用户id
    private String mobile;//电话
    private String imgUrl;

    private List<SkillBean> skills;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public int getServeType() {
        return serveType;
    }

    public void setServeType(int serveType) {
        this.serveType = serveType;
    }

    public double getLng() {
        return lng;
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

    public int getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(int userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<SkillBean> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillBean> skills) {
        this.skills = skills;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }


}
