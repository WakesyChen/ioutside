package com.xiaoxiang.ioutside.circle.model;

import java.util.List;

/**
 * Created by Wakesy on 2016/9/25.
 */
public class HotNotes {
    /**
     * {
     "list": [
     {
     "id": 2,
     "title": "怎么划皮划艇",
     "photoList": [
     "http://ioutside.com/xiaoxiang-backend/img/community/8ab14f22317681664a0951180684ee67.exe",
     "http://ioutside.com/xiaoxiang-backend/img/community/a5c551d4fe5617b75f609cde2a8f9b83.exe"
     ],
     "content": "怎么划皮划艇",
     "communityCircleID": 1,
     "publishUserID": 3,
     "publishUserName": "lxyeinsty",
     "publishUserPhoto": "http://ioutside.com/xiaoxiang-backend/img/user/8ad57633761e960156739c2c5a0667ba.jpg",
     "postType": 2,
     "publishDate": "2016/09/23 11:45",
     "viewCount": 2，
     "liked": false
     }
     ]
     }
     */
    private List<Notes> list;

    public List<Notes> getList() {
        return list;
    }

    public void setList(List<Notes> list) {
        this.list = list;
    }

    public static class Notes{
        private int id;
        private String title;
        private List<String> photoList;
        private String content;
        private int communityCircleID;
        private int publishUserID;
        private String publishUserName;
        private String publishUserPhoto;
        private int postType;
        private String publishDate;
        private int viewCount;
        private boolean liked;

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

        public List<String> getPhotoList() {
            return photoList;
        }

        public void setPhotoList(List<String> photoList) {
            this.photoList = photoList;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getCommunityCircleID() {
            return communityCircleID;
        }

        public void setCommunityCircleID(int communityCircleID) {
            this.communityCircleID = communityCircleID;
        }

        public int getPublishUserID() {
            return publishUserID;
        }

        public void setPublishUserID(int publishUserID) {
            this.publishUserID = publishUserID;
        }

        public String getPublishUserName() {
            return publishUserName;
        }

        public void setPublishUserName(String publishUserName) {
            this.publishUserName = publishUserName;
        }

        public String getPublishUserPhoto() {
            return publishUserPhoto;
        }

        public void setPublishUserPhoto(String publishUserPhoto) {
            this.publishUserPhoto = publishUserPhoto;
        }

        public int getPostType() {
            return postType;
        }

        public void setPostType(int postType) {
            this.postType = postType;
        }

        public String getPublishDate() {
            return publishDate;
        }

        public void setPublishDate(String publishDate) {
            this.publishDate = publishDate;
        }

        public int getViewCount() {
            return viewCount;
        }

        public void setViewCount(int viewCount) {
            this.viewCount = viewCount;
        }

        public boolean isLiked() {
            return liked;
        }

        public void setLiked(boolean liked) {
            this.liked = liked;
        }
    }
}
