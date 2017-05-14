package com.xiaoxiang.ioutside.dynamic.model;

import java.util.List;

/**
 * Created by 15119 on 2016/8/8.
 */
public class LHB {

    /**
     * errorMessage : sdsd
     * errorCode : 11
     * data : {"list":[{"id":1,"name":"aaa","photo":"http://115.156.157.34/xiaoxiang-backend/img/aaa.jpg","observed":true,"skills":"登山","experiences":"最喜欢登山"},{"id":2,"name":"admin","photo":"http://115.156.157.34/xiaoxiang-backend/img/aaa.jpg","observed":true,"skills":"登山","experiences":"最喜欢登山"},{"id":3,"name":"ccc","photo":"http://115.156.157.34/xiaoxiang-backend/img/aaa.jpg","observed":false,"skills":"登山","experiences":"最喜欢登山"},{"id":6,"name":"ooo","photo":"http://115.156.157.34/xiaoxiang-backend/img/aaa.jpg","observed":false,"skills":"登山","experiences":"最喜欢登山"}]}
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
         * name : aaa
         * photo : http://115.156.157.34/xiaoxiang-backend/img/aaa.jpg
         * observed : true
         * skills : 登山
         * experiences : 最喜欢登山
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
            private String name;
            private String photo;
            private boolean observed;
            private String skills;
            private String experiences;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public boolean isObserved() {
                return observed;
            }

            public void setObserved(boolean observed) {
                this.observed = observed;
            }

            public String getSkills() {
                return skills;
            }

            public void setSkills(String skills) {
                this.skills = skills;
            }

            public String getExperiences() {
                return experiences;
            }

            public void setExperiences(String experiences) {
                this.experiences = experiences;
            }
        }
    }
}
