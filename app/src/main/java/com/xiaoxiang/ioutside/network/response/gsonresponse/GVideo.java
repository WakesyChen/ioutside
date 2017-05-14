package com.xiaoxiang.ioutside.network.response.gsonresponse;


import com.xiaoxiang.ioutside.homepage.model.Video;

import java.util.ArrayList;

/**
 * Created by zhang on 2016/4/27,0027.
 */
public class GVideo {
    private ArrayList<Video> list;
    public ArrayList<Video> getList()
    {
        return list;
    }
    public void setList(ArrayList<Video> list) {
        this.list = list;
    }

}
