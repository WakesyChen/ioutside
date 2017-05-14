package com.xiaoxiang.ioutside.network.response.gsonresponse;

import java.util.ArrayList;

/**
 * Created by zhang on 2016/5/8/0008.
 */
public class GTest {
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public G123 getData() {
        return data;
    }

    public void setData(G123 data) {
        this.data = data;
    }

    private G123 data;
    private String errorMessage;
    private String errorCode;
    private boolean success;
  public static class G123{
        public ArrayList<Gsubfg123> getList() {
            return list;
        }

        public void setList(ArrayList<Gsubfg123> list) {
            this.list = list;
        }

        private ArrayList<Gsubfg123> list;
    public static   class Gsubfg123{
           public int getCommentID() {
               return commentID;
           }

           public void setCommentID(int commentID) {
               this.commentID = commentID;
           }

           public String getCommentTime() {
               return commentTime;
           }

           public void setCommentTime(String commentTime) {
               this.commentTime = commentTime;
           }

           public String getContent() {
               return content;
           }

           public void setContent(String content) {
               this.content = content;
           }

           public int getFavorCount() {
               return favorCount;
           }

           public void setFavorCount(int favorCount) {
               this.favorCount = favorCount;
           }

           public int getPostID() {
               return postID;
           }

           public void setPostID(int postID) {
               this.postID = postID;
           }

           public char getPostType() {
               return postType;
           }

           public void setPostType(char postType) {
               this.postType = postType;
           }

           public Userme getUser() {
               return user;
           }

           public void setUser(Userme user) {
               this.user = user;
           }

           public int commentID;
           public String commentTime;
           public String content;
           public int favorCount;
           public int postID;
           public char postType;
           public Userme user;
         public static class Userme{
               public int getId() {
                   return id;
               }

               public void setId(int id) {
                   this.id = id;
               }

               public int getLevel() {
                   return level;
               }

               public void setLevel(int level) {
                   this.level = level;
               }

               public String getAddress() {
                   return address;
               }

               public void setAddress(String address) {
                   this.address = address;
               }

               public String getName() {
                   return name;
               }

               public void setName(String name) {
                   this.name = name;
               }

               public String getNickname() {
                   return nickname;
               }

               public void setNickname(String nickname) {
                   this.nickname = nickname;
               }

               public String getPhoto() {
                   return photo;
               }

               public void setPhoto(String photo) {
                   this.photo = photo;
               }

               public String getSex() {
                   return sex;
               }

               public void setSex(String sex) {
                   this.sex = sex;
               }

               public String address;
               public int id;
               public int level;
               public String name;
               public String nickname;
               public String photo;
               public String sex;

           }
       }

    }


}




