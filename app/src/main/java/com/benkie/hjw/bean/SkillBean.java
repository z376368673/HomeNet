package com.benkie.hjw.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 技能
 *
 */

public class SkillBean implements Serializable {
    private int id;
    private String name;
    private int certificate;//有没有证书 （0.无，1.有）
    private int status;//是否发布（0.未发布，1.已发布
    private List<Picture> imgs;
    public SkillBean() {
    }
    public SkillBean(String name) {
        this.name = name;
    }
    public SkillBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

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

    public int getCertificate() {
        return certificate;
    }

    public void setCertificate(int certificate) {
        this.certificate = certificate;
    }

    public List<Picture> getImgs() {
        return imgs;
    }

    public void setImgs(List<Picture> imgs) {
        this.imgs = imgs;
    }
}
