package com.xiaoxiang.ioutside.mine.model;

import java.util.List;

/**
 * Created by 15119 on 2016/7/23.
 */
public class LikedItems {

    /**
     * errorMessage : null
     * errorCode : null
     * data : {"list":[{"id":1,"title":"赞赞赞","photo":"http://115.156.157.34/xiaoxiang-backend/img/1.jpg","content":null,"userId":3,"userName":"ccc","userPhoto":"http://115.156.157.34/xiaoxiang-backend/img/aaa.jpg","messageDescript":"赞了您的动态","time":"05-10 21:03","type":"2"},{"id":3,"title":"水中摄影之\u2014\u2014照明光源的抉择","photo":"http://115.156.157.34/xiaoxiang-backend/img/2.jpg","content":null,"userId":2,"userName":"admin","userPhoto":"http://115.156.157.34/xiaoxiang-backend/img/aaa.jpg","messageDescript":"赞了您推荐的文章","time":"05-05 21:08","type":"1"}]}
     * accessAdmin : false
     * success : true
     */

    private String errorMessage;
    private int errorCode;
    private DataBean data;
    private boolean accessAdmin;
    private boolean success;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isAccessAdmin() {
        return accessAdmin;
    }

    public void setAccessAdmin(boolean accessAdmin) {
        this.accessAdmin = accessAdmin;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBean {
        /**
         * id : 1
         * title : 赞赞赞
         * photo : http://115.156.157.34/xiaoxiang-backend/img/1.jpg
         * content : null
         * userId : 3
         * userName : ccc
         * userPhoto : http://115.156.157.34/xiaoxiang-backend/img/aaa.jpg
         * messageDescript : 赞了您的动态
         * time : 05-10 21:03
         * type : 2
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
            private String content;
            private int userId;
            private String userName;
            private String userPhoto;
            private String messageDescript;
            private String time;
            private int type;

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

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
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

            public String getMessageDescript() {
                return messageDescript;
            }

            public void setMessageDescript(String messageDescript) {
                this.messageDescript = messageDescript;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
