package com.xiaoxiang.ioutside.homepage.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by oubin6666 on 2016/5/7.
 */
public class SubjectEssay implements Serializable{
    private int id;
    private String title;
    private String photo;
    private String content;
    private String tags;
    private int viewCount;
    private int commentCount;
    private int likedCount;
    private int subjectID;
    private String subjectName;
    private String recommendTime;

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRecommendTime() {
        return recommendTime;
    }

    public void setRecommendTime(String recommendTime) {
        this.recommendTime = recommendTime;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public boolean isChildItem(ArrayList<SubjectEssay> list){
        for(SubjectEssay item:list){
            if(item.getId()==this.getId()){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || ! (o instanceof SubjectEssay)) {
            return false;
        }
        return this.id == ((SubjectEssay) o).getId();
    }
}
