package com.xiaoxiang.ioutside.network.response.gsonresponse;


import com.xiaoxiang.ioutside.dynamic.model.ThumbListOfObservedUser;

import java.util.ArrayList;

/**
 * Created by zhang on 2016/4/28,0028.
 */
public class GThumbListOfObservedUser {
    private ArrayList<ThumbListOfObservedUser> list;
    public ArrayList<ThumbListOfObservedUser> getList()
    {
        return list;
    }
    public void setList(ArrayList<ThumbListOfObservedUser> list) {
        this.list = list;
    }
}
