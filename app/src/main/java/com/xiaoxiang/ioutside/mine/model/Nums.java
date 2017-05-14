package com.xiaoxiang.ioutside.mine.model;

import java.io.Serializable;


/**
 * Created by oubin6666 on 2016/4/14.
 */
public class Nums implements Serializable {
    private int recommend_count;
    private int fans_count;
    private int observe_count;
    private int dynamic_count;   //用户动态数目

    public int getDynamic_count() {
        return dynamic_count;
    }

    public void setDynamic_count(int dynamic_count) {
        this.dynamic_count = dynamic_count;
    }

    public int getFansCount() {
        return fans_count;
    }

    public void setFans_count(int fans_count) {
        this.fans_count = fans_count;
    }

    public int getObserveCount() {
        return observe_count;
    }

    public void setObserve_count(int observe_count) {
        this.observe_count = observe_count;
    }

    public int getRecommendCount() {
        return recommend_count;
    }

    public void setRecommend_count(int recommend_count) {
        this.recommend_count = recommend_count;
    }
}
