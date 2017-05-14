package com.xiaoxiang.ioutside.common.alipaydemo;

import java.io.Serializable;

/**
 * Created by Wakesy on 2016/8/10.
 */
public class OrderInfoBean implements Serializable {
    
    private String productName;
    private String productDescri;
    private double productPrice;
    private String orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public OrderInfoBean(String productName, String productDescri, double productPrice,String orderNo) {
        this.productName = productName;
        this.productDescri = productDescri;
        this.productPrice = productPrice;
        this.orderNo=orderNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescri() {
        return productDescri;
    }

    public void setProductDescri(String productDescri) {
        this.productDescri = productDescri;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }
}