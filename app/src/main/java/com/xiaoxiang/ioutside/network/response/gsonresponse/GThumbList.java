package com.xiaoxiang.ioutside.network.response.gsonresponse;


import com.xiaoxiang.ioutside.dynamic.model.ThumbList;

import java.util.ArrayList;

/**
 * Created by zhang on 2016/4/28,0028.
 */
public class GThumbList {
    private ArrayList<ThumbList> list;
    public ArrayList<ThumbList> getList()
    {
        return list;
    }
    public void setList(ArrayList<ThumbList> list) {
        this.list = list;
    }
}
