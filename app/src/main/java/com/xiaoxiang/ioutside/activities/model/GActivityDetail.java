package com.xiaoxiang.ioutside.activities.model;

/**
 * Created by Wakesy on 2016/8/16.
 */
public class GActivityDetail {

    /**
     * errorMessage : null
     * errorCode : null
     * data : {"activity":{"activityId":3,"standard":"1:5 无装备","price":5555,"remainNum":100,"discountPrice":4545,"totalNum":100,"title":"<黄山观日出动车3日游>山顶住宿观日出","content":"产品特色\r\n 以奇松、怪石、云海、温泉、冬雪\"五绝\"著称于世 ","photoList":["http://115.156.157.32/xiaoxiang-backend/img/huangshan.jpg","http://115.156.157.32/xiaoxiang-backend/img/lusan.jpg"],"startPlace":"武汉","startDate":"2016-07-13","collected":true,"sellerId":1,"sellerPhone":"15927253301","sellerName":"ioutside","yearly":true}}
     * success : true
     * accessAdmin : false
     */

    private Object errorMessage;
    private Object errorCode;
    /**
     * activity : {"activityId":3,"standard":"1:5 无装备","price":5555,"remainNum":100,"discountPrice":4545,"totalNum":100,"title":"<黄山观日出动车3日游>山顶住宿观日出","content":"产品特色\r\n 以奇松、怪石、云海、温泉、冬雪\"五绝\"著称于世 ","photoList":["http://115.156.157.32/xiaoxiang-backend/img/huangshan.jpg","http://115.156.157.32/xiaoxiang-backend/img/lusan.jpg"],"startPlace":"武汉","startDate":"2016-07-13","collected":true,"sellerId":1,"sellerPhone":"15927253301","sellerName":"ioutside","yearly":true}
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
        /**
         * activityId : 3
         * standard : 1:5 无装备
         * price : 5555
         * remainNum : 100
         * discountPrice : 4545
         * totalNum : 100
         * title : <黄山观日出动车3日游>山顶住宿观日出
         * content : 产品特色
         以奇松、怪石、云海、温泉、冬雪"五绝"著称于世
         * photoList : ["http://115.156.157.32/xiaoxiang-backend/img/huangshan.jpg","http://115.156.157.32/xiaoxiang-backend/img/lusan.jpg"]
         * startPlace : 武汉
         * startDate : 2016-07-13
         * collected : true
         * sellerId : 1
         * sellerPhone : 15927253301
         * sellerName : ioutside
         * yearly : true
         */

        private ActivityDetail activity;

        public ActivityDetail getActivity() {
            return activity;
        }

        public void setActivity(ActivityDetail activity) {
            this.activity = activity;
        }


    }
}
