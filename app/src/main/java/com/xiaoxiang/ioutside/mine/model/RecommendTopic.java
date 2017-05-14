package com.xiaoxiang.ioutside.mine.model;

import java.util.List;

/**
 * Created by 15119 on 2016/6/28.
 */


public class RecommendTopic {

    /**
     * errorMessage : null
     * errorCode : null
     * data : {"list":[{"id":32,"title":"自由潜水","photo":"http://ioutside.com/xiaoxiang-backend/img/subject/feb2e92c05c2eb3e7b29cdd699b89e81.jpg","type":2,"typeName":"水上","observedNum":0,"introduction":null,"createTime":"2016/05/16 19:21","observed":false},{"id":23,"title":"潜水世界","photo":"http://ioutside.com/xiaoxiang-backend/img/subject/e29bee981ed8a91ff62d6bcee63b8686.jpg","type":2,"typeName":"水上","observedNum":0,"introduction":null,"createTime":"2016/04/27 10:22","observed":false}]}
     * success : true
     * accessAdmin : false
     */

    private String errorMessage;
    private int errorCode;
    private DataBean data;
    private boolean success;
    private boolean accessAdmin;

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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isAccessAdmin() {
        return accessAdmin;
    }

    public void setAccessAdmin(boolean accessAdmin) {
        this.accessAdmin = accessAdmin;
    }

    public static class DataBean {
        /**
         * id : 32
         * title : 自由潜水
         * photo : http://ioutside.com/xiaoxiang-backend/img/subject/feb2e92c05c2eb3e7b29cdd699b89e81.jpg
         * type : 2
         * typeName : 水上
         * observedNum : 0
         * introduction : null
         * createTime : 2016/05/16 19:21
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
            private int type;
            private String typeName;
            private int observedNum;
            private String introduction;
            private String createTime;
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

            public int getObservedNum() {
                return observedNum;
            }

            public void setObservedNum(int observedNum) {
                this.observedNum = observedNum;
            }

            public String getIntroduction() {
                return introduction;
            }

            public void setIntroduction(String introduction) {
                this.introduction = introduction;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public boolean isObserved() {
                return observed;
            }

            public void setObserved(boolean observed) {
                this.observed = observed;
            }
        }
    }
}