package com.benkie.hjw.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 37636 on 2018/1/24.
 */

public class TypeBean extends PopBean  implements Serializable {
    private int id;
    private String name;
    private List<Tclass> tcList;

    public TypeBean() {
    }

    public TypeBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public TypeBean(int id, String name, List<Tclass> tcList) {
        this.id = id;
        this.name = name;
        this.tcList = tcList;
    }

    public static class Tclass extends PopBean  implements Serializable{
        private int id;
        private int num;
        private String name;
        private String name2;

        public Tclass(){};
        public Tclass(int id, String name, String name2) {
            this.id = id;
            this.name = name;
            this.name2 = name2;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        @Override
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getName2() {
            return name2;
        }

        public void setName2(String name2) {
            this.name2 = name2;
        }
    }

    public List<Tclass> getTcList() {
        return tcList;
    }

    public void setTcList(List<Tclass> tcList) {
        this.tcList = tcList;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
