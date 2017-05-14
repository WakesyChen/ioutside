package com.xiaoxiang.ioutside.mine.model;

import java.util.ArrayList;

/**
 * Created by oubin6666 on 2016/5/8.
 */
public class MyDynamicNews {
    private int id;           //足迹的ID
    private String title;
    private ArrayList<String> photoList;
    private String content;
    private int userId;
    private String userName;
    private String userPhoto;
    private int commentCount;
    private int likedCount;
    private int type;
    private int messageType;
    private String message;
    private String footprintTypeName;

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public ArrayList<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(ArrayList<String> photoList) {
        this.photoList = photoList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public boolean isChildItem(ArrayList<MyDynamicNews> list){
        for(MyDynamicNews myDynamicNews:list){
            if(myDynamicNews.getId()==this.getId()){
                return true;
            }
        }
        return false;
    }



}
