package com.xiaoxiang.ioutside.mine.model;

import java.util.List;

/**
 * Created by 15119 on 2016/8/8.
 */
public class Subjects {

    /**
     * errorMessage : dfsd
     * errorCode : 12
     * data : {"list":[{"id":4,"title":"山地资讯","photo":"http://ioutside.com/xiaoxiang-backend/img/subject/d3397dd899ad88ff0e5001b0e1b1be2b.jpg","type":1,"typeName":"陆地","observedNum":11,"introduction":"登山，金冰镐，登山家","createTime":"2016/04/27 00:23","observed":false},{"id":12,"title":"特写：14座8000m+南北极","photo":"http://ioutside.com/xiaoxiang-backend/img/subject/711016a17d7e6116d51f281bb0d61011.jpg","type":1,"typeName":"陆地","observedNum":9,"introduction":"14+2，永远追逐的梦想","createTime":"2016/04/27 10:16","observed":false}]}
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
         * id : 4
         * title : 山地资讯
         * photo : http://ioutside.com/xiaoxiang-backend/img/subject/d3397dd899ad88ff0e5001b0e1b1be2b.jpg
         * type : 1
         * typeName : 陆地
         * observedNum : 11
         * introduction : 登山，金冰镐，登山家
         * createTime : 2016/04/27 00:23
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
