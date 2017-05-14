package com.xiaoxiang.ioutside.mine.model;

import java.util.List;

/**
 * Created by 15119 on 2016/6/29.
 */
public class RecommendPeople {

    /**
     * errorMessage : null
     * errorCode : null
     * data : {"list":[{"id":98,"name":"潜水员","photo":"http://ioutside.com/xiaoxiang-backend/img/user/5436b2551dde58ae12ff8dd63d517f49.jpg","tag":"潜水，自由潜"},{"id":45,"name":"国家地理爱好者","photo":"http://ioutside.com/xiaoxiang-backend/img/user/6bb48715fad27684e91730e136ca2190.jpg","tag":"户外摄影达人，国家地理，历史文化"},{"id":29,"name":"小王子","photo":"http://ioutside.com/xiaoxiang-backend/img/user/489ea6480afb1b1062cffd14f9c41978.jpg","tag":"独木舟达人"},{"id":11,"name":"hugo","photo":"http://ioutside.com/xiaoxiang-backend/img/user/61f2ebda25d65721116412f0d5a01b06.jpg","tag":"全能选手"},{"id":47,"name":"amomo","photo":"http://ioutside.com/xiaoxiang-backend/img/user/53a5d6660efd95da4d13aea976af7372.jpg","tag":null},{"id":4,"name":"Carl","photo":"http://ioutside.com/xiaoxiang-backend/img/user/8583bef4a50012527896570df04c286d.jpg","tag":"攀岩大神"},{"id":10,"name":"硬币","photo":"http://ioutside.com/xiaoxiang-backend/img/user/7fdd105ff95d3a1a2fb286852f05dcdc.jpg","tag":null},{"id":39,"name":"ioutside","photo":"http://ioutside.com/xiaoxiang-backend/img/user/37eb2f6861fb240e2427fc0b3ce7391c.jpg","tag":null},{"id":12,"name":"非鱼","photo":"http://ioutside.com/xiaoxiang-backend/img/user/74625a19272e417d4de8fffa27136d7c.jpg","tag":null},{"id":2,"name":"nM_JW","photo":"http://ioutside.com/xiaoxiang-backend/img/user/4380bfc504266482e2c8dd78263c822f.jpg","tag":"扎帐篷小能手"},{"id":38,"name":"mountain","photo":"http://ioutside.com/xiaoxiang-backend/img/user/user-default-photo12.jpg","tag":null},{"id":40,"name":"watersports","photo":"http://ioutside.com/xiaoxiang-backend/img/user/user-default-photo6.jpg","tag":null},{"id":31,"name":"小丸子","photo":"http://ioutside.com/xiaoxiang-backend/img/user/356592130801b4ed76fc67c477003fcf.jpg","tag":null},{"id":22,"name":"小牛顿 ","photo":"http://ioutside.com/xiaoxiang-backend/img/user/d1d88d379dce561b346f9883cc82e0ed.jpg","tag":null},{"id":19,"name":"morningking","photo":"http://ioutside.com/xiaoxiang-backend/img/user/0592fe126700b378f07a1808c93cbc25.jpg","tag":"后台开发"},{"id":7,"name":"xiaocheng","photo":"http://ioutside.com/xiaoxiang-backend/img/user/9fc0351180b6a324b6441f53585f3827.jpg","tag":null}]}
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
         * id : 98
         * name : 潜水员
         * photo : http://ioutside.com/xiaoxiang-backend/img/user/5436b2551dde58ae12ff8dd63d517f49.jpg
         * tag : 潜水，自由潜
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
            private String tag;

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

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }
        }
    }
}
