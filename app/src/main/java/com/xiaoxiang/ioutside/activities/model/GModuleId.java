package com.xiaoxiang.ioutside.activities.model;

import java.util.List;

/**
 * Created by Wakesy on 2016/8/16.
 */
//      通过ActivityId获取到活动描述模块Id
public class GModuleId {


    /**
     * errorMessage : null
     * errorCode : null
     * data : {"list":[{"id":18,"name":"行程介绍","content":null},{"id":19,"name":"费用说明","content":null},{"id":20,"name":"预订须知","content":null},{"id":21,"name":"常见问答","content":null}]}
     * accessAdmin : false
     * success : true
     */

    private Object errorMessage;
    private Object errorCode;
    private ModuleIds data;
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

    public ModuleIds getData() {
        return data;
    }

    public void setData(ModuleIds data) {
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


}
