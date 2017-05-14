package com.xiaoxiang.ioutside.activities.model;

import java.util.List;

/**
 * Created by Wakesy on 2016/8/19.
 */
public class StandardList {
    private int standardId;
    private int activityId;
    private String content;
    private int price;
    private int discountPrice;
    /**
     * remainNum : 10
     * totalNum : 10
     * startDate : 2016-09-22
     */

    private List<TimeList> timeList;

    public int getStandardId() {
        return standardId;
    }

    public void setStandardId(int standardId) {
        this.standardId = standardId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public List<TimeList> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<TimeList> timeList) {
        this.timeList = timeList;
    }
}
