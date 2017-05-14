package com.xiaoxiang.ioutside.network.response.gsonresponse;


import com.xiaoxiang.ioutside.homepage.model.SubjectEssay;

import java.util.ArrayList;

/**
 * Created by oubin6666 on 2016/5/7.
 */
public class GChildSubEssayList {
    private ArrayList<SubjectEssay> articleList;

    public ArrayList<SubjectEssay> getArticleList() {
        return articleList;
    }

    public void setArticleList(ArrayList<SubjectEssay> articleList) {
        this.articleList = articleList;
    }
}
