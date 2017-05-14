package com.xiaoxiang.ioutside.dynamic.model;

import java.util.ArrayList;

/*发现-推荐详细情况
 * Created by zhang on 2016/5/1/0001.
 */
public class ThumbListDetail {
    private  String brand;//null
    private int commentCount;
    private ArrayList<String> footprintTypeList;
    private  String equipmentType;//null
    private  String footprintTypeName;//null
    private int id;
    private int likedCount;
    private boolean observed;
    private boolean liked;
    private int outdoorTypeID;
    private  String outdoorTypeName;
    private ArrayList<String> photoList;
    private  String place;//null
    private String publishTime;
    private  String thoughts;
    private int type;
    private int userID;
    private  String userName;
    private  String userPhoto;
    private int viewCount;
    private String footprintType;
    private int level;
    private boolean collected;
    private boolean mine;
    private boolean recommend;
    private String video;

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public ArrayList<String> getFootprintTypeList() {
        return footprintTypeList;
    }

    public void setFootprintTypeList(ArrayList<String> footprintTypeList) {
        this.footprintTypeList = footprintTypeList;
    }

    public String getFootprintType() {
        return footprintType;
    }

    public void setFootprintType(String footprintType) {
        this.footprintType = footprintType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public ArrayList<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(ArrayList<String> photoList) {
        this.photoList = photoList;
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

    public String getThoughts() {
        return thoughts;
    }

    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
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

}
