package com.xiaoxiang.ioutside.activities.model;

import java.util.List;

/**
 * Created by Wakesy on 2016/8/16.
 */
public class ActivityDetail {
    /**
     * activityId : 3
     * standard : 1:5 无装备
     * price : 5555
     * remainNum : 100
     * discountPrice : 4545
     * totalNum : 100
     * title : <黄山观日出动车3日游>山顶住宿观日出
     * content : 产品特色
     以奇松、怪石、云海、温泉、冬雪"五绝"著称于世
     * photoList : ["http://115.156.157.32/xiaoxiang-backend/img/huangshan.jpg","http://115.156.157.32/xiaoxiang-backend/img/lusan.jpg"]
     * startPlace : 武汉
     * startDate : 2016-07-13
     * collected : true
     * sellerId : 1
     * sellerPhone : 15927253301
     * sellerName : ioutside
     * yearly : true
     */
    private int activityId;
    private String standard;
    private int price;
    private int remainNum;
    private int discountPrice;
    private int totalNum;
    private String title;
    private String content;
    private String startPlace;
    private String startDate;
    private boolean collected;
    private int sellerId;
    private String sellerPhone;
    private String sellerName;
    private boolean yearly;
    private List<String> photoList;

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRemainNum() {
        return remainNum;
    }

    public void setRemainNum(int remainNum) {
        this.remainNum = remainNum;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public boolean isYearly() {
        return yearly;
    }

    public void setYearly(boolean yearly) {
        this.yearly = yearly;
    }

    public List<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<String> photoList) {
        this.photoList = photoList;
    }
}

