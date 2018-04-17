package com.benkie.hjw.bean;

/**
 * Created by 37636 on 2017/12/29.
 */

public  class PopBean {
    private int id;
    private int num;
    private String name;
    private String name2;

    public PopBean(){}

    public PopBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public PopBean(int id, String name, String name2) {
        this.id = id;
        this.name = name;
        this.name2 = name2;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public String getName2(){
        return name2;
    }
    public  int getId(){
        return  0;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}