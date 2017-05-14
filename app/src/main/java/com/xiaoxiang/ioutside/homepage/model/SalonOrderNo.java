package com.xiaoxiang.ioutside.homepage.model;

/**
 * Created by Wakesy on 2016/8/12.
 */
public class SalonOrderNo {

    /**
     * errorMessage : null
     * errorCode : null
     * data : {"orderNo":"20160811SD1000000220241"}
     * success : true
     * accessAdmin : false
     */

    private Object errorMessage;
    private Object errorCode;
    /**
     * orderNo : 20160811SD1000000220241
     */

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
        private String orderNo;

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }
    }
}
