package com.xiaoxiang.ioutside.network.response.gsonresponse;


import com.xiaoxiang.ioutside.homepage.model.SubjectArticle;

import java.util.ArrayList;

/**
 * Created by zhang on 2016/5/12,0012.
 */
public class GSubjectArticle {
    public ArrayList<SubjectArticle> getList() {
        return list;
    }

    public void setList(ArrayList<SubjectArticle> list) {
        this.list = list;
    }

    private ArrayList<SubjectArticle> list;

}
