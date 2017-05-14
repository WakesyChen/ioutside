package com.xiaoxiang.ioutside.homepage;

import java.io.Serializable;

/**
 * Created by Wakesy on 2016/9/1.
 */
public class PersonInfoAll {

    /**
     * id : 244
     * name : Wakesy
     * photo : http://ioutside.com/xiaoxiang-backend/img/user/2ae61e01cc1c77808f0451f83fde4b6d.jpg
     * phone : 15623786928
     * email : null
     * emailState : 0
     * phoneState : 2
     * userType : 0
     * sex : m
     * level : 2
     * address : null
     * skills : 闲云野鹤,专注打野二十年
     * experiences : null
     * score : 86
     * tag : null
     * openID : null
     */

    private UserBean user;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean implements Serializable{
        private int id;
        private String name;
        private String photo;
        private String phone;
        private Object email;
        private String emailState;
        private String phoneState;
        private String userType;
        private String sex;
        private int level;
        private Object address;
        private String skills;
        private Object experiences;
        private int score;
        private Object tag;
        private Object openID;

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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public String getEmailState() {
            return emailState;
        }

        public void setEmailState(String emailState) {
            this.emailState = emailState;
        }

        public String getPhoneState() {
            return phoneState;
        }

        public void setPhoneState(String phoneState) {
            this.phoneState = phoneState;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public String getSkills() {
            return skills;
        }

        public void setSkills(String skills) {
            this.skills = skills;
        }

        public Object getExperiences() {
            return experiences;
        }

        public void setExperiences(Object experiences) {
            this.experiences = experiences;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public Object getTag() {
            return tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }

        public Object getOpenID() {
            return openID;
        }

        public void setOpenID(Object openID) {
            this.openID = openID;
        }
    }
}
