package com.xiaoxiang.ioutside.network.response.gsonresponse;

/**
 * Created by 15119 on 2016/6/30.
 */
public class GLogin {

    /**
     * isFirstLogin : 0
     * userID : 28
     * token : 9gpQzl59LFJ4q0ruvA4rx1cJPYpDSb4ZeGGwrXWYU9OcpFVDj6XfJhQvXeY5yZ8p
     */

    private int isFirstLogin;
    private int userID;
    private String token;

    public int getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(int isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
