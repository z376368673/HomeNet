package com.benkie.hjw.bean;

/**
 * Created by 37636 on 2018/3/5.
 */

public class UserInfo {
    private int userid = -1;
    private String name;
    private String imgUrl;
    private String mobile;
    private int skillDate;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getSkillDate() {
        return skillDate;
    }

    public void setSkillDate(int skillDate) {
        this.skillDate = skillDate;
    }
}
