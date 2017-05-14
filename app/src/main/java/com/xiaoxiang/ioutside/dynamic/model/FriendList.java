package com.xiaoxiang.ioutside.dynamic.model;

import java.util.ArrayList;

/**
 * Created by zhang on 2016/5/29,0029.
 */
public class FriendList {
    private int userId;
    private String tag;
    private String name;
    private String userPhoto;
    private ArrayList<FriendListPhoto> footprintList;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public ArrayList<FriendListPhoto> getFootprintList() {
        return footprintList;
    }

    public void setFootprintList(ArrayList<FriendListPhoto> footprintList) {
        this.footprintList = footprintList;
    }
}
