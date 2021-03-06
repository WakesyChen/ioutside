package com.xiaoxiang.ioutside.circle.model;

import java.util.List;

/**
 * Created by Wakesy on 2016/10/9.
 */
public class CircleType {

    /**
     * id : 2
     * title : 轻量化装备
     * photo : http://ioutside.com/xiaoxiang-backend/img/subject/37e859f255ea1451e8f5c736c9f97cde.png
     * createTime : 2016/09/23 11:05
     * ownerUserID : 0
     * ownerUserPhoto : null
     * ownerUserName : null
     * ownerUserTags : null
     * groupType : 1
     * introduction : 使用UL装备可以保证极限情况在相同资源下存活更久。
     * userNum : 0
     * postNum : 0
     * observed : false
     */

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private int id;
        private String title;
        private String photo;
        private String createTime;
        private int ownerUserID;
        private Object ownerUserPhoto;
        private Object ownerUserName;
        private Object ownerUserTags;
        private int groupType;
        private String introduction;
        private int userNum;
        private int postNum;
        private boolean observed;

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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getOwnerUserID() {
            return ownerUserID;
        }

        public void setOwnerUserID(int ownerUserID) {
            this.ownerUserID = ownerUserID;
        }

        public Object getOwnerUserPhoto() {
            return ownerUserPhoto;
        }

        public void setOwnerUserPhoto(Object ownerUserPhoto) {
            this.ownerUserPhoto = ownerUserPhoto;
        }

        public Object getOwnerUserName() {
            return ownerUserName;
        }

        public void setOwnerUserName(Object ownerUserName) {
            this.ownerUserName = ownerUserName;
        }

        public Object getOwnerUserTags() {
            return ownerUserTags;
        }

        public void setOwnerUserTags(Object ownerUserTags) {
            this.ownerUserTags = ownerUserTags;
        }

        public int getGroupType() {
            return groupType;
        }

        public void setGroupType(int groupType) {
            this.groupType = groupType;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public int getUserNum() {
            return userNum;
        }

        public void setUserNum(int userNum) {
            this.userNum = userNum;
        }

        public int getPostNum() {
            return postNum;
        }

        public void setPostNum(int postNum) {
            this.postNum = postNum;
        }

        public boolean isObserved() {
            return observed;
        }

        public void setObserved(boolean observed) {
            this.observed = observed;
        }
    }
}
