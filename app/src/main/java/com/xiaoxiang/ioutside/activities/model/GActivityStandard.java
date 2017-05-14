package com.xiaoxiang.ioutside.activities.model;

import java.util.List;

/**
 * Created by Wakesy on 2016/8/19.
 */
public class GActivityStandard {

    /**
     * standardId : 1
     * activityId : 1
     * content : 1:1无装备
     * price : 7777
     * discountPrice : 6868
     * timeList : [{"remainNum":10,"totalNum":10,"startDate":"2016-09-22"},{"remainNum":11,"totalNum":11,"startDate":"2016-09-13"},{"remainNum":12,"totalNum":12,"startDate":"2016-09-21"},{"remainNum":13,"totalNum":13,"startDate":"2016-09-15"}]
     */

    private List<StandardList> list;

    public List<StandardList> getList() {
        return list;
    }

    public void setList(List<StandardList> list) {
        this.list = list;
    }


}
