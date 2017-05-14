package com.xiaoxiang.ioutside.activities.model;

import java.io.Serializable;

/**
 * Created by Wakesy on 2016/8/24.
 */
public class TravelerInfor implements Serializable{
    private String name;
    private String phone;
    private String personID;
    private String passport;
    private int touristId;

    public TravelerInfor(String name, String phone, String personID, String passport, int touristId) {
        this.name = name;
        this.phone = phone;
        this.personID = personID;
        this.passport = passport;
        this.touristId = touristId;
    }

    public TravelerInfor() {
    }

    public int getTouristId() {
        return touristId;
    }

    public void setTouristId(int touristId) {
        this.touristId = touristId;
    }

    public TravelerInfor(String name, String phone, String personID, String passport) {
        this.name = name;
        this.phone = phone;
        this.personID = personID;
        this.passport = passport;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public TravelerInfor(String name, String phone, String personID) {
        this.name = name;
        this.phone = phone;
        this.personID = personID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
