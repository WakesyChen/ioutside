package com.xiaoxiang.ioutside.activities.model;

import java.util.List;

/**
 * Created by Wakesy on 2016/8/15.
 */
public class GQaData {
    /**
     * errorMessage : null
     * errorCode : null
     * data : {"list":[{"question":"那里漂亮吗","answer":"蛮漂亮的","addTime":"2016/08/03 17:23"},{"question":"那里远吗","answer":"不远的","addTime":"2016/08/03 17:23"}]}
     * success : true
     * accessAdmin : false
     */

    private Object errorMessage;
    private Object errorCode;
    private DataBean data;
    private boolean success;
    private boolean accessAdmin;

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
         * question : 那里漂亮吗
         * answer : 蛮漂亮的
         * addTime : 2016/08/03 17:23
         */

        private List<QaData> list;

        public List<QaData> getList() {
            return list;
        }

        public void setList(List<QaData> list) {
            this.list = list;
        }


    }
}
