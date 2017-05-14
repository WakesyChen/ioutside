package com.xiaoxiang.ioutside.network.response.gsonresponse;


import com.xiaoxiang.ioutside.model.CommentList;

import java.util.ArrayList;

/**
 * Created by zhang on 2016/5/3/0003.
 */
public class GCommentList {
    private ArrayList<CommentList> list;
    public ArrayList<CommentList> getList() {
        return list;
   }
   public void setList(ArrayList<CommentList> list) {
       this.list = list;
   }
}
