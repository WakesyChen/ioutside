package com.xiaoxiang.ioutside.homepage.model;

/**
 * Created by Wakesy on 2016/8/12.
 */
//      首页轮播图信息
public class BannerInfo {
    private int id;
    private String title;//banner图片标题
    private String photo;//banner图片url
    private  String href;//图片对应的链接

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
