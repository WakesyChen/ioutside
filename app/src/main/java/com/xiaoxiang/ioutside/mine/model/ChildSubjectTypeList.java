package com.xiaoxiang.ioutside.mine.model;

import java.util.List;

/**
 * Created by 15119 on 2016/8/8.
 */
public class ChildSubjectTypeList {

    /**
     * errorMessage : dafsadfsdf
     * errorCode : 12
     * data : {"list":[{"id":5,"typeName":"登山","createTime":"2016/06/09 16:14"},{"id":6,"typeName":"徒步露营","createTime":"2016/06/09 16:15"},{"id":7,"typeName":"跑步","createTime":"2016/06/09 16:15"},{"id":8,"typeName":"攀岩攀冰","createTime":"2016/06/09 16:15"},{"id":9,"typeName":"铁人三项","createTime":"2016/06/09 16:16"}]}
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
         * id : 5
         * typeName : 登山
         * createTime : 2016/06/09 16:14
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
            private String typeName;
            private String createTime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }
        }
    }
}
