package com.xiaoxiang.ioutside.homepage.model;

/**
 * Created by Wakesy on 2016/8/11.
 */

//      沙龙活动规格列表
public class SalonInfo {

    private int id;
    private int salonPeriods;
    private int totalNum;//总票数
    private int remainNum;//剩余票数
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSalonPeriods() {
        return salonPeriods;
    }

    public void setSalonPeriods(int salonPeriods) {
        this.salonPeriods = salonPeriods;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getRemainNum() {
        return remainNum;
    }

    public void setRemainNum(int remainNum) {
        this.remainNum = remainNum;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
