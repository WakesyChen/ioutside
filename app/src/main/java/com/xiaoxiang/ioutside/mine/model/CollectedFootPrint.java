package com.xiaoxiang.ioutside.mine.model;

import java.util.List;

/**
 * Created by oubin6666 on 2016/4/14.
 */
public class CollectedFootPrint {

    private int id;
    private String title;
    private String[] photoList;
    private int userID;
    private String userName;
    private String userPhoto;
    private String publishTime;
    private int outdoorTypeID;
    private String outdoorTypeName;
    private int viewCount;
    private int commentCount;
    private int likedCount;
    private String type;                          //足迹类型
    private String footprintTypeName;          //足迹类型名称
    private String place;
    private String equipmentType;
    private String brand;                       //装备品牌名称
    private boolean observed;               //是否关注了发足迹的人
    private boolean liked;
    private String thoughts;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getFootprintTypeName() {
        return footprintTypeName;
    }

    public void setFootprintTypeName(String footprintTypeName) {
        this.footprintTypeName = footprintTypeName;
    }

    public String[] getPhotoList() {
        return photoList;
    }

    public void setPhotoList(String[] photoList) {
        this.photoList = photoList;
    }

    public String getThoughts() {
        return thoughts;
    }

    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(int likedCount) {
        this.likedCount = likedCount;
    }

    public boolean isObserved() {
        return observed;
    }

    public void setObserved(boolean observed) {
        this.observed = observed;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isLiked() {
        return liked;
    }

    public int getOutdoorTypeID() {
        return outdoorTypeID;
    }

    public void setOutdoorTypeID(int outdoorTypeID) {
        this.outdoorTypeID = outdoorTypeID;
    }

    public String getOutdoorTypeName() {
        return outdoorTypeName;
    }

    public void setOutdoorTypeName(String outdoorTypeName) {
        this.outdoorTypeName = outdoorTypeName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public boolean isChildItem(List<CollectedFootPrint> list){
        for(CollectedFootPrint footPrint:list){
            if(footPrint.getId()==this.getId()){
                return true;
            }
        }
        return false;
    }

}
