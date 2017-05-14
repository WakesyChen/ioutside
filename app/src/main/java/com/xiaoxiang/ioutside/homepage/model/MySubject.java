package com.xiaoxiang.ioutside.homepage.model;

/**
 * Created by zhang on 2016/5/14,0014.
 */
public class MySubject {
    private int  id;
    private String  photo;
    private String title;
    private int commentCount;
    private boolean liked;
    private int likedCount;
    private  boolean original;
    private String recommendTime;
    private int subjectID;
    private  String subjectName;
    private String subjectPhoto;
    private int viewCount;
    private String columnistSubjectName;//第二个专题栏
    private int columnistSubjectID;

    public int getColumnistSubjectID() {
        return columnistSubjectID;
    }

    public void setColumnistSubjectID(int columnistSubjectID) {
        this.columnistSubjectID = columnistSubjectID;
    }

    public String getColumnistSubjectName() {
        return columnistSubjectName;
    }

    public void setColumnistSubjectName(String columnistSubjectName) {
        this.columnistSubjectName = columnistSubjectName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(int likedCount) {
        this.likedCount = likedCount;
    }

    public boolean isOriginal() {
        return original;
    }

    public void setOriginal(boolean original) {
        this.original = original;
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

    public String getSubjectPhoto() {
        return subjectPhoto;
    }

    public void setSubjectPhoto(String subjectPhoto) {
        this.subjectPhoto = subjectPhoto;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}
