package com.xiaoxiang.ioutside.activities.model;

import com.xiaoxiang.ioutside.common.alipaydemo.OrderInfoBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wakesy on 2016/8/28.
 *
 * 通过订单号获取到的订单信息
 */
public class OrderInforById {


    /**
     * id : 6
     * orderUserID : 3
     * activityID : 1
     * sellerID : 1
     * seller : admin
     * activitySpecID : 1
     * activityQuantity : 2
     * contactUser : lixinyan
     * contactPhone : 15927253301
     * orderNumber : 201607161000000011450
     * orderCode : 14686385382344411
     * orderPrice : 5880
     * remark : 这个活动很酷
     * orderStatus : 0
     * activityTitle : <马尔代夫双鱼岛Olhuveli5晚7日自助游>武汉出发，第1晚马累
     * activitySpecDesc : 1:1无装备
     * activityPhoto : http://ioutside.com/xiaoxiang-backend/img/footprint/22d1c2eb3c5a525da665b7fc7e5b87bb.jpg
     * payWay : 0
     * activityTime : 2016-08-01
     * startPlace : 武汉
     * tourists : [{"id":1,"addUser":3,"name":"lixinyan","idCard":"430621199511024617","phone":"15927253301","passport":"1234567","createTime":"2016/07/15 13:36"},{"id":2,"addUser":3,"name":"lixinyan","idCard":"430621199511024617","phone":"15927253301","passport":"1234567","createTime":"2016/07/15 13:42"}]
     */

    private ListBean list;

    public ListBean getList() {
        return list;
    }

    public void setList(ListBean list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        private int id;
        private int orderUserID;
        private int activityID;
        private int sellerID;
        private String seller;
        private int activitySpecID;
        private int activityQuantity;
        private String contactUser;
        private String contactPhone;
        private String orderNumber;
        private String orderCode;
        private int orderPrice;
        private String remark;
        private int orderStatus;
        private String activityTitle;
        private String activitySpecDesc;
        private String activityPhoto;
        private int payWay;
        private String activityTime;
        private String startPlace;
        private List<TouristsBean>tourists;

        public List<TouristsBean> getTourists() {
            return tourists;
        }

        public void setTourists(List<TouristsBean> tourists) {
            this.tourists = tourists;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getOrderUserID() {
            return orderUserID;
        }

        public void setOrderUserID(int orderUserID) {
            this.orderUserID = orderUserID;
        }

        public int getActivityID() {
            return activityID;
        }

        public void setActivityID(int activityID) {
            this.activityID = activityID;
        }

        public int getSellerID() {
            return sellerID;
        }

        public void setSellerID(int sellerID) {
            this.sellerID = sellerID;
        }

        public String getSeller() {
            return seller;
        }

        public void setSeller(String seller) {
            this.seller = seller;
        }

        public int getActivitySpecID() {
            return activitySpecID;
        }

        public void setActivitySpecID(int activitySpecID) {
            this.activitySpecID = activitySpecID;
        }

        public int getActivityQuantity() {
            return activityQuantity;
        }

        public void setActivityQuantity(int activityQuantity) {
            this.activityQuantity = activityQuantity;
        }

        public String getContactUser() {
            return contactUser;
        }

        public void setContactUser(String contactUser) {
            this.contactUser = contactUser;
        }

        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public int getOrderPrice() {
            return orderPrice;
        }

        public void setOrderPrice(int orderPrice) {
            this.orderPrice = orderPrice;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getActivityTitle() {
            return activityTitle;
        }

        public void setActivityTitle(String activityTitle) {
            this.activityTitle = activityTitle;
        }

        public String getActivitySpecDesc() {
            return activitySpecDesc;
        }

        public void setActivitySpecDesc(String activitySpecDesc) {
            this.activitySpecDesc = activitySpecDesc;
        }

        public String getActivityPhoto() {
            return activityPhoto;
        }

        public void setActivityPhoto(String activityPhoto) {
            this.activityPhoto = activityPhoto;
        }

        public int getPayWay() {
            return payWay;
        }

        public void setPayWay(int payWay) {
            this.payWay = payWay;
        }

        public String getActivityTime() {
            return activityTime;
        }

        public void setActivityTime(String activityTime) {
            this.activityTime = activityTime;
        }

        public String getStartPlace() {
            return startPlace;
        }

        public void setStartPlace(String startPlace) {
            this.startPlace = startPlace;
        }
    }
}
