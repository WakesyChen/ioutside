package com.xiaoxiang.ioutside.mine.model;

import java.util.List;

/**
 * Created by 15119 on 2016/7/24.
 */
public class CommentedItems {

    /**
     * errorMessage : null
     * errorCode : null
     * data : {"list":[{"id":3,"title":"水中摄影之\u2014\u2014照明光源的抉择","photo":"http://115.156.157.34/xiaoxiang-backend/img/2.jpg","content":"hhhh","userId":3,"userName":"ccc","receiverName":null,"userPhoto":"http://115.156.157.34/xiaoxiang-backend/img/aaa.jpg","messageDescript":"评论了您的文章","time":"05-24 17:38","type":"1","originContent":" ","referID":-1},{"id":4,"title":"水中摄影","photo":"http://115.156.157.34/xiaoxiang-backend/img/2.jpg","content":"吊炸天啊","userId":2,"userName":"admin","receiverName":null,"userPhoto":"http://115.156.157.34/xiaoxiang-backend/img/aaa.jpg","messageDescript":"评论了您的评论","time":"05-23 16:50","type":"1","originContent":"吊炸天","referID":11},{"id":4,"title":"可以","photo":"http://115.156.157.34/xiaoxiang-backend/img/1.jpg","content":"牛逼啊","userId":2,"userName":"admin","receiverName":null,"userPhoto":"http://115.156.157.34/xiaoxiang-backend/img/aaa.jpg","messageDescript":"评论了您的评论","time":"05-18 11:00","type":"2","originContent":"牛逼","referID":10}]}
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
         * id : 3
         * title : 水中摄影之——照明光源的抉择
         * photo : http://115.156.157.34/xiaoxiang-backend/img/2.jpg
         * content : hhhh
         * userId : 3
         * userName : ccc
         * receiverName : null
         * userPhoto : http://115.156.157.34/xiaoxiang-backend/img/aaa.jpg
         * messageDescript : 评论了您的文章
         * time : 05-24 17:38
         * type : 1
         * originContent :
         * referID : -1
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
            private String receiverName;
            private String userPhoto;
            private String messageDescript;
            private String time;
            private int type;
            private String originContent;
            private int referID;

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

            public String getReceiverName() {
                return receiverName;
            }

            public void setReceiverName(String receiverName) {
                this.receiverName = receiverName;
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

            public String getOriginContent() {
                return originContent;
            }

            public void setOriginContent(String originContent) {
                this.originContent = originContent;
            }

            public int getReferID() {
                return referID;
            }

            public void setReferID(int referID) {
                this.referID = referID;
            }
        }
    }
}
