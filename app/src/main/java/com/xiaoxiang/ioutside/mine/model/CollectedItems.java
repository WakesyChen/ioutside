package com.xiaoxiang.ioutside.mine.model;

import java.util.List;

/**
 * Created by 15119 on 2016/7/23.
 */
public class CollectedItems {

    /**
     * errorMessage : null
     * errorCode : null
     * data : {"list":[{"id":2,"title":"轻量化登山 ","photo":"http://115.156.157.34/xiaoxiang-backend/img/1.jpg","content":null,"userId":2,"userName":"admin","userPhoto":"http://115.156.157.34/xiaoxiang-backend/img/aaa.jpg","messageDescript":"收藏了您推荐的文章","time":"05-24 15:19","type":"1"},{"id":4,"title":"可以","photo":"http://115.156.157.34/xiaoxiang-backend/img/1.jpg","content":null,"userId":2,"userName":"admin","userPhoto":"http://115.156.157.34/xiaoxiang-backend/img/aaa.jpg","messageDescript":"收藏了您的动态","time":"05-12 10:34","type":"2"}]}
     * accessAdmin : false
     * success : true
     */

    private Object errorMessage;
    private Object errorCode;
    private DataBean data;
    private boolean accessAdmin;
    private boolean success;

    public Object getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
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
         * id : 2
         * title : 轻量化登山
         * photo : http://115.156.157.34/xiaoxiang-backend/img/1.jpg
         * content : null
         * userId : 2
         * userName : admin
         * userPhoto : http://115.156.157.34/xiaoxiang-backend/img/aaa.jpg
         * messageDescript : 收藏了您推荐的文章
         * time : 05-24 15:19
         * type : 1
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
            private Object content;
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

            public Object getContent() {
                return content;
            }

            public void setContent(Object content) {
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
