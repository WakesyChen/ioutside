package com.xiaoxiang.ioutside.activities.model;

import java.io.Serializable;

/**
 * Created by Wakesy on 2016/8/24.
 */
public class PersonInfor implements Serializable{

    /**
     * id : 2
     * addUser : 3
     * name : lixinyan
     * idCard : 430621199511024617
     * phone : 15927253301
     * passport : 1234567
     * createTime : 2016/07/15 13:42
     */

    private int id;
    private int addUser;
    private String name;
    private String idCard;
    private String phone;
    private String passport;
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAddUser() {
        return addUser;
    }

    public void setAddUser(int addUser) {
        this.addUser = addUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
