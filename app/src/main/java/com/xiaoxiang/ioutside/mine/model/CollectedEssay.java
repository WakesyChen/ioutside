package com.xiaoxiang.ioutside.mine.model;

import java.util.List;

/**
 * Created by oubin6666 on 2016/4/14.
 * 我收藏的文章
 */
public class CollectedEssay {
    private int id;
    private String title;
    private String photo;
    private String content;
    private String tags;
    private int viewCount;
    private int commentCount;
    private int likedCount;
    private int subjectID;     //文章所属专题的ID
    private String subjectName;   //文章所属专题的名字
    private String subjectPhoto;
    private String recommendTime;
    private boolean liked;

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



    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getRecommendTime() {
        return recommendTime;
    }

    public void setRecommendTime(String recommendTime) {
        this.recommendTime = recommendTime;
    }

    public String getSubjectPhoto() {
        return subjectPhoto;
    }

    public void setSubjectPhoto(String subjectPhoto) {
        this.subjectPhoto = subjectPhoto;
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

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isLiked() {
        return liked;
    }

    public boolean isChildItem(List<CollectedEssay> list){
        for(CollectedEssay essay:list){
            if(essay.getId()==this.getId()){
                return true;
            }
        }
        return false;
    }


}
