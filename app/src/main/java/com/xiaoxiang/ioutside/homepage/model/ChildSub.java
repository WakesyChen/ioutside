package com.xiaoxiang.ioutside.homepage.model;

/**
 * Created by oubin6666 on 2016/5/7.
 */
public class ChildSub {
    private int id;
    private String title;
    private int parentSubjectID;
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentSubjectID() {
        return parentSubjectID;
    }

    public void setParentSubjectID(int parentSubjectID) {
        this.parentSubjectID = parentSubjectID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
