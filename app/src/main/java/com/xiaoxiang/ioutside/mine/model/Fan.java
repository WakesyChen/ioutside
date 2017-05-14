package com.xiaoxiang.ioutside.mine.model;

import java.util.ArrayList;

/**
 * Created by oubin6666 on 2016/4/8.
 */
public class Fan {
    private int id;
    private String name;
    private String photo;
    private char sex;
    private int level;
    private String address;
    private boolean observed;

    public boolean isObserved() {
        return observed;
    }

    public void setObserved(boolean observed) {
        this.observed = observed;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isChildItem(ArrayList<Fan> list){
        for(Fan fan:list){
            if(fan.getId()==this.getId()){
                return true;
            }
        }
        return false;
    }



}
