package com.xiaoxiang.ioutside.homepage.model;

import java.util.ArrayList;

/**这是首页-文章-专题列表的情况
 * Created by zhang on 2016/4/28,0028.
 */
public class Subject {
    private int id;
    private String photo;
    private String title;
    private int type;
    private String typeName;
    private String createTime;
    private String introduction;
    private boolean observed;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introducation) {
        this.introduction = introducation;
    }

    public boolean isObserved() {
        return observed;
    }

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

    public boolean getObserved() {
        return observed;
    }

    public void setObserved(boolean observed) {
        this.observed = observed;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean isChildItem(ArrayList<Subject> list){
        for(Subject subject:list){
            if(subject.getId()==this.getId()){
                return true;
            }
        }
        return false;
    }

}
