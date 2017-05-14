package com.xiaoxiang.ioutside.activities.model;

/**
 * Created by Wakesy on 2016/8/19.
 */
//          活动列表下面的时间列表
public class TimeList {
    private int remainNum;
    private int totalNum;
    private String startDate;

    public int getRemainNum() {
        return remainNum;
    }

    public void setRemainNum(int remainNum) {
        this.remainNum = remainNum;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
