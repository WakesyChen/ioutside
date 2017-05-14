package com.xiaoxiang.ioutside.activities.model;

import java.io.Serializable;

/**
 * Created by Wakesy on 2016/8/24.
 */
public class OrderInfor  implements Serializable{
    private String sellerPhone;
    private String title;
    private int remainNum;
    private String startDate;//活动开始时间
    private String content;
    private String startPlace;
    private double discountPrice;
    private int activityId;
    private int sellerID;
    private int activitySpecID;//活动规格id
//    private String activityTime;



    public int getSellerID() {
        return sellerID;
    }

    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public int getActivitySpecID() {
        return activitySpecID;
    }

    public void setActivitySpecID(int activitySpecID) {
        this.activitySpecID = activitySpecID;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRemainNum() {
        return remainNum;
    }

    public void setRemainNum(int remainNum) {
        this.remainNum = remainNum;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
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
}
